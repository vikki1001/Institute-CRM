package com.ksv.ktrccrm.db1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ksv.ktrccrm.db1.entities.TenantConfig;

public interface TenantConfigRepository extends JpaRepository<TenantConfig, Integer> {

}
