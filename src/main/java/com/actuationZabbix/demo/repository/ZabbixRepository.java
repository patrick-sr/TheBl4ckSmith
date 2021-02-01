package com.actuationZabbix.demo.repository;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Project: "demo", created at 30/01/2021 by Patrick Rosa.
 */

@Repository
public class ZabbixRepository {

    private String endpoint = "http://opti.xpinc.io/zabbix-hml/api_jsonrpc.php";

    public boolean acknowledgeZabbixAlert(String ticketServiceNow) {
        AtomicBoolean ackAlert = new AtomicBoolean(false);
        String eventsResult = requestToZabbix(getProblems());

        JSONObject json = new JSONObject(eventsResult );
            json.getJSONArray("result").forEach(alert -> {
                JSONObject jsonObject = new JSONObject(alert.toString());
                if (!jsonObject.isNull("tags")) {
                    if (!jsonObject.getJSONArray("tags").isEmpty()) {
                        jsonObject.getJSONArray("tags").forEach(tag -> {
                            JSONObject jsonTag = new JSONObject(tag.toString());
                            if(jsonTag.getString("value").trim().equals(ticketServiceNow.trim())) {
                                setAcknowledgeToEvent(jsonObject.getString("eventid"));
                                ackAlert.set(true);
                            }
                        });
                    }
                }
            });

        return ackAlert.get();
    }

    private void setAcknowledgeToEvent(String eventid) {
        String setJson = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"event.acknowledge\",\n" +
                "    \"params\": {\n" +
                "        \"eventids\": \""+ eventid +"\",\n" +
                "        \"action\": \"6\",\n" +
                "        \"message\": \"Time responsável acionado.\"\n" +
                "    },\n" +
                "    \"auth\": \"" + getTokenFromZabbix() + "\",\n" +
                "    \"id\": 1\n" +
                "}";

        requestToZabbix(setJson);
    }

    public boolean deactivateExpiredTriggerOnZabbixAlert() {
        AtomicBoolean deactivated = new AtomicBoolean(false);
        String problems = requestToZabbix(getProblems());

        JSONObject json = new JSONObject(problems);
        json.getJSONArray("result").forEach(alert -> {
            JSONObject jsonObject = new JSONObject(alert.toString());

            Timestamp clockZabbix = new Timestamp(jsonObject.getLong("clock"));

            Date dt = new Date();
            Timestamp dateNow = new Timestamp(dt.getTime());

            if (clockZabbix.toLocalDateTime().isBefore(dateNow.toLocalDateTime().minusDays(5))) {
                String jsonDeactivate = "";

                requestToZabbix(jsonDeactivate);
                deactivated.set(true);
            }
        });


        return deactivated.get();
    }

    private String requestToZabbix(String json) {
        String result = "";
        try {
            URL u = new URL(endpoint);
            HttpURLConnection con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setReadTimeout(25000);
            con.setConnectTimeout(25000);
            con.setDoOutput(true);
            con.setUseCaches(false);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(json.getBytes(StandardCharsets.UTF_8));
            outputStream.close();

            con.connect();

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    result = response.append(inputLine).toString();
                }
                in.close();
            }


            con.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getTokenFromZabbix() {
        JSONObject json = new JSONObject(requestToZabbix(getJsonAuth()));
        return json.getString("result");
    }

    private String getJsonAuth() {
        return "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"user.login\",\n" +
                "    \"params\": {\n" +
                "        \"user\": \"U002422\",\n" +
                "        \"password\": \"Brasil@1996\"\n" +
                "    },\n" +
                "    \"id\": 1,\n" +
                "    \"auth\": null\n" +
                "}";
    }

    private String getProblems() {

        return "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"problem.get\",\n" +
                "    \"params\": {\n" +
                "        \"selectTags\": \"extend\"\n" +
                "    },\n" +
                "    \"auth\": \"" + getTokenFromZabbix() + "\",\n" +
                "    \"id\": 1\n" +
                "}";
    }
}
