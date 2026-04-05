package com.major.PgManagement.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.major.PgManagement.Entities.Room;
import com.major.PgManagement.Service.RoomService;

@Controller
@RequestMapping("/owner")
public class RoomController {

    @Autowired
    private RoomService service;

    @GetMapping("/rooms")
    public String listRooms(Model model) {
        model.addAttribute("rooms", service.getAllRooms());
        return "owner/rooms";
    }

    @GetMapping("/rooms/new")
    public String createRoomForm(Model model) {
        model.addAttribute("room", new Room());
        return "owner/create_room";
    }

    @PostMapping("/rooms")
    public String saveRoom(@ModelAttribute("room") Room room) {
        service.saveRoom(room);
        return "redirect:/rooms";
    }
}

