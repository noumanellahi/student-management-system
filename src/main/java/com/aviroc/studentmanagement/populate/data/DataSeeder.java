package com.aviroc.studentmanagement.populate.data;

import java.util.Arrays;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.aviroc.studentmanagement.enums.Role;
import com.aviroc.studentmanagement.enums.StudentStatus;
import com.aviroc.studentmanagement.model.Student;
import com.aviroc.studentmanagement.model.User;
import com.aviroc.studentmanagement.repo.StudentRepo;
import com.aviroc.studentmanagement.repo.UserRepo;
import com.aviroc.studentmanagement.security.EncodePassword;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class DataSeeder implements ApplicationListener<ApplicationReadyEvent> {

	private final StudentRepo studentRepo;
	private final UserRepo userRepo;
	private final EncodePassword encoder;

	public DataSeeder(StudentRepo studentRepo, UserRepo userRepo, EncodePassword encoder) {
		super();
		this.studentRepo = studentRepo;
		this.userRepo = userRepo;
		this.encoder = encoder;
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {

		studentRepo.deleteAll().thenMany(this.addStudents().flatMap(studentRepo::save))
				.subscribe(film -> log.info("STUDENT ADDED : {} ", film.toString()));

		userRepo.deleteAll()
				.then(Mono.just(new User("nomi", encoder.encode("123456"), "Noman Ellahi", true,
						Arrays.asList(Role.ROLE_USER))))
				.flatMap(userRepo::save).subscribe(user -> log.info("USER ADDED : {}", user));
	}

	private Flux<Student> addStudents() {
		return Flux.just(new Student("Noman Ellahi", 1, "Tariq Ali", 10, StudentStatus.Active.toString()),
				new Student("Hassan Nadeem", 2, "Muhammad Nadeem", 10, StudentStatus.Active.toString()),
				new Student("Ahmer Shafeeq", 3, "Shafeeq Ahmed", 10, StudentStatus.Active.toString()));
	}

}
