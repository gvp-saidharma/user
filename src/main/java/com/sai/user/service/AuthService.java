package com.sai.user.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.sai.user.config.UserDetailsImpl;
import com.sai.user.domain.Role;
import com.sai.user.domain.User;
import com.sai.user.model.*;
import com.sai.user.repository.RoleRepository;
import com.sai.user.repository.UserRepository;
import com.sai.user.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder pwdEncoder;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	public ResponseEntity<?> registerUser(RegistrationRequest registrationReq, boolean isAdmin) {
		if (userRepository.existsByUsername(registrationReq.getUsername())) {
			return ResponseEntity.badRequest().body("Error: Username is already taken!");
		}

		if (userRepository.existsByEmail(registrationReq.getEmail())) {
			return ResponseEntity.badRequest().body("Error: Email is already in use!");
		}

		User user = new User();
		user.setUsername(registrationReq.getUsername());
		user.setEmail(registrationReq.getEmail());
		user.setPassword(pwdEncoder.encode(registrationReq.getPassword()));
		Role role = null;
		if (isAdmin) {
			role = roleRepository.findByRole(RoleEnum.ROLE_ADMIN)
					.orElseThrow(() -> new RuntimeException("Role not found"));
			role.setRole(RoleEnum.ROLE_ADMIN);
		} else {
			role = roleRepository.findByRole(RoleEnum.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Role not found"));
			role.setRole(RoleEnum.ROLE_USER);
		}
		user.setRoles(Collections.singleton(role));

		userRepository.save(user);

		return ResponseEntity.ok("User created successfully");
	}

	public ResponseEntity<?> login(LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtUtils.generateToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(
				new JwtResponse(token, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
	}

	public ResponseEntity<?> refreshToken(Authentication authentication) {
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtUtils.generateToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(
				new JwtResponse(token, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
	}
}
