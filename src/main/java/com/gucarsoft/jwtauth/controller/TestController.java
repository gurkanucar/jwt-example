package com.gucarsoft.jwtauth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/show")
    public String selamunAleykum() {
        return "calisiyor";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public String loginLazim() {
        return "bunu login olan gorur";
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin")
    public String adminOzel() {
        return "admine ozel ;)";
    }



}
