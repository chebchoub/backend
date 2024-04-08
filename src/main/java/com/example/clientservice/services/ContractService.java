package com.example.clientservice.services;

import com.example.clientservice.dto.ContractRequest;
import com.example.clientservice.dto.ContractResponse;
import com.example.clientservice.model.Contract;
import com.example.clientservice.model.ContractType;
import com.example.clientservice.repo.ContractRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    @Autowired
    private ContractRepo contractRepo;

    public void createContract(ContractRequest contractRequest) {
        contractRequest.setTickets(0);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date today = cal.getTime();
        if (contractRequest.getStartDate().before(today)) {
            throw new IllegalArgumentException("StartDate must be today or later.");
        }
        if (contractRequest.getEndDate().before(contractRequest.getStartDate())) {
            throw new IllegalArgumentException("EndDate must be after StartDate.");
        }
        Contract contract = Contract.builder()
                .contractType(contractRequest.getContractType())
                .premiumType(contractRequest.getPremiumType())
                .entreprise(contractRequest.getEntreprise())
                .startDate(contractRequest.getStartDate())
                .endDate(contractRequest.getEndDate())
                .updateDate(new Date())
                .description(contractRequest.getDescription())
                .maintenance(contractRequest.getMaintenance())
                .build();
        switch (contractRequest.getContractType()) {
            case STANDARD:
                contract.setPremiumType(null);
                contract.setMaintenance(0);
                break;
            case PREMIUM:
                switch (contractRequest.getPremiumType()) {
                    case SILVER:
                        contract.setTickets(5);
                        break;
                    case GOLD:
                        contract.setTickets(10);
                        break;
                    case PLATINIUM:
                        contract.setTickets(50);
                        break;
                }
        }

        contractRepo.save(contract);
    }

    public List<ContractResponse> getAllContracts() {
        List<Contract> contracts = contractRepo.findAll();
        return contracts.stream().map(this::convertToContractResponse).toList();
    }

    private ContractResponse convertToContractResponse(Contract contract) {
        ContractResponse contractResponse = new ContractResponse();
        contractResponse.setId(contract.getId());
        contractResponse.setContractType(contract.getContractType());
        contractResponse.setPremiumType(contract.getPremiumType());
        contractResponse.setEntreprise(contract.getEntreprise());
        contractResponse.setStartDate(contract.getStartDate());
        contractResponse.setEndDate(contract.getEndDate());
        contractResponse.setUpdateDate(contract.getUpdateDate());
        contractResponse.setDescription(contractResponse.getDescription());
        contractResponse.setMaintenance(contract.getMaintenance());
        contractResponse.setTickets(contract.getTickets());
        return contractResponse;
    }

    public void editContract(ContractRequest contractRequest, String _id) {
        Optional<Contract> existingContract = contractRepo.findById(_id);
        if (existingContract.isPresent()) {
            Contract contract = existingContract.get();

            contract.setContractType(contractRequest.getContractType());
            contract.setPremiumType(contractRequest.getPremiumType());
            contract.setEntreprise(contractRequest.getEntreprise());
            contract.setStartDate(contractRequest.getStartDate());
            contract.setEndDate(contractRequest.getEndDate());
            contract.setUpdateDate(new Date());
            contract.setDescription(contractRequest.getDescription());
            switch (contractRequest.getContractType()) {
                case STANDARD:
                    contract.setPremiumType(null);
                    contract.setTickets(0);
                    contract.setMaintenance(0);
                    break;
                case PREMIUM:
                    contract.setTickets(contractRequest.getTickets());
                    contract.setMaintenance(contractRequest.getMaintenance());
            }
            contractRepo.save(contract);
        } else {
            throw new RuntimeException("Contract with ID " + _id + " not found");
        }
    }

    public void deleteContract(String contractId) {
        if (contractRepo.existsById(contractId)) {
            contractRepo.deleteById(contractId);
        } else {
            throw new NotFoundException("Contract not found with ID: " + contractId);
        }
    }

    // ---------------------------------Repository methods----------------------------------
    public Contract getContractById(String contractId) {
        return contractRepo.getContractById(contractId);
    }

    public List<Contract> findByContractType(ContractType contractType) {
        return contractRepo.findByContractType(contractType);
    }

    public List<Contract> findByPremiumType(ContractType.PremiumType premiumType) {
        return contractRepo.findByPremiumType(premiumType);
    }

    public List<Contract> findByOrderByTicketsAsc() {
        return contractRepo.findByOrderByTicketsAsc();
    }

    public List<Contract> findByOrderByTicketsDesc() {
        return contractRepo.findByOrderByTicketsDesc();
    }

    /*
    public List<Contract>findByOrderByStartDate(){
        return contractRepo.findByOrderByStartDate();
    }
    */
    public List<Contract> findByOrderByEndDateAsc() {
        return contractRepo.findByOrderByEndDateAsc();
    }

    public List<Contract> findByOrderByEndDateDesc() {
        return contractRepo.findByOrderByEndDateDesc();
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }
}