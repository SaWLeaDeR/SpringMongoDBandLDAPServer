package com.finartz.security.app.service;

import com.finartz.security.app.domain.Guest;
import com.finartz.security.app.domain.GuestModel;
import org.bson.types.ObjectId;

import java.util.List;

public interface IGuestService {
    List<Guest> getAllGuests();
    Guest addGuest(GuestModel guestModel,String captchaResponse);
    Guest getGuest(ObjectId id);
    Guest updateGuest(ObjectId id, GuestModel guestModel);
    void deleteGuestDemo(ObjectId id);


}
