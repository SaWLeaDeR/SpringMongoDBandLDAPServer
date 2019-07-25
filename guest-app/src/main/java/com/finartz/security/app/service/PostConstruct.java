package com.finartz.security.app.service;

import com.finartz.security.app.GuestAppApplication;
import com.finartz.security.app.domain.AdminUser;
import com.finartz.security.app.repository.GroupRepository;
import com.finartz.security.app.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostConstruct {

    private static Logger log = LoggerFactory.getLogger(GuestAppApplication.class);

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private GroupRepository groupRepository;


    @javax.annotation.PostConstruct
    public void setup() {
        log.info("Spring LDAP CRUD Operations Binding and Unbinding Example");

        log.info("- - - - - - Managing persons");

        List<AdminUser> persons = personRepository.findAll();
        log.info("persons: " + persons);
        System.out.println(persons.get(1).getFirstName());

//        AdminUser john = personRepository.findOne("john");
//        john.setLastName("customlastname");
//        personRepository.updateLastName(john);
//        System.out.println(john.getLastName());
//
//        AdminUser jahn = personRepository.findOne("jahn");
//        jahn.setLastName("custom last name");
//        personRepository.update(jahn);

//        List<String> list = new ArrayList<>();
//        list.add("paly computer games");
//        ObjectId id = new ObjectId("5d2dd5d4589afc32a4ad75f0");
//        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
//        AdminUser person = new AdminUser("ahmet", "ahmet", "person", "5555", 5, "female", "das", "sdfsdfsd", list, "BSc", "{SHA}W6ph5Mm5Pz8GgiULbPgzG37mj9g=");
////        AdminModel adminmodel = new AdminModel("ahmet", "person", "5555", 5, "female", "das", "sdfsdfsd", list, "BSc", "{SHA}W6ph5Mm5Pz8GgiULbPgzG37mj9g=");
//        personRepository.create(person);
//        groupRepository.addMemberToGroup("user", person);


//        AdminUser jahn = personRepository.findOne("ahmet");
//        jahn.setLastName("custom last name");
//        personRepository.update(jahn);
//
//        AdminUser jihn = personRepository.findOne("jihn");
//        personRepository.delete(jihn);
//
//        persons = personRepository.findAll();
//        log.info("persons: " + persons);
//
//        log.info("- - - - - - Managing groups");
//
//        List<Group> groups = groupRepository.findAll();
//        log.info("groups: " + groups);
//
//        groupRepository.removeMemberFromGroup("developers", jihn);
//
//        groupRepository.addMemberToGroup("managers", jihn);
//
//        groups = groupRepository.findAll();
//        log.info("groups: " + groups);
//
//        System.exit(-1);
    }
}
