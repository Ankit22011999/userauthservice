package com.ankit.userauthservice.service;

import com.ankit.userauthservice.Models.User;
import com.ankit.userauthservice.exception.PasswordMismatchException;
import com.ankit.userauthservice.exception.UserIsNotFoundInSystemException;
import com.ankit.userauthservice.exception.userAlreadySigninException;
import com.ankit.userauthservice.repos.userRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private userRepo userRepo;

    public User signup(String email , String password){
        Optional<User> userOptional = userRepo.findByEmailEquals(email);
        if(userOptional.isPresent()){
            throw new userAlreadySigninException("Please Login Directly");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        userRepo.save(user);
        return user;
    }
    public User login(String email, String password){
        Optional<User> userOptional = userRepo.findByEmailEquals(email);
        if(userOptional.isEmpty()){
            throw new UserIsNotFoundInSystemException("Please process the signup first and then retry");
        }
        String checkPassword = userOptional.get().getPassword();
        if(!checkPassword.equals(password)){
            throw new PasswordMismatchException("Please Enter Correct Password  , Otherwise reset it !!");
        }
        return userOptional.get();
    }
}
