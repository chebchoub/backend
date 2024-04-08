package com.example.clientservice.controller;

import com.example.clientservice.dto.*;
import com.example.clientservice.model.*;
import com.example.clientservice.services.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("api/v1/technician")
public class TechnicianController {
    @Autowired
    TechnicianService technicianService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTechnician (@RequestBody TechnicianRequest technicianRequest){
        technicianService.createTechnician(technicianRequest);
    }
    @GetMapping("/getAll")
    @ResponseStatus(HttpStatus.OK)
    public List<TechnicianResponse> getAllTechnician(){
        return technicianService.getAllTechnician();
    }

    @PutMapping("/edit/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void editTechnician (@RequestBody TechnicianRequest technicianRequest, @PathVariable(name = "id")String _id){
        technicianService.editTechnician(technicianRequest,_id);
    }
    @PostMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTechnician (@PathVariable(name = "id")String _id){
        technicianService.deleteTechnician(_id);
    }
    @PutMapping("/addTicketToTechnicien/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void addTicketToTechnicien (@RequestBody Ticket ticket, @PathVariable(name = "id")String _id){
        technicianService.addTicketToTechnicien(ticket,_id);
    }

    @GetMapping("get/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Technician> getTechnicianById(@PathVariable(name = "id")String id){

        return technicianService.getTechnicianById(id);
    }
    @GetMapping("/getByNonArchiver")
    @ResponseStatus(HttpStatus.OK)
    public List<Technician> getByNonArchiver(){
        return technicianService.findByArchiver();
    }
    @GetMapping("/getByTicketResolvedRatingAsc")
    @ResponseStatus(HttpStatus.OK)
    public List<Technician> getByTicketResolvedRatingAsc(){
        return technicianService.findByOrderByTicketResolvedRatingAsc();
    }
    @GetMapping("/getByTicketResolvedRatingDesc")
    @ResponseStatus(HttpStatus.OK)
    public List<Technician> getByTicketResolvedRatingDesc(){
        return technicianService.findByOrderByTicketResolvedRatingDesc();
    }
    @GetMapping("/getByStartDateWorkAsc")
    @ResponseStatus(HttpStatus.OK)
    public List<Technician> getByStartDateWorkAsc(){
        return technicianService.findByOrderByStartDateWorkAsc();
    }

    @GetMapping("/getByStartDateWorkDesc")
    @ResponseStatus(HttpStatus.OK)
    public List<Technician> getByStartDateWorkDesc(){
        return technicianService.findByOrderByStartDateWorkDesc();
    }


}
