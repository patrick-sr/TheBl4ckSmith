package com.actuationZabbix.demo.repository;

import com.actuationZabbix.demo.domain.AlertDomain;
import org.springframework.data.repository.CrudRepository;

/**
 * Project: "demo", created at 30/01/2021 by Patrick Rosa.
 */
public interface AlertRepository extends CrudRepository<AlertDomain, String> {
}
