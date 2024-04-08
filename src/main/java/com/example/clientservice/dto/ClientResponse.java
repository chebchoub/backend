package com.example.clientservice.dto;

import com.example.clientservice.model.Contract;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "client")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ClientResponse {

    private String id;
    private String email;
    private String password;
    private String phoneNumber;
    private String location;
    private int ticketsAvailable;
    private Contract contract;
}
