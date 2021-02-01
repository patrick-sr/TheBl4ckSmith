package com.actuationZabbix.demo.business;

import com.actuationZabbix.demo.repository.ZabbixRepository;
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
public class
DeactivateExpiredTriggerService {

    private final ZabbixRepository zabbixRepository;

    @Scheduled(cron="0 15 10 ? * *")
    public void deactivate() {
        zabbixRepository.deactivateExpiredTriggerOnZabbixAlert();
    }
}
