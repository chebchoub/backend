package com.example.clientservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "ticket")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Ticket {
    @Id
    private String _id;
    @NonNull
    private String titre;
    @NonNull
    private Status status;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date openingDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date closingDate;
    @NonNull
    private Priority priority;
    @NonNull
    private Category category;
    @NonNull
    private String description;
    private String image;
    private int rating;
}
