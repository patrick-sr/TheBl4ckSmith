package com.actuationZabbix.demo.business;

import com.actuationZabbix.demo.domain.AlertDomain;
import com.actuationZabbix.demo.repository.AlertRepository;
import com.actuationZabbix.demo.repository.ZabbixRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Optional;

/**
 * Project: "demo", created at 30/01/2021 by Patrick Rosa.
 */

@Service
@Component
@RequiredArgsConstructor
public class AcknowledgeService {

    private AlertRepository alertRepository;
    private ZabbixRepository zabbixRepository;

    @Scheduled(cron="0 0/1 0 ? * *")
    public void getAcknowledge() {
        File file = new File("/Users/u002422/Documents/testHacka/");
        File files[] = file.listFiles();
        if (files.length > 0) {
            for (int i = 0; i < Arrays.stream(files).count(); i++) {

                AlertDomain alert = new AlertDomain();
                Optional<AlertDomain> alertDomainOptional = alertRepository.findById(files[i].getName());

                if (alertDomainOptional.isPresent())
                    alert = alertDomainOptional.get();

                try {
                    String resultFile = new String(Files.readAllBytes(files[i].toPath()));
                    if (!resultFile.isEmpty()) {
                        if(Integer.parseInt(resultFile) == 1) {
                            zabbixRepository.acknowledgeZabbixAlert(alert.getTicket());

                            if(files[i].delete()) {
                                alertRepository.deleteById(alert.getTicket());
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
