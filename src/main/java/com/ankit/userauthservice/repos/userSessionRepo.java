package com.ankit.userauthservice.repos;

import com.ankit.userauthservice.Models.userSessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface userSessionRepo extends JpaRepository<userSessions, Long> {

    Optional<userSessions> findByTokenAndUser_id(String token, Long user_id);
}
