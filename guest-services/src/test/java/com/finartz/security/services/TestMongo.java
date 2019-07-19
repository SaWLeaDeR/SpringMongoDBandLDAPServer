package com.finartz.security.services;

import com.finartz.security.services.repository.GuestRepository;
import com.finartz.security.services.ui.Guest;
import com.finartz.security.services.ui.model.GuestModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RunWith(SpringRunner.class)
@DataMongoTest
public class TestMongo {

    @Autowired
    private GuestRepository guestRepository;
    @Autowired
    private MongoOperations mongoOps;


    @Test
    public void test_AllUser() {
        List<Guest> guestList = guestRepository.findAll();
        for (Guest guest : guestList) {
            System.out.println("UserId: " + guest.getId());
            System.out.println("Name: " + guest.getFirstName());
            System.out.println("Surname: " + guest.getLastName());
            System.out.println("Email: " + guest.getEmailAddress());
        }
    }

    @Test
    public void testDeleteUsingId() {
        assertThat(mongoOps.findById(3L, Guest.class), is(equalTo(null)));
    }

    @Test
    public void testFindOne() {
        Guest guest = mongoOps.findOne(query(where("firstName").is("fatih")), Guest.class);
        assertThat(guest.getFirstName(), is(equalTo("fatih")));
    }

    @Test
    public void testDeleteUsingObject() {
        Guest guest = mongoOps.findOne(query(where("firstName").is("fatih")), Guest.class);

        guestRepository.delete(guest);

        assertThat(mongoOps.findById(guest.getId(), Guest.class), is(equalTo(null)));
    }

    @Test
    public void testDeleteFindOne() {
        List<Guest> guestList = guestRepository.findAll();
        Guest guest = guestList.get(404);
        guestRepository.delete(guestRepository.findBy_id(guest.getId()));
    }

    @Test
    public void testSave() {
        GuestModel model = new GuestModel("Another", "asdfas", "email", "adress", "country", "state", "phoneNumber");
        Guest continentInserted = guestRepository.save(model.translateModelToGuest());
        Query query2 = new Query();
        query2.addCriteria(Criteria.where("firstName").is("Another"));
        assertThat(continentInserted.getFirstName(), is(equalTo("Another")));
        assertThat(mongoOps.findOne(query2, Guest.class).getFirstName(), is(equalTo("Another")));
    }
}