package com.ankit.userauthservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateTokenRequest {

    private String token;
    private long userid;
}
