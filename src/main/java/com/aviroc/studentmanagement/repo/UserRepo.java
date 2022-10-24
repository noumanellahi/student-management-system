package com.aviroc.studentmanagement.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.aviroc.studentmanagement.model.User;

import reactor.core.publisher.Mono;

@Repository
public interface UserRepo extends ReactiveMongoRepository<User, String>{
	
	Mono<User> findByUsername(String username);
}
