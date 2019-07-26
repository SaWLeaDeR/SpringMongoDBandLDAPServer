package com.finartz.security.app.controller;

import com.finartz.security.app.domain.AdminUser;
import com.finartz.security.app.model.AdminModel;
import com.finartz.security.app.service.AdminUserServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
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
public class AdminUserConroller {

    private static final String ADMINVIEW = "admin-view";
    private static final String ADMIN = "admin";
    private final AdminUserServiceImpl adminUserServiceImpl;

    public AdminUserConroller(AdminUserServiceImpl adminUserServiceImpl) {
        super();
        this.adminUserServiceImpl = adminUserServiceImpl;
    }

    @GetMapping(value = "/admins")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getAdminUsers(Model model) {
        List<AdminUser> admins = this.adminUserServiceImpl.getAllAdminUser();
        model.addAttribute("admins", admins);
        return "admins-view";
    }

    @GetMapping(value = "/admins/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAddGuestForm(Model model) {
        return ADMINVIEW;
    }

    @PostMapping(value = "/admins")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addAdminUser(HttpServletRequest request, Model model,
                                     @ModelAttribute AdminModel adminModel) {
        String hashedVal = Base64.getEncoder().encodeToString(DigestUtils.sha1(adminModel.getUserPassword().getBytes(Charset.forName("UTF-8"))));
        hashedVal = "{SHA}" + hashedVal;
        adminModel.setUserPassword(hashedVal);
        AdminUser admin = this.adminUserServiceImpl.addAdminUser(adminModel, hashedVal);
        model.addAttribute(ADMIN, admin);
        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
        return new ModelAndView("redirect:/admins/" + admin.getId());
    }

    @GetMapping(value = "/admins/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getAdminUser(Model model, @PathVariable String id) {
        AdminUser adminUser = this.adminUserServiceImpl.getAdminUser(id);
        model.addAttribute(ADMIN, adminUser);
        return ADMINVIEW;
    }

    @PostMapping(value = "/admins/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateAdminUser(Model model, @PathVariable String id, @ModelAttribute AdminModel adminModel) {
        String hashedVal = Base64.getEncoder().encodeToString(DigestUtils.sha1(adminModel.getUserPassword().getBytes(Charset.forName("UTF-8"))));
        hashedVal = "{SHA}" + hashedVal;
        adminModel.setUserPassword(hashedVal);
        this.adminUserServiceImpl.updateAdminUser(id, adminModel,hashedVal);
        model.addAttribute(ADMIN, null);
        model.addAttribute("adminModel", new AdminModel());
        return ADMINVIEW;
    }

    @DeleteMapping(value = "/admins/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteAdminUser(@PathVariable String id) {
        this.adminUserServiceImpl.deleteAdminUser(id);
        return "admins-view";
    }


}
