package com.example.demo.service;

import com.example.demo.model.LoginResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class LoginController {

    @GetMapping("/login")
    public LoginResponse login() {
        LoginResponse loginResponse = new LoginResponse();

        loginResponse.setStatus(true);
        loginResponse.setMessage("You are logged in");
        loginResponse.setData(new ArrayList());

        return loginResponse;
    }

}