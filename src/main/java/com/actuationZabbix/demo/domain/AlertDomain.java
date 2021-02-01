package com.actuationZabbix.demo.domain;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * Project: "demo", created at 30/01/2021 by Patrick Rosa.
 */

@Entity
@Data
public class AlertDomain {

    @Id
    private String ticket;
    private String telephony;
    private int Acknowledge;
    private int countCalls;
    private LocalDateTime createDate;
}
