package com.ankit.userauthservice.Controller;

import com.ankit.userauthservice.dtos.ValidateTokenRequest;
import org.apache.commons.lang3.tuple.Pair;
import com.ankit.userauthservice.Models.User;
import com.ankit.userauthservice.dtos.UserDTO;
import com.ankit.userauthservice.dtos.loginRequestDto;
import com.ankit.userauthservice.dtos.signupRequestDto;
import com.ankit.userauthservice.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
    public ResponseEntity<UserDTO> login(@RequestBody loginRequestDto loginrequestdto){
        try {
            Pair<User, String> userWithToken = authService.login(loginrequestdto.getEmail(), loginrequestdto.getPassword());
            UserDTO userDTO = from(userWithToken.getLeft());
            MultiValueMap<String , String > headers = new LinkedMultiValueMap<>();
            headers.add(HttpHeaders.SET_COOKIE , userWithToken.getRight());
            return  new ResponseEntity<>(userDTO ,headers, HttpStatus.valueOf(200));
        }catch (Exception e) {
            throw e;
        }
    }

    @PostMapping("/validateToken")
    public ResponseEntity<String> validateToken(@RequestBody ValidateTokenRequest validateTokenRequest){
        boolean result = authService.validateToken(validateTokenRequest.getToken() , validateTokenRequest.getUserid());
        if(result){
//            System.out.println("Token has expired ");
            return new ResponseEntity<>("SUCCESS" , HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("FAILURE" , HttpStatus.UNAUTHORIZED);
        }
    }
    private UserDTO from(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }
}
