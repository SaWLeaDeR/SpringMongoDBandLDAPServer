package com.finartz.security.services;


import com.finartz.security.services.repository.GuestRepository;
import com.finartz.security.services.ui.Guest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataMongoTest
public class TestMongo {

    @Autowired
    private GuestRepository guestRepository;


    @Test
    public void test_AllUser(){
        List<Guest> guestList =guestRepository.findAll();
        for(Guest guest:guestList){
            System.out.println("UserId: "+ guest.getId());
            System.out.println("Name: "+guest.getFirstName());
            System.out.println("Surname: "+ guest.getLastName());
            System.out.println("Email: "+ guest.getEmailAddress());
        }
    }
}