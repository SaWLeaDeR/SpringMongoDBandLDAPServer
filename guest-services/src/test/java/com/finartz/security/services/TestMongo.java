package com.finartz.security.services;

import com.finartz.security.services.repository.GuestRepository;
import com.finartz.security.services.ui.Guest;
import com.finartz.security.services.ui.model.GuestModel;
import org.junit.After;
import org.junit.Before;
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

    @Before
    public void beforeInit() {
        guestRepository.save(new GuestModel("China",
                "China", "Last", "email@email.com",
                "address Test", "Turkey", "NW",
                "5448964123", "asdf")
                .translateModelToGuest());
    }

    @After
    public void afterInit() {
        guestRepository.delete(guestRepository.findByuserName("China"));

    }

    @Test
    public void test_GetAllUser() {
        List<Guest> guestList = guestRepository.findAll();
        for (Guest guest : guestList) {
            System.out.println("UserId: " + guest.getId());
            System.out.println("Name: " + guest.getFirstName());
            System.out.println("Surname: " + guest.getLastName());
            System.out.println("Email: " + guest.getEmailAddress());
        }
    }

    @Test
    public void testFindOne() {
        Guest guest = mongoOps.findOne(query(where("firstName").is("China")), Guest.class);
        assertThat(guest.getFirstName(), is(equalTo("China")));
    }

    @Test
    public void testDeleteUsingObject() {
        Guest guest = mongoOps.findOne(query(where("userName").is("China")), Guest.class);
        guestRepository.delete(guestRepository.findBy_id(guest.getId()));
        assertThat(mongoOps.findById(guest.getId(), Guest.class), is(equalTo(null)));
    }

    @Test
    public void testAddGuest() {
        GuestModel model = new GuestModel("Another", "Another", "asdfas", "email", "address", "country", "state", "phoneNumber", "dasda");
        Guest continentInserted = guestRepository.save(model.translateModelToGuest());
        Query query2 = new Query();
        query2.addCriteria(Criteria.where("firstName").is("Another"));
        assertThat(continentInserted.getFirstName(), is(equalTo("Another")));
        assertThat(mongoOps.findOne(query2, Guest.class).getFirstName(), is(equalTo("Another")));
        guestRepository.delete(guestRepository.findBy_id(continentInserted.getId()));
    }

    @Test
    public void testUpdateGuest() {
        GuestModel model = new GuestModel("Another", "Another", "asdfas", "email", "address", "country", "state", "phoneNumber", "asdasdas");
        Guest guestInserted = guestRepository.save(model.translateModelToGuest());
        Query query = new Query();
        query.addCriteria(Criteria.where("firstName").is("Another"));
        assertThat(guestInserted.getFirstName(), is(equalTo("Another")));

        model.setFirstName("UpdateTest");
        Guest guestUpdated = guestRepository.save(model.translateModelToGuest());
        Query query2 = new Query();
        query2.addCriteria(Criteria.where("firstName").is("UpdateTest"));
        assertThat(guestUpdated.getFirstName(), is(equalTo("UpdateTest")));
        assertThat(mongoOps.findOne(query2, Guest.class).getFirstName(), is(equalTo("UpdateTest")));
        guestRepository.delete(guestRepository.findBy_id(guestUpdated.getId()));
    }
}