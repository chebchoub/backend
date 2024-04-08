package com.example.clientservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "technician")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Technician {
    @Id
    private String _id;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private String email;

    @NonNull
    private String password;
    @NonNull
    private Poste poste;

    private String ProfilePhoto;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date startDateWork;

    private int  ticketResolvedRating;
    private List<Ticket> ticketWaitingList;
    private boolean archiver;

}
