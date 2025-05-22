package com.ankit.userauthservice.Controller;

import com.ankit.userauthservice.Models.User;
import com.ankit.userauthservice.dtos.UserDTO;
import com.ankit.userauthservice.dtos.loginRequestDto;
import com.ankit.userauthservice.dtos.signupRequestDto;
import com.ankit.userauthservice.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class authController {

    private AuthService authService;

    public authController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public UserDTO signUp(@RequestBody signupRequestDto signuprequestdto) {

        try{
             User user = authService.signup(signuprequestdto.getEmail() ,signuprequestdto.getPassword());
             return from(user);
        } catch (Exception e) {
            throw e;
        }
    }

    @PostMapping("/login")
    public UserDTO login(@RequestBody loginRequestDto loginrequestdto){
        try{
           User user = authService.login(loginrequestdto.getEmail(),loginrequestdto.getPassword());
           return from(user);
        } catch (Exception e) {
            throw e;
        }
    }
    private UserDTO from(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }
}
