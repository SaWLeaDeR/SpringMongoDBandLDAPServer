package com.finartz.security.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finartz.security.app.crypto.PasswordEncode;
import com.finartz.security.app.domain.AdminUser;
import com.finartz.security.app.repository.GroupRepository;
import com.finartz.security.app.repository.PersonRepository;
import com.finartz.security.app.service.AdminUserServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import javax.naming.ldap.LdapName;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Tests {

    private static final String ADMINS = "/admins";
    private static final String SLASH = "/";

    @Value("${landon.guest.service.url}")
    private String adminServiceUrl;
    @Mock
    private RestTemplate restTemplate;
    @Autowired
    private AdminUserServiceImpl empService = new AdminUserServiceImpl(restTemplate);

    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private GroupRepository groupRepository;

    @Before
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    private PasswordEncode passwordEncode;
    @Autowired
    private LdapTemplate ldapTemplate;
    private LdapName baseLdapPath;

    @Test
    public void encryptPasswordTest(){
        String a =passwordEncode.encryptPassword("password");
        System.out.println("PASSWORD "+a+" PASSWORD");
    }

    @Test
    public void encriptor(){
        System.out.println(DigestUtils.sha1Hex("password"));
        String hashedVal = Base64.getEncoder().encodeToString(DigestUtils.sha1("password".getBytes(Charset.forName("UTF-8"))));
        System.out.println("hashed val   "+ hashedVal);
    }

    @Test
    public void testGetAllAdminUser(){
        String url = adminServiceUrl + ADMINS;
        HttpEntity<String> request = new HttpEntity<>(null, null);
        System.out.println(this.restTemplate.exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<List<AdminUser>>() {
        }).getBody());
    }

    @Test
    public void testGoupRepositoryAddMemberToGroup(){
        AdminUser adminUser = new AdminUser("123","123","fatih",
                "koyuncu","4567890",23,"male",
                "Ist","Student",
                Stream.of("Reading book","ride Biycle").collect(toList()),
                "BSc","1234");
        groupRepository.addMemberToGroup("admin",adminUser);
    }


//    private static final String GUESTS = "/guests";
//    private static final String SLASH = "/";
//    @Value("${landon.guest.service.url}")
//    private String guestServiceUrl;
//    @Autowired
//    private RestTemplate restTemplate;
//    @Autowired
//    private MongoOperations mongoOps;

//    @Test
//    public void testGetAllGuest() {
//        String url = guestServiceUrl + GUESTS;
//        HttpEntity<String> request = new HttpEntity<>(null, null);
//        List<Guest> guests = this.restTemplate.exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<List<Guest>>() {
//        }).getBody();
//    }
//
//    @Test
//    public void testAddNewGuest() {
//        GuestModel guestModel = new GuestModel();
//        String url = guestServiceUrl + GUESTS;
//        HttpEntity<GuestModel> request = new HttpEntity<>(guestModel, null);
//        Guest guest = this.restTemplate.exchange(url, HttpMethod.POST, request, Guest.class).getBody();
//        System.out.println(guest + " new guest");
//    }

//    @Test
//    public void testGetGuest() {
//        Guest guest = mongoOps.findOne(query(where("firstName").is("Samuel")), Guest.class);
//        System.out.println("inside TestGetGuest method ");
//        System.out.println(guest.getId() + " asdff  " + guest.getFirstName());
//        String url = guestServiceUrl + GUESTS + SLASH + guest.getId();
//        HttpEntity<String> request = new HttpEntity<>(null, null);
//        System.out.println(this.restTemplate.exchange(url, HttpMethod.GET, request, Guest.class).getBody());
//    }

//    @Test
//    public void testGetAllPersons() {
//        List<Guest> persons = GuestRepository.getAllPersons();
//        assertNotNull(persons);
//        assertEquals(persons.size(), 3);
//    }
//
//    @Test
//    public void testGetAllPersonsNames() {
//        List<String> persons = GuestRepository.getAllPersonNames();
//        assertNotNull(persons);
//        assertEquals(persons.size(), 3);
//    }
//
//    @Test
//    public void testFindPerson() {
//        Guest person = GuestRepository.findPerson("uid=john,ou=people,dc=memorynotfound,dc=com");
//        assertNotNull(person);
//        assertEquals(person.getFullName(), "John Doe");
//    }
}

