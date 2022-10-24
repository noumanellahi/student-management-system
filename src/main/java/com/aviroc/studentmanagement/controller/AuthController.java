package com.aviroc.studentmanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aviroc.studentmanagement.model.AuthRequest;
import com.aviroc.studentmanagement.model.AuthResponse;
import com.aviroc.studentmanagement.security.EncodePassword;
import com.aviroc.studentmanagement.security.JWTUtil;
import com.aviroc.studentmanagement.service.UserService;

import reactor.core.publisher.Mono;

@RestController
public class AuthController {

	private final JWTUtil jwtUtil;
	private final EncodePassword encodePassword;
	private final UserService userService;

	public AuthController(JWTUtil jwtUtil, EncodePassword encodePassword, UserService userService) {
		this.jwtUtil = jwtUtil;
		this.encodePassword = encodePassword;
		this.userService = userService;
	}

	@PostMapping("/login")
	public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest ar) {
		return userService.findByUserName(ar.getUsername())
				.filter(userDetails -> encodePassword.encode(ar.getPassword()).equals(userDetails.getPassword()))
				.map(userDetails -> ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails))))
				.switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
	}

}
