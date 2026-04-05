package com.major.PgManagement.Service;

import org.springframework.stereotype.Service;

import com.major.PgManagement.Entities.Visitor;
import com.major.PgManagement.Repository.VisitorRepository;

import java.util.List;

@Service
public class VisitorServiceImpl implements VisitorService {

    private final VisitorRepository repo;

    public VisitorServiceImpl(VisitorRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Visitor> getAllVisitors() {
        return repo.findAll();
    }

    @Override
    public Visitor saveVisitor(Visitor visitor) {
        return repo.save(visitor);
    }
}

