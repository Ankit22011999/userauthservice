package com.ankit.userauthservice.repos;

import com.ankit.userauthservice.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface  userRepo extends JpaRepository<User, Long> {

    Optional<User> findByEmailEquals(String email);

}
