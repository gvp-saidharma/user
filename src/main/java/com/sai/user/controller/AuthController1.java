package com.sai.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class AuthController1 {


	@GetMapping()
	public ResponseEntity<?> register(Authentication authentication) {
//		System.out.println(authentication.getName());
//		System.out.println(authentication.getAuthorities());
//		System.out.println(authentication.getName());
		return ResponseEntity.badRequest().body(authentication.getPrincipal());
	}


}
