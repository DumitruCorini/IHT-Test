package com.example.demo.service;

import com.example.demo.model.LoginResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @PostMapping("/login")
    public LoginResponse login(HttpServletRequest request) {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        HttpResponse response = logIntoBonita(username, password);

        LoginResponse loginResponse = new LoginResponse();

        if (response.statusCode() == 204) {
            loginResponse.setStatus(response.statusCode());
            loginResponse.setMessage("Login successful");

            return loginResponse;
        }
        loginResponse.setStatus(response.statusCode());
        loginResponse.setMessage("Login error");

        return loginResponse;
    }

    private HttpResponse logIntoBonita(String username, String password) {

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(buildFormDataFromMap(data))
                .uri(URI.create("http://localhost:8080/bonita/loginservice?redirect=false"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();


        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return response;
        } catch (Exception e) {
            System.err.println(e);
        }

        return null;
    }

    private static HttpRequest.BodyPublisher buildFormDataFromMap(Map<String, String> data) {
        var builder = new StringBuilder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }
}