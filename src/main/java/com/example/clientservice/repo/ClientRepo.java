package com.example.clientservice.repo;

import com.example.clientservice.model.Client;
import com.example.clientservice.model.Contract;
import com.example.clientservice.model.ContractType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClientRepo extends MongoRepository<Client,String> {
    Optional<Client> findClientById(String clientId);


    List<Client> findByOrderByTicketsAvailableAsc();

    List<Client> findByOrderByTicketsAvailableDesc();
}
