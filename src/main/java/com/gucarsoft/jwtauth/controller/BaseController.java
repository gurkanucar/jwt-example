package com.gucarsoft.jwtauth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public abstract class BaseController {

    @Autowired
    HttpServletRequest request;
    @Autowired
    HttpServletResponse response;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    String getAuthUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
