package com.major.PgManagement.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.major.PgManagement.Entities.Complaint;
import com.major.PgManagement.Entities.Rent;
import com.major.PgManagement.Entities.Tenant;
import com.major.PgManagement.Entities.User;
import com.major.PgManagement.Repository.ComplaintRepository;
import com.major.PgManagement.Repository.RentRepository;
import com.major.PgManagement.Repository.TenantRepository;
import com.major.PgManagement.Repository.UserRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/tenant")
public class TenantFeatureController {

    private final RentRepository rentRepo;
    private final ComplaintRepository complaintRepo;
    private final UserRepository userRepo;

    public TenantFeatureController(RentRepository rentRepo,
                                   ComplaintRepository complaintRepo,
                                   UserRepository userRepo) {
        this.rentRepo = rentRepo;
        this.complaintRepo = complaintRepo;
        this.userRepo = userRepo;
    }

    @Autowired
    private TenantRepository tenantRepo;

    @GetMapping("/dashboard")
    public String dashboard(Principal principal,Model model) {

        String email=principal.getName();

        Optional<User> newtenant=userRepo.findByUsername(email);

        User tenant=newtenant.get();
        
        model.addAttribute("tenant",tenant.getUsername());

        return "tenant/tenant-dashboard";
    }
    

    // ✅ View Rent
    @GetMapping("/rent")
    public String viewRent(Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName()).get();
        List<Rent> rents = rentRepo.findByTenantId(user.getTenantId());
        model.addAttribute("rents", rents);
        return "tenant/tenant-rent";
    }

    // ✅ View Complaint Page
    @GetMapping("/complaint")
    public String complaintPage(Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName()).get();
        List<Complaint> complaints = complaintRepo.findByTenantId(user.getTenantId());
        model.addAttribute("complaints", complaints);
        return "tenant/tenant-complaint";
    }

    // ✅ Submit Complaint
    @PostMapping("/complaint")
    public String saveComplaint(@RequestParam String message,
                                Principal principal) {

        User user = userRepo.findByUsername(principal.getName()).get();

        Complaint c = new Complaint(
                user.getTenantId(),
                message,
                "PENDING"
        );

        complaintRepo.save(c);
        return "redirect:/tenant/complaint";
    }

    @GetMapping("/profile")
    public String tenantProfile(Model model, Principal principal) {
    
    
    String email=principal.getName();

    Optional<User> user=userRepo.findByUsername(email);
  
    
    model.addAttribute("tenant", user.get());
     
    return "tenant/profile";
}

}

