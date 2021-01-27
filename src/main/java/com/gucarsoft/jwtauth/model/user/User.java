package com.gucarsoft.jwtauth.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gucarsoft.jwtauth.model.BaseEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


@Data
@Entity
@Where(clause = "deleted = false")
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1751002052247865647L;

    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;

    @Setter(AccessLevel.NONE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonIgnore
    private String verificationCode;

    private String name;
    private String surname;
    private String mobile;

    private boolean resetPassword;

    @Enumerated(EnumType.STRING)
    private Language preferredLanguage;

    @Enumerated(EnumType.STRING)
    private Role role;

    //

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }

}
