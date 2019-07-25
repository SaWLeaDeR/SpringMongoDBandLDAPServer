package com.finartz.security.app.service;

import com.finartz.security.app.domain.AdminUser;
import com.finartz.security.app.model.AdminModel;

import java.util.List;

public interface IAdminUserService {
    List<AdminUser> getAllAdminUser();

    AdminUser addAdminUser(AdminModel adminModel, String hashedVal);

    AdminUser getAdminUser(String id);

    AdminUser updateAdminUser(String id, AdminModel adminModel, String hashedVal);

    void deleteAdminUser(String id);
}
