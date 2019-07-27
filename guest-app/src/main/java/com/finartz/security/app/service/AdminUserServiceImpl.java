package com.finartz.security.app.service;


import com.finartz.security.app.domain.AdminUser;
import com.finartz.security.app.model.AdminModel;
import com.finartz.security.app.repository.GroupRepository;
import com.finartz.security.app.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    private static final String ADMINS = "/admins";
    private static final String ADMIN = "admin";
    private static final String USER = "user";
    private static final String SLASH = "/";
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private GroupRepository groupRepository;

    @Value("${landon.guest.service.url}")
    private String adminServiceUrl;

    @Autowired
    private final RestTemplate restTemplate;

    public AdminUserServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<AdminUser> getAllAdminUser() {
        String url = adminServiceUrl + ADMINS;
        HttpEntity<String> request = new HttpEntity<>(null, null);
        return this.restTemplate.exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<List<AdminUser>>() {
        }).getBody();
    }

    @Override
    public AdminUser addAdminUser(AdminModel adminModel, String hashedVal) {
        AdminUser adminuser = adminModel.translateModelToAdminUser(hashedVal);
        personRepository.create(adminuser);
        groupRepository.addMemberToGroup(ADMIN, adminuser);
        groupRepository.addMemberToGroup(USER, adminuser);
        String url = adminServiceUrl + ADMINS;
        HttpEntity<AdminModel> request = new HttpEntity<>(adminModel, null);
        return this.restTemplate.exchange(url, HttpMethod.POST, request, AdminUser.class).getBody();
    }

    @Override
    public AdminUser getAdminUser(String id) {
        String url = adminServiceUrl + ADMINS + SLASH + id;
        HttpEntity<String> request = new HttpEntity<>(null, null);
        return this.restTemplate.exchange(url, HttpMethod.GET, request, AdminUser.class).getBody();
    }

    @Override
    public AdminUser updateAdminUser(String id, AdminModel adminModel, String hashedVal) {
        personRepository.delete(adminModel.translateModelToAdminUser(hashedVal));
        groupRepository.removeMemberFromGroup(ADMIN, adminModel.translateModelToAdminUser(hashedVal));
        groupRepository.removeMemberFromGroup(USER, adminModel.translateModelToAdminUser(hashedVal));

        String url = adminServiceUrl + ADMINS + SLASH + id;
        personRepository.create(adminModel.translateModelToAdminUser(hashedVal));
        groupRepository.addMemberToGroup(ADMIN, adminModel.translateModelToAdminUser(hashedVal));
        groupRepository.addMemberToGroup(USER, adminModel.translateModelToAdminUser(hashedVal));

        HttpEntity<AdminModel> request = new HttpEntity<>(adminModel, null);
        return this.restTemplate.exchange(url, HttpMethod.PUT, request, AdminUser.class).getBody();
    }

    @Override
    public void deleteAdminUser(String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = adminServiceUrl + ADMINS + SLASH + id;
        HttpEntity<AdminModel> request = new HttpEntity<>(headers);
        restTemplate.exchange(url, HttpMethod.DELETE, request, Void.class, 101);
    }
}
