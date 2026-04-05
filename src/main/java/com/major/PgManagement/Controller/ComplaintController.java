package com.major.PgManagement.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.major.PgManagement.Entities.Complaint;
import com.major.PgManagement.Repository.TenantRepository;
import com.major.PgManagement.Service.ComplaintService;

@Controller
@RequestMapping("/owner")
public class ComplaintController {

    private final ComplaintService service;

    @Autowired
    private TenantRepository tenantRepository;

    public ComplaintController(ComplaintService service) {
        this.service = service;
    }

    @GetMapping("/complaints")
    public String listComplaints(Model model) {
        model.addAttribute("complaints", service.getAllComplaints());
        return "owner/complaints";
    }

    @GetMapping("/complaints/new")
    public String createComplaintForm(Model model) {

        model.addAttribute("tenants", tenantRepository.findAll());
        model.addAttribute("complaint", new Complaint());
        return "owner/create_complaint";
    }

    @PostMapping("/complaints")
    public String saveComplaint(@ModelAttribute("complaint") Complaint complaint) {
        service.saveComplaint(complaint);
        return "redirect:/owner/complaints";
    }
}


