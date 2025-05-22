package com.ankit.userauthservice.Models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.service.annotation.GetExchange;

@Getter
@Setter
@Entity
public class User extends  BaseModel{
    private String email;
    private String password;
}
