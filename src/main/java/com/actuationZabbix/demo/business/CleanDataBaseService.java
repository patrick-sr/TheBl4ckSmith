package com.actuationZabbix.demo.business;

import com.actuationZabbix.demo.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Project: "demo", created at 30/01/2021 by Patrick Rosa.
 */

@Service
@Component
@RequiredArgsConstructor
public class CleanDataBaseService {

    private AlertRepository alertRepository;

    @Scheduled(cron="0 0/1 0 ? * *")
    public void cleanDB() {
        try {
            alertRepository.findAll().forEach(alert -> {
                if(alert.getCreateDate().plusHours(1).isBefore(LocalDateTime.now())) {
                    alertRepository.deleteById(alert.getTicket());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
