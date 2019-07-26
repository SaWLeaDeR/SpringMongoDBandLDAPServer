package com.finartz.security.app.controller;

import com.finartz.security.app.domain.Guest;
import com.finartz.security.app.model.GuestModel;
import com.finartz.security.app.service.GuestServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/")
public class GuestController {

    private static final String GUESTVIEW = "guest-view";
    private static final String GUEST = "guest";
    private final GuestServiceImpl guestServiceImpl;

    public GuestController(GuestServiceImpl guestServiceImpl) {
        super();
        this.guestServiceImpl = guestServiceImpl;
    }

    @GetMapping(value = {"/", "/index"})
    public String getHomePage(Model model) {
        return "index";
    }

    @GetMapping(value = "/login")
    public String getLoginPage(Model model) {
        return "login";
    }

    @GetMapping(value = "/logout-success")
    public String getLogoutPage(Model model) {
        return "logout";
    }

    @GetMapping(value = "/guests")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getGuests(Model model) {
        List<Guest> guests = this.guestServiceImpl.getAllGuests();
        model.addAttribute("guests", guests);
        return "guests-view";
    }

    @GetMapping(value = "/guests/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAddGuestForm(Model model) {
        return GUESTVIEW;
    }

    @PostMapping(value = "/guests")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addGuest(HttpServletRequest request, Model model,
                                 @ModelAttribute GuestModel guestModel) {
        String hashedVal = Base64.getEncoder().encodeToString(DigestUtils.sha1(guestModel.getPassword().getBytes(Charset.forName("UTF-8"))));
        hashedVal = "{SHA}" + hashedVal;
        guestModel.setPassword(hashedVal);
        Guest guest = this.guestServiceImpl.addGuest(guestModel,hashedVal);
        model.addAttribute(GUEST, guest);
        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
        return new ModelAndView("redirect:/guests/" + guest.getId());
    }

    @GetMapping(value = "/guests/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getGuest(Model model, @PathVariable ObjectId id) {
        Guest guest = this.guestServiceImpl.getGuest(id);
        model.addAttribute(GUEST, guest);
        return GUESTVIEW;
    }

    @PostMapping(value = "/guests/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateGuest(Model model, @PathVariable ObjectId id, @ModelAttribute GuestModel guestModel) {
        String hashedVal = Base64.getEncoder().encodeToString(DigestUtils.sha1(guestModel.getPassword().getBytes(Charset.forName("UTF-8"))));
        hashedVal = "{SHA}" + hashedVal;
        guestModel.setPassword(hashedVal);
        this.guestServiceImpl.updateGuest(id, guestModel,hashedVal);
        model.addAttribute(GUEST, null);
        model.addAttribute("guestModel", new GuestModel());
        return GUESTVIEW;
    }

    @DeleteMapping(value = "/guests/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteGuest(@PathVariable ObjectId id) {
        this.guestServiceImpl.deleteGuest(id);
        return "guests-view";
    }
}
