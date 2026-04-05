package com.major.PgManagement.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.major.PgManagement.Entities.Rent;
import com.major.PgManagement.Repository.TenantRepository;
import com.major.PgManagement.Service.RentServiceImpl;

@Controller
@RequestMapping("/owner")
public class RentController {

    private final RentServiceImpl service;

    public RentController(RentServiceImpl service) {
        this.service = service;
    }

    @Autowired
    private TenantRepository tenantRepository;

    @GetMapping("/rents")
    public String listRents(Model model) {
        model.addAttribute("rents", service.getAllRents());
        return "owner/rents";
    }

    @GetMapping("/rents/new")
    public String createRentForm(Model model) {

        model.addAttribute("tenants", tenantRepository.findAll());
        model.addAttribute("rent", new Rent());
        return "owner/create_rent";
    }

    @PostMapping("/rents")
    public String saveRent(@ModelAttribute("rent") Rent rent) {
        service.saveRent(rent);
        return "redirect:/owner/rents";
    }
}

