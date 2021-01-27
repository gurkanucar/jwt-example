package com.gucarsoft.jwtauth.config;


import com.gucarsoft.jwtauth.model.user.Role;
import com.gucarsoft.jwtauth.model.user.User;
import com.gucarsoft.jwtauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Component
public class FirstSetupConfig implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepo;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!alreadySetup) {
            User adminUser = userRepo.findByUsername("admin");
            if (adminUser == null) {

                // CREATE ADMIN USER
                User newUser = new User();
                newUser.setUsername("admin");
                newUser.setPassword("admin123");
                newUser.setName("admin");
                newUser.setSurname("admin");
                newUser.setEmail("admin");
                newUser.setRole(Role.ADMIN);
                userRepo.save(newUser);
            }
            alreadySetup = true;
        }
    }


}
