package com.major.PgManagement.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.major.PgManagement.Entities.Rent;
import com.major.PgManagement.Repository.RentRepository;
import com.major.PgManagement.Repository.TenantRepository;
import com.major.PgManagement.Service.ComplaintService;
import com.major.PgManagement.Service.RentService;
import com.major.PgManagement.Service.RoomService;
import com.major.PgManagement.Service.TenantService;

@Controller
@RequestMapping("/owner")
public class OwnerController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RentService rentService;

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private RentRepository rentRepo;

    @Autowired
    private TenantRepository tenantRepo;
   
    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        long vacantRooms = roomService.getAllRooms().stream()
                .filter(r -> "Vacant".equalsIgnoreCase(r.getStatus()))
                .count();

        long pendingRent = rentService.getAllRents().stream()
                .filter(r -> "Unpaid".equalsIgnoreCase((String) r.getStatus()))
                .count();

        long pendingComplaints = complaintService.getAllComplaints().stream()
                .filter(c -> "Pending".equalsIgnoreCase(c.getStatus()))
                .count();

        model.addAttribute("totalTenants", tenantService.getAllTenants().size());
        model.addAttribute("vacantRooms", vacantRooms);
        model.addAttribute("pendingRent", pendingRent);
        model.addAttribute("pendingComplaints", pendingComplaints);

        return "owner/owner-dashboard";
    }

    
    // ✅ Show Rent Page
    @GetMapping
    public String rentPage(Model model) {
        model.addAttribute("rents", rentRepo.findAll());
        model.addAttribute("tenants", tenantRepo.findAll());
        return "owner-rent";
    }

    // ✅ Add New Rent
    @PostMapping("/add")
    public String addRent(@RequestParam Long tenantId,
                          @RequestParam double amount,
                          @RequestParam String month) {

        Rent rent = new Rent(tenantId, amount, month, "PENDING");
        rentRepo.save(rent);
        return "redirect:/owner/rent";
    }

    // ✅ Mark Rent Paid
    @GetMapping("/paid/{id}")
    public String markPaid(@PathVariable Long id) {
        Rent rent = rentRepo.findById(id).get();
        rent.setStatus("PAID");
        rentRepo.save(rent);
        return "redirect:/owner/rent";
    }
}
