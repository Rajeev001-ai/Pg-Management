package com.major.PgManagement.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.major.PgManagement.Entities.Visitor;
import com.major.PgManagement.Repository.TenantRepository;
import com.major.PgManagement.Service.VisitorService;

@Controller
@RequestMapping("/owner")
public class VisitorController {

    private final VisitorService service;

    @Autowired
    private TenantRepository tenantRepository;

    public VisitorController(VisitorService service) {
        this.service = service;
    }

    @GetMapping("/visitors")
    public String listVisitors(Model model) {
        model.addAttribute("visitors", service.getAllVisitors());
        return "owner/visitors";
    }

    @GetMapping("/visitors/new")
    public String createVisitorForm(Model model) {
        model.addAttribute("tenants", tenantRepository.findAll());
        model.addAttribute("visitor", new Visitor());
        return "owner/create_visitor";
    }

    @PostMapping("/visitors")
    public String saveVisitor(@ModelAttribute("visitor") Visitor visitor) {
        service.saveVisitor(visitor);
        return "redirect:/owner/visitors";
    }
}

