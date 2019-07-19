package com.finartz.security.services;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * fpmoles password      jdoe foobar
 */
@RestController
@RequestMapping("/guests")
public class GuestServicesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuestServicesController.class);

    private GuestRepository repository;

    public GuestServicesController(GuestRepository repository){
        super();
        this.repository = repository;
    }

    @GetMapping
    public List<Guest> getAllGuests(){
        return new ArrayList<>(this.repository.findAll());
    }

    @PostMapping
    public ResponseEntity<Guest> addGuest(@RequestBody GuestModel model){
        Guest guest = this.repository.save(model.translateModelToGuest());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(guest.getId()).toUri();
        return ResponseEntity.created(location).body(guest);
    }

    @GetMapping("/{id}")
    public Guest getGuest(@PathVariable("id") ObjectId id) {
        return repository.findBy_id(id);
    }

    @PutMapping("/{id}")
    public void updateGuest(@PathVariable("id") ObjectId id, @Valid @RequestBody Guest guest) {
        guest.setId(id);
        repository.save(guest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    public void deleteGuest(@PathVariable ObjectId id) {
        repository.delete(repository.findBy_id(id));
    }
}
