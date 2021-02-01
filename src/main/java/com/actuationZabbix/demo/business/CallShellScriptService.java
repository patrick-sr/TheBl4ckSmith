package com.actuationZabbix.demo.business;

import java.io.IOException;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import com.actuationZabbix.demo.domain.AlertDomain;
import com.actuationZabbix.demo.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.springframework.stereotype.Service;

/**
 * Project: "demo", created at 30/01/2021 by Patrick Rosa.
 */

@Service
@RequiredArgsConstructor
public class CallShellScriptService {

    private final AlertRepository repository;

    int iExitValue;
    String sCommandString;

    public int runScript(String telephoneNumber, String ticketServiceNow) {

        telephoneNumber = checkAndAdjustTelephone(telephoneNumber);
        saveAlert(telephoneNumber, ticketServiceNow);

        sCommandString = "sh /data/discap1.sh " + telephoneNumber + " " + ticketServiceNow;
        CommandLine oCmdLine = CommandLine.parse(sCommandString);
        DefaultExecutor oDefaultExecutor = new DefaultExecutor();
        oDefaultExecutor.setExitValue(0);
        try {
            iExitValue = oDefaultExecutor.execute(oCmdLine);
        } catch (ExecuteException e) {
            System.err.println("Execution failed.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("permission denied.");
            e.printStackTrace();
        }

        return iExitValue;
    }

    private String checkAndAdjustTelephone(String telephoneNumber) {
        if(telephoneNumber.startsWith("55") && telephoneNumber.length() < 13) {
            telephoneNumber = "55"+telephoneNumber;
        }
        return telephoneNumber;
    }

    private void saveAlert(String telephoneNumber, String ticketServiceNow) {
        AlertDomain alert = new AlertDomain();

        alert.setTicket(ticketServiceNow);
        alert.setTelephony(telephoneNumber);
        alert.setCountCalls(1);
        alert.setCreateDate(LocalDateTime.now());

        repository.save(alert);

    }
}
