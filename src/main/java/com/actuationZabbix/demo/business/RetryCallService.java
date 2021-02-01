package com.actuationZabbix.demo.business;

import com.actuationZabbix.demo.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Project: "demo", created at 30/01/2021 by Patrick Rosa.
 */

@Service
@Component
@RequiredArgsConstructor
public class RetryCallService {

    private final AlertRepository repository;
    private final CallShellScriptService callService;

    @Scheduled(cron="0 0/1 0 ? * *")
    public void retryCall() {
        try {
            repository.findAll().forEach(alert -> {
               if(alert.getTicket() != null) {
                   if (alert.getAcknowledge() <= 0 && alert.getCountCalls() < 3) {
                       callService.runScript(alert.getTelephony(), alert.getTicket());
                       alert.setCountCalls(alert.getCountCalls()+1);
                       repository.save(alert);
                   }
               }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
