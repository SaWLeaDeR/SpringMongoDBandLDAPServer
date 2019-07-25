package com.finartz.security.app.service;

import com.finartz.security.app.captcha.ReCaptchaResponse;
import com.finartz.security.app.domain.Guest;
import com.finartz.security.app.model.GuestModel;
import com.finartz.security.app.repository.GroupRepository;
import com.finartz.security.app.repository.PersonRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GuestService implements IGuestService {
    private static final String GUESTS = "/guests";
    private static final String SLASH = "/";

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private GroupRepository groupRepository;

    @Value("${landon.guest.service.url}")
    private String guestServiceUrl;


    private final RestTemplate restTemplate;

    public GuestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Guest> getAllGuests() {
        String url = guestServiceUrl + GUESTS;
        HttpEntity<String> request = new HttpEntity<>(null, null);
        return this.restTemplate.exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<List<Guest>>() {
        }).getBody();
    }

    public Guest addGuest(GuestModel guestModel,String hashedVal) {
//        String captchaurl = "https://www.google.com/recaptcha/api/siteverify";
//        String params = "?secret=6LdiZa4UAAAAABSui6lHL9JSkpD1uz9Rm14KytOm-&response=" + captchaResponse;
//        ReCaptchaResponse reCaptchaResponse = restTemplate.exchange(captchaurl + params, HttpMethod.POST, null, ReCaptchaResponse.class).getBody();
        Guest guestuser = guestModel.translateModelToGuest(hashedVal);
        personRepository.create(guestuser);
        groupRepository.addMemberToGroup("user", guestuser);
        String url = guestServiceUrl + GUESTS;
        HttpEntity<GuestModel> request = new HttpEntity<>(guestModel, null);
        return this.restTemplate.exchange(url, HttpMethod.POST, request, Guest.class).getBody();
    }

    public Guest getGuest(ObjectId id) {
        String url = guestServiceUrl + GUESTS + SLASH + id;
        HttpEntity<String> request = new HttpEntity<>(null, null);
        return this.restTemplate.exchange(url, HttpMethod.GET, request, Guest.class).getBody();
    }

    public Guest updateGuest(ObjectId id, GuestModel guestModel, String hashedVal) {
        personRepository.delete(guestModel.translateModelToGuest(hashedVal));
        groupRepository.removeMemberFromGroup("user", guestModel.translateModelToGuest(hashedVal));


        String url = guestServiceUrl + GUESTS + SLASH + id;
        personRepository.create(guestModel.translateModelToGuest(hashedVal));
        groupRepository.addMemberToGroup("user", guestModel.translateModelToGuest(hashedVal));

        HttpEntity<GuestModel> request = new HttpEntity<>(guestModel, null);
        return this.restTemplate.exchange(url, HttpMethod.PUT, request, Guest.class).getBody();
    }

    public void deleteGuest(ObjectId id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = guestServiceUrl + GUESTS + SLASH + id;
        HttpEntity<GuestModel> request = new HttpEntity<>(headers);
        restTemplate.exchange(url, HttpMethod.DELETE, request, Void.class, 101);
    }
}
