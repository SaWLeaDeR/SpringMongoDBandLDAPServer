package com.finartz.security.services.repository;

import com.finartz.security.services.ui.Guest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends MongoRepository<Guest, String> {

    Guest findBy_id(String _id);

    Guest findByuserName(String userName);
}