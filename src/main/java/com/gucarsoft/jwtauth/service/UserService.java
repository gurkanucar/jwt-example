package com.gucarsoft.jwtauth.service;


import com.gucarsoft.jwtauth.model.AuthRequest;
import com.gucarsoft.jwtauth.model.CreateUser;
import com.gucarsoft.jwtauth.model.other.Message;
import com.gucarsoft.jwtauth.model.other.ResetPassword;
import com.gucarsoft.jwtauth.model.user.Role;
import com.gucarsoft.jwtauth.model.user.User;
import com.gucarsoft.jwtauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService extends BaseService {

    @Autowired
    private UserRepository repo;


    public ResponseEntity create(User entity) {
        entity.setUsername(entity.getEmail());
        User createdEntity = repo.save(entity);
        if (createdEntity != null) {
            return new ResponseEntity<>(createdEntity, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public ResponseEntity update(User user) {
        User existing = repo.findById(user.getId()).orElse(null);
        if (existing == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (user.isResetPassword()) {
            existing.setPassword(user.getEmail());
            existing.setResetPassword(false);
        }

        existing.setUsername(user.getEmail());
        existing.setEmail(user.getEmail());
        existing.setName(user.getName());
        existing.setSurname(user.getSurname());
        existing.setMobile(user.getMobile());
        existing.setRole(user.getRole());

        return new ResponseEntity<>(repo.save(existing), HttpStatus.OK);
    }

    public ResponseEntity<List<User>> list() {
        List<User> users = repo.findAll();
        List<User> entityList = new ArrayList<>();
        for (User user : users) {
            if (user.getUsername() != null) {
                if (!user.getUsername().equals("admin")) {
                    entityList.add(user);
                }
            }
        }
        return new ResponseEntity<>(entityList, HttpStatus.OK);
    }

    public ResponseEntity<User> getById(long id) {
        User entity = repo.findById(id).orElse(null);
        User authUser = repo.findByUsername(getAuthUserName());

        if (entity != null) {
            if (authUser.getRole().equals(Role.ADMIN) || authUser.getId() == entity.getId()) {
                return new ResponseEntity<>(entity, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity deleteById(long id) {
        User entity = repo.findById(id).orElse(null);
        User user = repo.findByUsername(getAuthUserName());
        if (user.getId() == id) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
        return deleteById(entity, repo);
    }

    public ResponseEntity<User> getByUsername(String username) {
        User user = repo.findByUsername(username);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity updateProfile(String authUserName, User user) {
        User originalUser = repo.findByUsername(authUserName);
        originalUser.setName(user.getName());
        originalUser.setSurname(user.getSurname());
        originalUser.setEmail(user.getEmail());
        originalUser.setUsername(user.getEmail());
        originalUser.setMobile(user.getMobile());

        repo.save(originalUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity register(CreateUser user) {
        /*String bearerToken =
                ((ServletRequestAttributes) RequestContextHolder.
                        currentRequestAttributes()).
                        getRequest().getHeader("authorization");*/

        User entity = new User();

        User existingUsername = repo.findByUsername(user.getUsername());
        if (existingUsername != null) {
            return new ResponseEntity<>(Message.USERNAME_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }

        User existingEmail = repo.findByEmail(user.getEmail());
        if (existingEmail != null) {
            return new ResponseEntity<>(Message.EMAIL_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }

        // assign user role
        entity.setRole(Role.USER);
        entity.setEmail(user.getEmail());
        entity.setUsername(user.getEmail());
        entity.setName(user.getName());
        entity.setSurname(user.getSurname());
        entity.setPassword(user.getPassword());
        entity.setMobile(user.getMobile());
        entity.setPreferredLanguage(user.getPreferredLanguage());
        User savedUser = repo.save(entity);
        if (savedUser != null) {
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Message.SYSTEM_ERROR, HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity forgetPassword(String email) {
        User user = repo.findByEmail(email);
        if (user == null || !user.getRole().equals(Role.USER)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UUID verificationKey = UUID.randomUUID();
        user.setVerificationCode(verificationKey.toString());
        /*sendMail(email, "Reset Password", getMailContent("forgetPassword.html")
                .replaceFirst("#verificationKey#", verificationKey.toString()));*/
        return new ResponseEntity<>(repo.save(user), HttpStatus.OK);
    }

    public ResponseEntity resetPassword(ResetPassword resetPassword) {
        User user = repo.findByVerificationCode(resetPassword.getVerificationCode());
        if (!user.getVerificationCode().equals(resetPassword.getVerificationCode()) || !resetPassword.getNewPassword().equals(resetPassword.getRepeatPassword())) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        user.setVerificationCode(null);
        user.setPassword(resetPassword.getNewPassword());
        return new ResponseEntity<>(repo.save(user), HttpStatus.OK);
    }

    public ResponseEntity getUserCount() {
        return new ResponseEntity<>(repo.count(), HttpStatus.OK);
    }
}
