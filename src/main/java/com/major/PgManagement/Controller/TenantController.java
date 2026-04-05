package com.major.PgManagement.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.major.PgManagement.Entities.Tenant;
import com.major.PgManagement.Service.TenantService;

@Controller
@RequestMapping("/owner")
public class TenantController {

    @Autowired
    private TenantService service;

    @GetMapping("/tenants")
    public String getTenants(Model model) {

        model.addAttribute("tenants", service.getAllTenants());
        return "owner/tenants";
    }

    @GetMapping("/tenants/new")
    public String createRoomForm(Model model) {
        model.addAttribute("tenant", new Tenant());
        return "owner/create_tenant";
    }

    @PostMapping("/tenants")
    public String saveRoom(@ModelAttribute("tenant") Tenant tenant) {
        service.saveTenant(tenant);
        return "redirect:/owner/tenants";
    }
}
