package com.gucarsoft.jwtauth.repository;

import com.gucarsoft.jwtauth.model.user.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    User findByVerificationCode(String verificationCode);
    
}
