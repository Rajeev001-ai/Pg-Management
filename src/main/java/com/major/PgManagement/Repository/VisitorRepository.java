package com.major.PgManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.major.PgManagement.Entities.Visitor;


public interface VisitorRepository extends JpaRepository<Visitor, Long> {
}

