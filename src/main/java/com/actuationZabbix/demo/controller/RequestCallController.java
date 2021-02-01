package com.actuationZabbix.demo.controller;

import com.actuationZabbix.demo.business.CallShellScriptService;
import com.actuationZabbix.demo.repository.ZabbixRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Project: "demo", created at 30/01/2021 by Patrick Rosa.
 */

@RestController
@RequestMapping("actuationController/")
@RequiredArgsConstructor
public class RequestCallController {

        private final CallShellScriptService callShellScriptService;
        private final ZabbixRepository zabbixRepository;

        @PostMapping (value = "requestACallForActuation/{telephoneNumber}/{ticketServiceNow}")
        public HttpStatus requestACallForActuation(@PathVariable("telephoneNumber") String telephoneNumber, @PathVariable("ticketServiceNow") String ticketServiceNow) {
            return ResponseEntity.ok(callShellScriptService.runScript(telephoneNumber, ticketServiceNow)).getStatusCode();
        }

        @PostMapping(value = "zabbixRepositorio/ack")
        public ResponseEntity<Boolean> acknowledgeAlert() {
            return ResponseEntity.ok(zabbixRepository.acknowledgeZabbixAlert("INC0010045"));
        }

        @PostMapping(value = "zabbixRepositorio/deactivate")
        public ResponseEntity<Boolean> deactivateAlert() {
            return ResponseEntity.ok(zabbixRepository.deactivateExpiredTriggerOnZabbixAlert());
        }
}
