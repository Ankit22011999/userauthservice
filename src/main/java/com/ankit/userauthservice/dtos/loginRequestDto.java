package com.ankit.userauthservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class loginRequestDto {
    private String email;
    private String password;
}
