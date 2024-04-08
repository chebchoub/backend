package com.example.clientservice.model;

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
public class Client {

    @Id
    private String id;
    @NonNull
    private String email;
    private String password;
    private String phoneNumber;
    private String location;
    private int ticketsAvailable;
    @DBRef
    private Contract contract;

    public ContractType getContractType() {
        if (contract != null) {
            return contract.getContractType();
        }
        return null;
    }

}
