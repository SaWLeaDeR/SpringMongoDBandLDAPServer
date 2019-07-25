package com.finartz.security.services.service;

import com.finartz.security.services.repository.AdminRepository;
import com.finartz.security.services.ui.AdminUser;
import com.finartz.security.services.ui.model.AdminModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admins")
public class AdminServicesController {

    private AdminRepository repository;

    public AdminServicesController(AdminRepository repository) {
        super();
        this.repository = repository;
    }

    @GetMapping
    public List<AdminUser> getAllAdminUser() {
        return new ArrayList<>(this.repository.findAll());
    }

    @PostMapping
    public ResponseEntity<AdminUser> addAdminUser(@RequestBody AdminModel model) {
        AdminUser adminUser = repository.save(model.translateModelToAdminUser());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(adminUser.getId()).toUri();
        return ResponseEntity.created(location).body(adminUser);
    }

    @GetMapping("/{id}")
    public AdminUser getAdminUser(@PathVariable("id") String id) {
        return repository.findBy_id(id);
    }

    @PutMapping("/{id}")
    public void updateAdminUser(@PathVariable("id") String id, @Valid @RequestBody AdminUser adminUser) {
        adminUser.setId(id);
        repository.save(adminUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    public void deleteAdminUser(@PathVariable String id) {
        repository.delete(repository.findBy_id(id));
    }

}
