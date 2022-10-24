package com.aviroc.studentmanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aviroc.studentmanagement.enums.StudentStatus;
import com.aviroc.studentmanagement.model.Student;
import com.aviroc.studentmanagement.repo.StudentRepo;

import lombok.extern.log4j.Log4j2;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

@Service
@Log4j2
public class StudentService {

	@Autowired
	private StudentRepo studentRepo;
	
	/**
	 * Return the list of roll numbers of active students.
	 * @return
	 */
	public List<Integer> findRollNumbersOfAllActiveStudents() {
		List<Integer> rollNumbers = new ArrayList<Integer>();
		Disposable subscribe = this.studentRepo.findByStatus(StudentStatus.Active.toString())
				.switchIfEmpty(Mono.error(new Exception("NO RECORD FOUND"))).map(record -> {
					rollNumbers.add(record.getRollNumber());
					return rollNumbers;
				}).subscribe();

		while (!subscribe.isDisposed()) {
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				log.error(e);
			}
		}
		
		return rollNumbers;
	}

	/*
	 * Soft delete the student.
	 */
	public Mono<Student> deleteStudent(int rollNumber) {

		return this.studentRepo.findByRollNumber(rollNumber).switchIfEmpty(Mono.error(new Exception("NO RECORD FOUND")))
				.map(record -> {
					record.setStatus(StudentStatus.Deleted.toString());
					return record;
				}).flatMap(this.studentRepo::save);
	}

}
