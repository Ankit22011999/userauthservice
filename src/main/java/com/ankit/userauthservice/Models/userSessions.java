package com.ankit.userauthservice.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.service.annotation.GetExchange;

@Entity
@Getter@Setter
public class userSessions extends  BaseModel{
    @ManyToOne
    private User user;

    private String token;

}
