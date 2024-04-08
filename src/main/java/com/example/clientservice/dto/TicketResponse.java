package com.example.clientservice.dto;

import com.example.clientservice.model.Category;
import com.example.clientservice.model.Priority;
import com.example.clientservice.model.Status;

import java.util.Date;

public class TicketResponse {
    private String _id;
    private String titre;
    private Status status;
    private Date openingDate;
    private Date closingDate;
    private Priority priority;
    private Category category;
    private String description;
    private String image;
    private int rating;
}
