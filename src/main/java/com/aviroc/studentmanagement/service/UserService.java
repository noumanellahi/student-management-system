package com.aviroc.studentmanagement.service;

import org.springframework.stereotype.Service;

import com.aviroc.studentmanagement.model.User;
import com.aviroc.studentmanagement.repo.UserRepo;

import reactor.core.publisher.Mono;

@Service
public class UserService {
	private final UserRepo userRepo;

	public UserService(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	public Mono<User> findByUserName(String username) {
		return this.userRepo.findByUsername(username);
	}
}
