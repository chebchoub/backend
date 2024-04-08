package com.example.clientservice.dto;

import com.example.clientservice.model.ContractType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContractResponse {

    @Id
    private String id;
    @NonNull
    private ContractType contractType;
    private ContractType.PremiumType premiumType;
    private String entreprise;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date startDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date endDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date updateDate;
    private String description;
    private int maintenance;
    private int tickets;
}
