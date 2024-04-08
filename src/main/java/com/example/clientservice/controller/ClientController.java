package com.example.clientservice.controller;

import com.example.clientservice.dto.ClientRequest;
import com.example.clientservice.dto.ClientResponse;
import com.example.clientservice.dto.ContractRequest;
import com.example.clientservice.model.Client;
import com.example.clientservice.model.Contract;
import com.example.clientservice.model.ContractType;
import com.example.clientservice.repo.ClientRepo;
import com.example.clientservice.repo.ContractRepo;
import com.example.clientservice.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/client")
@CrossOrigin("http://localhost:4200/")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private ContractRepo contractRepo;



    @PostMapping("/create/{contractId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createClient (@RequestBody ClientRequest clientRequest,@PathVariable(name = "contractId") String contractId){
        clientService.createClient(clientRequest,contractId);
    }

    @PutMapping("/edit/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public void editClient(@RequestBody ClientRequest clientRequest,@PathVariable(name = "clientId")String clientId){
        clientService.editClient(clientRequest,clientId);
    }

    @GetMapping("/getAll")
    @ResponseStatus(HttpStatus.OK)
    public List<ClientResponse> getAllClients(){
        return clientService.getAllClients();
    }

    @GetMapping("/get/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Client> getClient(@PathVariable(name = "clientId")String clientId){
        return  clientService.getClientById(clientId);
    }
    @GetMapping("/getContract/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public Contract getClientContract(@PathVariable(name = "clientId")String clientId){
        return clientService.getClientContract(clientId);
    }

    @DeleteMapping("/delete/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteContract (@PathVariable(name = "clientId")String clientId){
        clientService.deleteClient(clientId);
    }

    @PutMapping("/addContract/{clientId}/{contractId}")
    @ResponseStatus(HttpStatus.OK)
    public void addContractToClient(@PathVariable(name = "clientId")String clientId,@PathVariable(name = "contractId")String contractId){
        clientService.addContractToClient(clientId,contractId);
    }

    @PutMapping("/ticketsAvailable/{clientId}")
    public ResponseEntity<?> updateClientTicketsAvailable(@PathVariable String clientId, @RequestBody Map<String, Integer> updateRequest) {
        int ticketsAv = updateRequest.getOrDefault("ticketsAvailable", 0);
        clientService.updateClientTicketsAvailable(clientId, ticketsAv);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/getByContractType/{contractType}")
    @ResponseStatus(HttpStatus.OK)
    public List<Client> getByContractType(@PathVariable(name = "contractType") ContractType contractType){
        return clientService.findByContractType(contractType);
    }

    @GetMapping("/getByTicketsAvailableAsc")
    @ResponseStatus(HttpStatus.OK)
    public List<Client> getByTicketsAvailableAsc(){
        return clientService.findByOrderByTicketsAvailableAsc();
    }

    @GetMapping("/getByTicketsAvailableDesc")
    @ResponseStatus(HttpStatus.OK)
    public List<Client> getByTicketsAvailableDesc(){
        return clientService.findByOrderByTicketsAvailableDesc();
    }






}

