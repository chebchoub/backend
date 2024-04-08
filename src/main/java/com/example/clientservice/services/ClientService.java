package com.example.clientservice.services;

import com.example.clientservice.dto.ClientRequest;
import com.example.clientservice.dto.ClientResponse;
import com.example.clientservice.dto.ContractRequest;
import com.example.clientservice.model.Client;
import com.example.clientservice.model.Contract;
import com.example.clientservice.model.ContractType;
import com.example.clientservice.repo.ClientRepo;
import com.example.clientservice.repo.ContractRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    private ClientRepo clientRepo;
    @Autowired
    private ContractRepo contractRepo;

    @Autowired
    private ContractService contractService;


    public void createClient(ClientRequest clientRequest, String contractId) {
        Contract existingContract = contractRepo.getContractById(contractId);
        if (existingContract != null) {
            Client client = Client.builder()
                    .email(clientRequest.getEmail())
                    .password(clientRequest.getPassword())
                    .phoneNumber(clientRequest.getPhoneNumber())
                    .contract(existingContract)
                    .location(clientRequest.getLocation())
                    .ticketsAvailable(existingContract.getTickets())
                    .build();

            clientRepo.save(client);
        } else {
            throw new RuntimeException("Contract with ID " + contractId + " not found");
        }
    }
    public void editClient(ClientRequest clientRequest,String clientId){
        Optional<Client> existingClient = clientRepo.findById(clientId);
        if(existingClient.isPresent()){
            Client client = existingClient.get();

            client.setPhoneNumber(clientRequest.getPhoneNumber());
            client.setLocation(clientRequest.getLocation());
            client.setTicketsAvailable(clientRequest.getTicketsAvailable());

            clientRepo.save(client);
        }else{
            throw new RuntimeException("Client "+ clientId + " Not Found ");
        }
    }

    public List<ClientResponse> getAllClients() {
        List<Client> clients = clientRepo.findAll();
        return clients.stream().map(this::convertToClientResponse).toList();
    }


    public void addContractToClient(String clientId, String contractId) {
        Optional<Client> clientCheck = clientRepo.findById(clientId);
        if (!clientCheck.isPresent()) {
            throw new RuntimeException("Client with ID " + clientId + " not found");
        }
        Client existingClient = clientCheck.get();

        Contract existingContract = contractRepo.getContractById(contractId);

        if (existingContract == null) {
            throw new RuntimeException("Contract with ID " + contractId + " not found");
        }

        if (existingClient.getContract() == null){
            existingClient.setContract(existingContract);
            existingClient.setTicketsAvailable(existingContract.getTickets());
            clientRepo.save(existingClient);
        }else{
            throw  new RuntimeException("This client " + existingClient.getContract().getEntreprise() + " already have a contract, please remove it's contract first");
        }

    }

    public void deleteClient(String clientId) {
        if (clientRepo.existsById(clientId)) {
            this.clientRepo.deleteById(clientId);
        } else {
            throw new ContractService.NotFoundException("Contract not found with ID: " + clientId);
        }
    }

    public void updateClientTicketsAvailable(String clientId,int ticketsAv) {

        Optional<Client> clientOptional = clientRepo.findById(clientId);
        if (!clientOptional.isPresent()) {
            throw new RuntimeException("Client with ID " + clientId + " not found");
        }
        Client client = clientOptional.get();

        client.setTicketsAvailable(ticketsAv);

        clientRepo.save(client);
    }

    public Contract getClientContract(String clientId){
        Optional<Client> clientOptional = clientRepo.findById(clientId);
        if (!clientOptional.isPresent()) {
            throw new RuntimeException("Client with ID " + clientId + " not found");
        }
        Client client = clientOptional.get();
        return client.getContract();
    }

    private ClientResponse convertToClientResponse(Client client) {
        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setId(client.getId());
        clientResponse.setEmail(client.getEmail());
        clientResponse.setPassword(client.getPassword());
        clientResponse.setPhoneNumber(client.getPhoneNumber());
        clientResponse.setLocation(client.getLocation());
        clientResponse.setTicketsAvailable(client.getTicketsAvailable());
        clientResponse.setContract(client.getContract());
        return clientResponse;
    }

    public Optional<Client> getClientById(String clientId){
        return clientRepo.findClientById(clientId);
    }

    public List<Client> findByContractType(ContractType contractType) {
        List<Client> clients = clientRepo.findAll();
        return clients.stream()
                .filter(client -> client.getContract() != null && client.getContract().getContractType() == contractType)
                .collect(Collectors.toList());
    }


    public List<Client> findByOrderByTicketsAvailableAsc() {
        return clientRepo.findByOrderByTicketsAvailableAsc();
    }

    public List<Client> findByOrderByTicketsAvailableDesc() {
        return clientRepo.findByOrderByTicketsAvailableDesc();
    }
}