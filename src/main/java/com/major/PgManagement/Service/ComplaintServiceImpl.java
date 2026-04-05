package com.major.PgManagement.Service;

import org.springframework.stereotype.Service;

import com.major.PgManagement.Entities.Complaint;
import com.major.PgManagement.Repository.ComplaintRepository;

import java.util.List;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository repo;

    public ComplaintServiceImpl(ComplaintRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Complaint> getAllComplaints() {
        return repo.findAll();
    }

    @Override
    public Complaint saveComplaint(Complaint complaint) {
        return repo.save(complaint);
    }
}

