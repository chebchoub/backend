package com.example.clientservice.dto;

import com.example.clientservice.model.Grade;
import com.example.clientservice.model.Poste;
import com.example.clientservice.model.Ticket;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TechnicianRequest {
    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Poste poste;
    private String ProfilePhoto;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date startDateWork;
    private int  ticketResolvedRating;
    private List<Ticket> ticketWaitingList;

}
