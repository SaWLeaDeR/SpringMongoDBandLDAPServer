package com.finartz.security.app;

import com.finartz.security.app.crypto.PasswordEncode;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.Charset;
import java.util.Base64;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Tests {
    private PasswordEncode passwordEncode;

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

