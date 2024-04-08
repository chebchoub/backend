package com.example.clientservice.repo;

import com.example.clientservice.model.Technician;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TechnicianRepo extends MongoRepository<Technician,String>  {

    List<Technician> findByOrderByTicketResolvedRatingAsc();

    List<Technician>findByOrderByTicketResolvedRatingDesc();

    List<Technician> findByOrderByStartDateWorkAsc();

    List<Technician> findByOrderByStartDateWorkDesc();

    List<Technician> findByArchiver(boolean b);
}
