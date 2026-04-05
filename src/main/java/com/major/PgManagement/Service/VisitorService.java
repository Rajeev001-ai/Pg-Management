package com.major.PgManagement.Service;

import java.util.List;

import com.major.PgManagement.Entities.Visitor;

public interface VisitorService {
    List<Visitor> getAllVisitors();
    Visitor saveVisitor(Visitor visitor);
}

