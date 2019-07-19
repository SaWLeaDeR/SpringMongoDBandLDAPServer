package com.finartz.security.app;

import com.finartz.security.app.service.GuestService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Tests {
    @Autowired
    private GuestService guestService;


    public Tests(GuestService guestService) {
        this.guestService = guestService;
    }

}
