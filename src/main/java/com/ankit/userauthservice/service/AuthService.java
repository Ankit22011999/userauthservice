package com.ankit.userauthservice.service;

import com.ankit.userauthservice.Models.userSessions;
import com.ankit.userauthservice.config.AuthConfiguration;
import com.ankit.userauthservice.repos.userSessionRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import org.apache.commons.lang3.tuple.Pair;
import com.ankit.userauthservice.Models.User;
import com.ankit.userauthservice.exception.PasswordMismatchException;
import com.ankit.userauthservice.exception.UserIsNotFoundInSystemException;
import com.ankit.userauthservice.exception.userAlreadySigninException;
import com.ankit.userauthservice.repos.userRepo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private  SecretKey secretKey;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder ;

    @Autowired
    private userRepo userRepo;

    @Autowired
    private userSessionRepo userSessionsRepo;

    public User signup(String email , String password){
        Optional<User> userOptional = userRepo.findByEmailEquals(email);
        if(userOptional.isPresent()){
            throw new userAlreadySigninException("Please Login Directly");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepo.save(user);
        return user;
    }
    public Pair<User, String> login(String email, String password){
        Optional<User> userOptional = userRepo.findByEmailEquals(email);
        if(userOptional.isEmpty()){
            throw new UserIsNotFoundInSystemException("Please process the signup first and then retry");
        }
        String checkPassword = userOptional.get().getPassword();
        if(!bCryptPasswordEncoder.matches(password ,  checkPassword)){
            throw new PasswordMismatchException("Please Enter Correct Password  , Otherwise reset it !!");
        }
        Map<String , Object > claims = new HashMap<>();

        claims.put("userId", userOptional.get().getId());
        claims.put("email", userOptional.get().getEmail());
        Long nowInMillis = System.currentTimeMillis();
        claims.put("iat" , nowInMillis);
        claims.put("exp", nowInMillis+100000);
        claims.put("iss","Authentication");


        String token =Jwts.builder().claims(claims).signWith(secretKey).compact();

        //persisting the generating token

        userSessions usersessions = new userSessions();
        usersessions.setUser(userOptional.get());
        usersessions.setToken(token);
        userSessionsRepo.save(usersessions);

        return Pair.of(userOptional.get(), token);

    }

    public boolean validateToken(String token , Long id ){

        Optional<userSessions> userSessionsOptional = userSessionsRepo.findByTokenAndUser_id(token,id);

        if(userSessionsOptional.isEmpty()){
            return false;
        }
        userSessions userSessions = userSessionsOptional.get();
        String persistedToken = userSessions.getToken();

        // parsing token to get payload to get expiry

        JwtParser  jwtParser =Jwts.parser().verifyWith(secretKey).build();
        Claims claims = jwtParser.parseSignedClaims(token).getPayload();

        Long exp = (Long)claims.get("exp");
        Long currentTime = (Long)System.currentTimeMillis();

        if(currentTime > exp){
            System.out.println("Token has expire ");
            return false;
        }


        return true;
    }


}
