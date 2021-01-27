package com.gucarsoft.jwtauth.model.user;

import com.gucarsoft.jwtauth.model.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import java.io.Serializable;

@Data
@Entity
@Where(clause = "deleted = false")
public class UserDTO extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 4479809601898895873L;

    private String name;
    private String surname;
    private String email;
    private String mobile;
    private String password;

    private boolean resetPassword;

}
