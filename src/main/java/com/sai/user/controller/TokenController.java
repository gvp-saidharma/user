package com.sai.user.controller;

import com.sai.user.model.LoginRequest;
import com.sai.user.model.RegistrationRequest;
import com.sai.user.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    private AuthService authService;

    @PostMapping(path = "/refresh", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> refreshToken(@RequestHeader("id") int id, @RequestHeader("username") String username,
                                          @RequestHeader("authorities") String authorities, Authentication authentication) {
//        System.out.println("refresh username:" + username);
//        System.out.println("id:" + id);
//        System.out.println("authorities:" + authorities);
        return authService.refreshToken(authentication);
    }

}
