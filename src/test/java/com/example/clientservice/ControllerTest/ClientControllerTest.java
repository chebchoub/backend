package com.example.clientservice.ControllerTest;

import com.example.clientservice.controller.ClientController;
import com.example.clientservice.dto.ClientRequest;
import com.example.clientservice.dto.ClientResponse;
import com.example.clientservice.model.Contract;
import com.example.clientservice.repo.ClientRepo;
import com.example.clientservice.repo.ContractRepo;
import com.example.clientservice.services.ClientService;
import com.example.clientservice.services.ContractService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientRepo clientRepo;

    @MockBean
    private ContractRepo contractRepo;
    @MockBean
    private ClientService clientService;

    @MockBean
    private ContractService contractService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createClient_Success() throws Exception{
        Contract contract = new Contract();
        contract.setId("1");

        ObjectMapper objectMapper = new ObjectMapper();

        ClientRequest clientRequest = ClientRequest.builder()
                .entreprise("Test Entreprise")
                .email("testEntreprise@gmail.com")
                .password("testunit123-")
                .phoneNumber("12345678")
                .location("test")
                .build();

        String jsonRequest = objectMapper.writeValueAsString(clientRequest);

        mockMvc.perform(post("/api/v1/client/create/" + contract.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated());

        verify(clientService).createClient(clientRequest,contract.getId());
    }


    @Test
    public void editClient_Success() throws Exception {
        String existingClientId = "existingClientId";

        ClientRequest clientRequest = ClientRequest.builder()
                .entreprise("Test Entreprise")
                .email("testEntreprise@gmail.com")
                .password("testunit123-")
                .phoneNumber("12345678")
                .location("test")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(clientRequest);

        mockMvc.perform(put("/api/v1/client/edit/" + existingClientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        verify(clientService).editClient(clientRequest,existingClientId);
    }

    @Test
    public void getAllClients_Success() throws Exception {
        List<ClientResponse> clientResponses = List.of(new ClientResponse());

        when(clientService.getAllClients()).thenReturn(clientResponses);

        mockMvc.perform(get("/api/v1/client/getAll"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)));
    }

    @Test
    public void getClientContract_Success() throws Exception {
        String clientId = "1";
        Contract contract = new Contract();

        when(clientService.getClientContract(clientId)).thenReturn(contract);

        mockMvc.perform(get("/api/v1/client/getContract/" + clientId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void deleteClient_Success() throws Exception {
        String clientId = "1";

        doNothing().when(clientService).deleteClient(clientId);

        mockMvc.perform(delete("/api/v1/client/delete/" + clientId))
                .andExpect(status().isOk());

        verify(clientService).deleteClient(clientId);
    }

    @Test
    public void addContractToClient_Success() throws Exception {
        String clientId = "1";
        String contractId = "1";

        doNothing().when(clientService).addContractToClient(clientId, contractId);

        mockMvc.perform(put("/api/v1/client/addContract/" + clientId + "/" + contractId))
                .andExpect(status().isOk());

        verify(clientService).addContractToClient(clientId, contractId);
    }







}
