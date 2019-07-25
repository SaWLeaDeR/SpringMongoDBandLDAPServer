package com.finartz.security.services.repository;

import com.finartz.security.services.ui.AdminUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminRepository extends MongoRepository<AdminUser, String> {

    AdminUser findBy_id(String _id);
}
