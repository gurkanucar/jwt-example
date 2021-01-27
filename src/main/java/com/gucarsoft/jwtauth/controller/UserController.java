package com.gucarsoft.jwtauth.controller;


import com.gucarsoft.jwtauth.config.jwt.JwtUtil;
import com.gucarsoft.jwtauth.model.AuthRequest;
import com.gucarsoft.jwtauth.model.CreateUser;
import com.gucarsoft.jwtauth.model.other.ResetPassword;
import com.gucarsoft.jwtauth.model.user.User;
import com.gucarsoft.jwtauth.model.user.UserDTO;
import com.gucarsoft.jwtauth.service.UserService;
import com.gucarsoft.jwtauth.service.security.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController {

    @Autowired
    private UserService service;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    // ***************************
    // ADMIN OPERATIONS
    // ***************************

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity create(@RequestBody User user) {
        return service.create(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping
    public ResponseEntity update(@RequestBody User user) {
        return service.update(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> list() {
        return service.list();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SHOP','DELIVERY')")
    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable Long id) {
        return service.getById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        return service.deleteById(id);
    }


    // ***************************
    // USER OPERATIONS
    // ***************************

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/self")
    public ResponseEntity<User> getSelf() {
        return service.getByUsername(getAuthUserName());
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/updateProfile")
    public ResponseEntity updateProfile(@RequestBody User user) {
        return service.updateProfile(getAuthUserName(), user);
    }


    // PUBLIC
    @PostMapping("/login")
    public String creteToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
            return jwtUtil.generateToken(userDetails);
        } catch (AuthenticationException ex) {
            throw new Exception("Incorrect username or password", ex);
        }
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody CreateUser user) {
        return service.register(user);
    }

    @GetMapping("/forgetPassword/{email}")
    public ResponseEntity forgetPassword(@PathVariable String email) {
        return service.forgetPassword(email);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity resetPassword(@RequestBody ResetPassword resetPassword) {
        return service.resetPassword(resetPassword);
    }

    @GetMapping("/count")
    public ResponseEntity getUserCount(){
        return service.getUserCount();
    }

}
