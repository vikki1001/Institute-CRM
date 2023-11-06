package com.ksv.ktrccrm.db1.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ksv.ktrccrm.db1.entities.KRADescription;

@Transactional
public interface KRADescriptionRepository extends JpaRepository<KRADescription, Long> {


}
