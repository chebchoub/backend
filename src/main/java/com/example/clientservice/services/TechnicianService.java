package com.example.clientservice.services;

import com.example.clientservice.dto.TechnicianRequest;
import com.example.clientservice.dto.TechnicianResponse;
import com.example.clientservice.model.Technician;
import com.example.clientservice.model.Ticket;
import com.example.clientservice.repo.TechnicianRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TechnicianService {
    @Autowired
    TechnicianRepo technicianRepo;
    public void createTechnician(TechnicianRequest technicianRequest) {
         List<Ticket>  ticketWaitingListVide = new ArrayList<>();
        Technician technician = Technician.builder()
                .firstName(technicianRequest.getFirstName())
                .lastName(technicianRequest.getLastName())
                .email(technicianRequest.getEmail())
                .password(technicianRequest.getPassword())
                .poste(technicianRequest.getPoste())
                .startDateWork(technicianRequest.getStartDateWork())
                .ticketResolvedRating(0)
                .ticketWaitingList( ticketWaitingListVide)
                .ProfilePhoto(technicianRequest.getProfilePhoto())
                .archiver(false)
                .build();
        technicianRepo.save(technician);
    }

    public List<TechnicianResponse> getAllTechnician() {
        List<Technician> technicians = technicianRepo.findAll();
        return technicians.stream().map(this::convertToTechnicianResponse).toList();
    }
    private TechnicianResponse convertToTechnicianResponse(Technician technician) {
        return TechnicianResponse.builder()
                ._id(technician.get_id())
                .firstName(technician.getFirstName())
                .lastName(technician.getLastName())
                .email(technician.getEmail())
                .password(technician.getPassword())
                .poste(technician.getPoste())
                .startDateWork(technician.getStartDateWork())
                .ticketResolvedRating(technician.getTicketResolvedRating())
                .ticketWaitingList(technician.getTicketWaitingList())
                .build();
    }

    public void editTechnician(TechnicianRequest technicianRequest, String id) {
        // Recherche du technicien existant par son ID
        Optional<Technician> existingTechnician = technicianRepo.findById(id);

        // Vérification si le technicien existe
        if (existingTechnician.isPresent()) {
            // Récupération du technicien existant
            Technician technician = existingTechnician.get();

            // Mise à jour des champs avec les nouvelles valeurs
            technician.setFirstName(technicianRequest.getFirstName());
            technician.setLastName(technicianRequest.getLastName());
            technician.setEmail(technicianRequest.getEmail());
            technician.setPassword(technicianRequest.getPassword());
            technician.setPoste(technicianRequest.getPoste());
            technician.setStartDateWork(technicianRequest.getStartDateWork());
            technician.setTicketResolvedRating((technicianRequest.getTicketResolvedRating()));
            technician.setTicketWaitingList((technicianRequest.getTicketWaitingList()));

            // Enregistrement du technicien mis à jour dans la base de données
            technicianRepo.save(technician);
        } else {
            // Si aucun technicien correspondant à l'ID n'est trouvé, vous pouvez lancer une exception ou gérer la situation en conséquence
            throw new IllegalArgumentException("Technician not found with id: " + id);
        }
    }

    public void deleteTechnician(String id) {
        Optional<Technician> existingTechnician = technicianRepo.findById(id);

        // Vérification si le technicien existe
        if (existingTechnician.isPresent()) {
            // Récupération du technicien existant
            Technician technician = existingTechnician.get();

            // Mise à jour des champs avec les nouvelles valeurs
           technician.setArchiver(true);
            technicianRepo.save(technician);

        } else {
            // Si aucun technicien correspondant à l'ID n'est trouvé, vous pouvez lancer une exception ou gérer la situation en conséquence
            throw new IllegalArgumentException("Technician not found with id: " + id);
        }
        }

    public Optional<Technician> getTechnicianById(String id) {
        return this.technicianRepo.findById(id);
    }


    public List<Technician> findByOrderByTicketResolvedRatingAsc() {
        return technicianRepo.findByOrderByTicketResolvedRatingAsc();
    }

    public List<Technician> findByOrderByTicketResolvedRatingDesc() {
        return technicianRepo.findByOrderByTicketResolvedRatingDesc();

    }

    public List<Technician> findByOrderByStartDateWorkAsc() {
        return technicianRepo.findByOrderByStartDateWorkAsc();
    }

    public List<Technician> findByOrderByStartDateWorkDesc() {
        return technicianRepo.findByOrderByStartDateWorkDesc();

    }

    public List<Technician> findByArchiver() {
        return this.technicianRepo.findByArchiver(false);
    }

    public void addTicketToTechnicien(Ticket ticket, String id) {
        // Recherche du technicien existant par son ID
        Optional<Technician> existingTechnician = technicianRepo.findById(id);

        // Vérification si le technicien existe
        if (existingTechnician.isPresent()) {
            // Récupération du technicien existant
            Technician technician = existingTechnician.get();

           List<Ticket>ticketList =technician.getTicketWaitingList();
           ticketList.add(ticket);
            technician.setTicketWaitingList(ticketList);

            technicianRepo.save(technician);
        } else {
            // Si aucun technicien correspondant à l'ID n'est trouvé, vous pouvez lancer une exception ou gérer la situation en conséquence
            throw new IllegalArgumentException("Technician not found with id: " + id);
        }
    }
}
