package com.gucarsoft.jwtauth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gucarsoft.jwtauth.model.user.Language;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class CreateUser {

    private String username;

    @Setter(AccessLevel.NONE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String name;
    private String surname;
    private String email;
    private String mobile;

    @Enumerated(EnumType.STRING)
    private Language preferredLanguage;

}
