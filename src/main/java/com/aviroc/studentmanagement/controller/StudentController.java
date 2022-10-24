package com.aviroc.studentmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aviroc.studentmanagement.model.Result;
import com.aviroc.studentmanagement.model.Student;
import com.aviroc.studentmanagement.repo.ResultRepo;
import com.aviroc.studentmanagement.repo.StudentRepo;
import com.aviroc.studentmanagement.service.ResultService;
import com.aviroc.studentmanagement.service.StudentService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/students")
public class StudentController {

	@Autowired
	private StudentRepo studentRepo;

	@Autowired
	private ResultRepo resultRepo;

	@Autowired
	private StudentService studentService;

	@Autowired
	private ResultService resultService;

	/**
	 * This will return the all available results in database.
	 * 
	 * @return
	 */
	@GetMapping
	private Flux<Result> getAllStudentsResult() {
		return resultService.getResultOfAllActiveUsers();
	}

	/**
	 * This will return the result of require user.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/result/{roll-number}", method = RequestMethod.GET)
	private Flux<Result> getSpecificStudentsResult(@PathVariable(value = "roll-number") int rollNUmber) {
		return resultRepo.findByRollNumber(rollNUmber);
	}

	/**
	 * This will add new student to database.
	 * 
	 * @param student
	 * @return
	 */
	@PostMapping
	private Mono<Student> save(@RequestBody Student student) {
		return studentRepo.save(student);
	}

	/**
	 * This will change the student status from active to delete
	 * 
	 * @param student
	 * @return
	 */
	@DeleteMapping
	private Mono<Student> delete(@RequestBody Student student) {
		return studentService.deleteStudent(student.getRollNumber());
	}
}
