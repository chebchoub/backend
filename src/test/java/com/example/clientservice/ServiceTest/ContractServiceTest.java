package com.example.clientservice.ServiceTest;

import com.example.clientservice.dto.ContractRequest;
import com.example.clientservice.dto.ContractResponse;
import com.example.clientservice.model.Contract;
import com.example.clientservice.model.ContractType;
import com.example.clientservice.repo.ContractRepo;
import com.example.clientservice.services.ContractService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ContractServiceTest {

    @Mock
    private ContractRepo contractRepo;

    @InjectMocks
    private ContractService contractService;

    @Captor
    private ArgumentCaptor<Contract> contractCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createContract_standardType_setsPremiumTypeToNull() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2026,Calendar.MARCH,17);
        Date startDate = calendar1.getTime();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2028,Calendar.MARCH,17);
        Date endDate = calendar2.getTime();

        ContractRequest request = ContractRequest.builder()
                .contractType(ContractType.STANDARD)
                .startDate(startDate)
                .endDate(endDate)
                .updateDate(new Date())
                .maintenance(3)
                .build();

        contractService.createContract(request);

        verify(contractRepo).save(contractCaptor.capture());
        Contract savedContract = contractCaptor.getValue();

        assertNull(savedContract.getPremiumType(), "PremiumType should be null for STANDARD contracts");
    }

    @Test
    public void createContract_premiumTypeSilver_setsTicketsTo5() {

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2026,Calendar.MARCH,17);
        Date startDate = calendar1.getTime();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2028,Calendar.MARCH,17);
        Date endDate = calendar2.getTime();

        ContractRequest request = ContractRequest.builder()
                .contractType(ContractType.PREMIUM)
                .premiumType(ContractType.PremiumType.SILVER)
                .startDate(startDate)
                .endDate(endDate)
                .updateDate(new Date())
                .maintenance(3)
                .build();

        contractService.createContract(request);

        verify(contractRepo).save(contractCaptor.capture());
        Contract savedContract = contractCaptor.getValue();

        assertEquals(5, savedContract.getTickets());
    }

    @Test
    public void createContract_premiumTypeGold_setsTicketsTo10() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2026,Calendar.MARCH,17);
        Date startDate = calendar1.getTime();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2028,Calendar.MARCH,17);
        Date endDate = calendar2.getTime();

        ContractRequest request = ContractRequest.builder()
                .contractType(ContractType.PREMIUM)
                .premiumType(ContractType.PremiumType.GOLD)
                .startDate(startDate)
                .endDate(endDate)
                .maintenance(3)
                .build();

        contractService.createContract(request);

        verify(contractRepo).save(contractCaptor.capture());
        Contract savedContract = contractCaptor.getValue();

        assertEquals(10, savedContract.getTickets());
    }

    @Test
    public void createContract_WithInvalidStartDate() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2024,Calendar.MARCH,17);
        Date startDate = calendar1.getTime();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2028,Calendar.MARCH,17);
        Date endDate = calendar2.getTime();

        ContractRequest request = ContractRequest.builder()
                .contractType(ContractType.PREMIUM)
                .premiumType(ContractType.PremiumType.GOLD)
                .startDate(startDate)
                .endDate(endDate)
                .updateDate(new Date())
                .maintenance(3)
                .build();

        assertThrows(IllegalArgumentException.class,()->contractService.createContract(request));
    }

    @Test
    public void getAllContracts_returnsEmptyListWhenNoContractsAvailable() {
        when(contractRepo.findAll()).thenReturn(Collections.emptyList());

        List<ContractResponse> responses = contractService.getAllContracts();

        assertTrue(responses.isEmpty());
    }

    @Test
    public void getAllContracts_Success() {
        // Setup mock contracts
        Contract contract = Contract.builder()
                .id("1")
                .contractType(ContractType.PREMIUM)
                .premiumType(ContractType.PremiumType.GOLD)
                .startDate(new Date(2024-03-07))
                .endDate(new Date(2025-03-07))
                .updateDate(new Date())
                .maintenance(3)
                .build();


        when(contractRepo.findAll()).thenReturn(Arrays.asList(contract));

        List<ContractResponse> responses = contractService.getAllContracts();

        assertEquals(1, responses.size());
        assertEquals("1", responses.get(0).getId());
    }

    @Test
    public void editContract_Success() {

        String contractId = "existingId";
        Contract existingContract = new Contract();
        existingContract.setId(contractId);


        ContractRequest request = ContractRequest.builder()
                .contractType(ContractType.PREMIUM)
                .premiumType(ContractType.PremiumType.GOLD)
                .startDate(new Date(2024-03-07))
                .endDate(new Date(2025-03-07))
                .updateDate(new Date())
                .maintenance(3)
                .build();



        when(contractRepo.findById(contractId)).thenReturn(Optional.of(existingContract));

        contractService.editContract(request, contractId);

        verify(contractRepo).save(any(Contract.class));
    }

    @Test
    public void editContract_throwsRuntimeExceptionWhenContractNotFound() {
        String contractId = "nonExistingContractId";
        ContractRequest contractRequest = new ContractRequest();

        when(contractRepo.findById(contractId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            contractService.editContract(contractRequest, contractId);
        });
    }

    @Test
    public void deleteContract_Success() {
        String contractId = "existingId";

        when(contractRepo.existsById(contractId)).thenReturn(true);

        contractService.deleteContract(contractId);

        verify(contractRepo).deleteById(contractId);
    }

    @Test
    public void deleteContract_Failed() {
        String contractId = "nonExistingContractId";

        assertThrows(ContractService.NotFoundException.class, () -> {
            contractService.deleteContract(contractId);
        });

        verify(contractRepo, never()).deleteById(contractId);
    }


}
