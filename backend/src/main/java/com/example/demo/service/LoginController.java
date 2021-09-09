package com.example.demo.service;

import com.example.demo.model.LoginForm;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LoginController {

    HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();
    String sessionIdCookieValue;
    String bonitaApiTokenCookieValue;

    @CrossOrigin
    @PostMapping("/login")
    public HttpServletResponse login(@RequestBody LoginForm loginForm, HttpServletResponse httpServletResponse) {

        HttpResponse<String> response = logIntoBonita(loginForm.getUsername(), loginForm.getPassword());

        if (response.statusCode() == 200) {
            List<String> cookiesString = response.headers().map().get("set-cookie");
            String[] sessionIdArray = cookiesString.get(0).split("; ");
            sessionIdCookieValue = sessionIdArray[0].substring(sessionIdArray[0].indexOf("=") + 1);
            String sessionIdPath = sessionIdArray[1].substring(sessionIdArray[1].indexOf("=") + 1);
            Cookie sessionIdCookie = new Cookie("JSESSIONID", sessionIdCookieValue);
            sessionIdCookie.setPath(sessionIdPath);
            httpServletResponse.addCookie(sessionIdCookie);

            String[] bonitaApiToken = cookiesString.get(1).split("; ");
            bonitaApiTokenCookieValue = bonitaApiToken[0].substring(bonitaApiToken[0].indexOf("=") + 1);
            String bonitaApiTokenPath = bonitaApiToken[1].substring(bonitaApiToken[1].indexOf("=") + 1);
            Cookie bonitaApiTokenCookie = new Cookie("X-Bonita-API-Token", bonitaApiTokenCookieValue);
            bonitaApiTokenCookie.setPath(bonitaApiTokenPath);
            httpServletResponse.addCookie(bonitaApiTokenCookie);

            httpServletResponse.setStatus(response.statusCode());
        }
        httpServletResponse.setStatus(response.statusCode());
        return null;
    }

    @CrossOrigin
    @GetMapping("/getLicense")
    public HttpServletResponse getLicense() {
        HttpResponse<String> response = getLicenseFromBonita();

        System.out.println(response.statusCode());

        return null;
    }

    private HttpResponse logIntoBonita(String username, String password) {

        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(buildFormDataFromMap(data))
                .uri(URI.create("http://localhost:8080/bonita/platformloginservice"))
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

    private HttpResponse getLicenseFromBonita() {

        String cookies = "JSESSIONID=" + sessionIdCookieValue + "; X-Bonita-API-Token=" + bonitaApiTokenCookieValue;

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/bonita/API/platform/license"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .setHeader("Cookie", cookies)
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