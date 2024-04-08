package com.example.clientservice.ServiceTest;

import com.example.clientservice.dto.ClientRequest;
import com.example.clientservice.dto.ClientResponse;
import com.example.clientservice.model.Client;
import com.example.clientservice.model.Contract;
import com.example.clientservice.model.ContractType;
import com.example.clientservice.repo.ClientRepo;
import com.example.clientservice.repo.ContractRepo;
import com.example.clientservice.services.ClientService;
import com.example.clientservice.services.ContractService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ClientServiceTest {

    @Mock
    private ContractRepo contractRepo;

    @InjectMocks
    private ContractService contractService;

    @Mock
    private ClientRepo clientRepo;

    @InjectMocks
    private ClientService clientService;

    @Captor
    private ArgumentCaptor<Client> clientArgumentCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createClient_Success(){
        Contract existingContract = new Contract();
        existingContract.setId("1");
        ClientRequest clientRequest = ClientRequest.builder()
                .entreprise("Test Entreprise")
                .email("testEntreprise@gmail.com")
                .password("testunit123-")
                .phoneNumber("12345678")
                .location("test")
                .build();

        when(contractRepo.getContractById(existingContract.getId())).thenReturn(existingContract);

        clientService.createClient(clientRequest,existingContract.getId());

        verify(clientRepo).save(clientArgumentCaptor.capture());
        Client saveClient = clientArgumentCaptor.getValue();

        assertEquals("Test Entreprise",saveClient.getEntreprise());
        assertEquals(existingContract,saveClient.getContract());
    }

    @Test
    public void createClient_FailedWithInvalidContractId(){
        Contract existingContract = new Contract();
        ClientRequest clientRequest = ClientRequest.builder()
                .entreprise("Test Entreprise")
                .email("testEntreprise@gmail.com")
                .password("testunit123-")
                .phoneNumber("12345678")
                .location("test")
                .build();

        when(contractRepo.getContractById(existingContract.getId())).thenReturn(null);

        assertThrows(RuntimeException.class,()->clientService.createClient(clientRequest,existingContract.getId()));
    }

    @Test
    public void createClient_FailedWithInvalidData() {
        Contract existingContract = new Contract();
        existingContract.setId("1");

        ClientRequest clientRequest = ClientRequest.builder()
                .entreprise(null)
                .email("testEntreprise@gmail.com")
                .password("testunit123-")
                .phoneNumber("12345678")
                .location("test")
                .build();

        when(contractRepo.getContractById(existingContract.getId())).thenReturn(existingContract);

        assertThrows(NullPointerException.class, () -> {
            clientService.createClient(clientRequest, existingContract.getId());
        });
    }

    @Test
    public void editClient_Success(){
        Client existingClient = new Client();
        existingClient.set_id("1");
        ClientRequest clientRequest = ClientRequest.builder()
                .entreprise("Test Entreprise")
                .phoneNumber("12345678")
                .location("test")
                .build();

        when(clientRepo.findById(existingClient.get_id())).thenReturn(Optional.of(existingClient));

        clientService.editClient(clientRequest,existingClient.get_id());

        assertEquals(existingClient.getEntreprise(),"Test Entreprise");

    }

    @Test
    public void editClient_FailedWithInvalidClientId(){
        Client existingClient = new Client();

        ClientRequest clientRequest = ClientRequest.builder()
                .entreprise("Test Entreprise")
                .phoneNumber("12345678")
                .location("test")
                .build();

        when(clientRepo.findById(existingClient.get_id())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,()->clientService.editClient(clientRequest,existingClient.get_id()));
    }

    @Test
    public void getAllClients_NoClientExists() {
        // Arrange
        when(clientRepo.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<ClientResponse> result = clientService.getAllClients();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void getAllClients_WithExistingClients() {

        Client client = Client.builder()
                .entreprise("Test Entreprise")
                .email("testEntreprise@gmail.com")
                .password("testunit123-")
                .phoneNumber("12345678")
                .location("test")
                .contract(new Contract())
                .build();
        List<Client> clients = List.of(client);
        when(clientRepo.findAll()).thenReturn(clients);


        List<ClientResponse> result = clientService.getAllClients();

        assertEquals(clients.size(), result.size());
    }

    @Test
    public void addContractToClient_FailedClientDoesNotExist() {
        String clientId = "1";
        String contractId = "1";

        when(clientRepo.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                clientService.addContractToClient(clientId, contractId));
    }

    @Test
    public void addContractToClient_FailedContractDoesNotExist() {
        String clientId = "1";
        String contractId = "1";

        when(clientRepo.findById(clientId)).thenReturn(Optional.of(new Client()));
        when(contractRepo.getContractById(contractId)).thenReturn(null);

        assertThrows(RuntimeException.class, () ->
                clientService.addContractToClient(clientId, contractId));
    }

    @Test
    public void addContractToClient_FailedClientAlreadyHasContract() {
        String clientId = "1";
        String contractId = "1";

        Client client = new Client();
        client.setContract(new Contract());

        when(clientRepo.findById(clientId)).thenReturn(Optional.of(client));
        when(contractRepo.getContractById(contractId)).thenReturn(new Contract());

        assertThrows(RuntimeException.class, () ->
                clientService.addContractToClient(clientId, contractId));
    }

    @Test
    public void addContractToClient_Success() {
        String clientId = "1";
        Contract contract = Contract.builder()
                .id("1")
                .contractType(ContractType.PREMIUM)
                .premiumType(ContractType.PremiumType.GOLD)
                .startDate(new Date(2024-03-07))
                .endDate(new Date(2025-03-07))
                .updateDate(new Date())
                .maintenance(3)
                .build();

        Client client = new Client();
        client.setEntreprise("Test Enterprise");

        when(clientRepo.findById(clientId)).thenReturn(Optional.of(client));
        when(contractRepo.getContractById(contract.getId())).thenReturn(contract);

        clientService.addContractToClient(clientId, contract.getId());

        verify(clientRepo).save(client);
        assertEquals(client.getContract(),contract);

    }

    @Test
    public void deleteClient_Success() {
        String clientId = "1";

        when(clientRepo.existsById(clientId)).thenReturn(true);

        clientService.deleteClient(clientId);

        verify(clientRepo).deleteById(clientId);
    }

    @Test
    public void deleteClient_FailedClientDoesNotExist() {
        String clientId = "1";


        when(clientRepo.existsById(clientId)).thenReturn(false);

        assertThrows(ContractService.NotFoundException.class, () -> {
            clientService.deleteClient(clientId);
        });

        verify(clientRepo, never()).deleteById(clientId);
    }

    @Test
    public void updateClientTicketsAvailable_FailedClientDoesNotExist() {
        String nonExistingClientId = "1";
        int ticketsAv = 10;

        when(clientRepo.findById(nonExistingClientId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> clientService.updateClientTicketsAvailable(nonExistingClientId, ticketsAv));

    }

    @Test
    public void updateClientTicketsAvailable_Success() {
        String existingClientId = "1";
        int ticketsAv = 10;
        Client existingClient = new Client();
        existingClient.setTicketsAvailable(5);

        when(clientRepo.findById(existingClientId)).thenReturn(Optional.of(existingClient));

        clientService.updateClientTicketsAvailable(existingClientId, ticketsAv);

        assertEquals(ticketsAv, existingClient.getTicketsAvailable());
        verify(clientRepo).save(existingClient);
    }

    @Test
    public void getClientContract_FailedClientNotFound() {
        String clientId = "1";

        when(clientRepo.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                clientService.getClientContract(clientId));

    }

    @Test
    public void getClientContract_Success() {
        String clientId = "1";
        Client client = new Client();
        Contract contract = new Contract();
        client.setContract(contract);

        when(clientRepo.findById(clientId)).thenReturn(Optional.of(client));

        Contract result = clientService.getClientContract(clientId);

        assertNotNull(result);
        assertEquals(contract, result);
    }







}
