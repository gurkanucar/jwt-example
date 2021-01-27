package com.gucarsoft.jwtauth.service;


import com.gucarsoft.jwtauth.model.BaseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import java.io.*;
import java.nio.charset.StandardCharsets;
import org.apache.http.Consts;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

public class BaseService {


//    @Value("${mailServiceUrl}")
//    String mailServiceUrl;


    protected String getAuthUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    ResponseEntity deleteById(BaseEntity entity, JpaRepository repo) {
        if (entity != null) {
            entity.setDeleted(true);
            repo.save(entity);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    public String generateUniqueCode() {
        String letters = "ABCDEFGHIJKLMNOPQPRSTUVWXYZ";
        String numbers = "0123456789";

        int codeLength = 10;
        String code = "";

        for (int i = 0; i < codeLength; i++) {
            if (i % 2 == 0) {
                int rnum = (int) Math.floor(Math.random() * letters.length());
                code += letters.charAt(rnum);
            } else {
                int rnum2 = (int) Math.floor(Math.random() * numbers.length());
                code += numbers.charAt(rnum2);
            }
        }

        return code;
    }


/*
    void sendMail(String to, String subject, String message) {
        try {
            Request.Post(mailServiceUrl)
                    .bodyForm(Form.form()
                            .add("from", null)
                            .add("to", to)
                            .add("subject", subject)
                            .add("mailContent", message)
                            .build(), Consts.UTF_8)
                    .execute().returnResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/



}
