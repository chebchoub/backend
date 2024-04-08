package com.example.clientservice.repo;

import com.example.clientservice.model.Contract;
import com.example.clientservice.model.ContractType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ContractRepo extends MongoRepository<Contract,String> {

    Contract getContractById(String contractId);
    List<Contract>findByContractType(ContractType contractType);
    List<Contract>findByPremiumType(ContractType.PremiumType premiumType);

    List<Contract>findByOrderByTicketsAsc();
    List<Contract>findByOrderByTicketsDesc();
    List<Contract>findByOrderByStartDate();
    List<Contract>findByOrderByEndDateAsc();
    List<Contract>findByOrderByEndDateDesc();
}
