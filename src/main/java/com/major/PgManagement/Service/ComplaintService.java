package com.major.PgManagement.Service;

import java.util.List;

import com.major.PgManagement.Entities.Complaint;

public interface ComplaintService {
    List<Complaint> getAllComplaints();
    Complaint saveComplaint(Complaint complaint);
}

