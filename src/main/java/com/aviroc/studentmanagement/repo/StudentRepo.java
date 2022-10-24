package com.aviroc.studentmanagement.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.aviroc.studentmanagement.model.Student;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface StudentRepo extends ReactiveCrudRepository<Student, String> {
	Mono<Student> findByRollNumber(int rollNumber);

	Flux<Student> findByStatus(String status);
}
