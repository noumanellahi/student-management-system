package com.aviroc.studentmanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aviroc.studentmanagement.enums.Remarks;
import com.aviroc.studentmanagement.enums.StudentStatus;
import com.aviroc.studentmanagement.model.Result;
import com.aviroc.studentmanagement.repo.ResultRepo;
import com.aviroc.studentmanagement.repo.StudentRepo;

import lombok.extern.log4j.Log4j2;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Log4j2
public class ResultService {

	@Autowired
	private StudentRepo studentRepo;

	@Autowired
	private StudentService studentService;

	@Autowired
	private ResultRepo resultRepo;

	/**
	 * This will save the new result data in database and update the historical data
	 * accordingly.
	 * 
	 * @param result
	 * @return
	 */
	public List<String> updateResult(Result result) {
		List<Integer> userWithSameMarks = getPositionOfStudentWithSameMarks(result.getObtainedMarks());

		List<String> response = new ArrayList<String>();

		if (!userWithSameMarks.isEmpty()) {
			result.setPositionInClass(userWithSameMarks.get(0));
		} else {
			result.setPositionInClass(getUsersWithMoreMarks(result.getObtainedMarks()) + 1);
		}

		Disposable subscribe = studentRepo.findByRollNumber(result.getRollNumber()).map(f -> {
			if (f.getStatus().equalsIgnoreCase(StudentStatus.Active.toString())) {
				result.setGrade(f.getGrade());
				result.setRemarks(getRemarks(result.getObtainedMarks()));

				if (userWithSameMarks.isEmpty()) {
					updateOtherStudentPosition(result.getObtainedMarks());
				}
				resultRepo.save(result).subscribe();
				return response.add("POSITION OF STUDENT IS : " + result.getPositionInClass());
			} else {
				return response.add("STUDENT IS NOT ACTIVE");
			}
		}).subscribe();

		while (!subscribe.isDisposed()) {
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				log.error(e);
			}
		}

		if (response.isEmpty()) {
			response.add("USER WITH THIS ROLL-NUMBER DOESN'T EXIST : " + result.getRollNumber());
		}
		return response;
	}

	/**
	 * Return either user is Passed/Failed on the basis of its marks
	 * 
	 * @param obtainedMarks
	 * @return
	 */
	private String getRemarks(int obtainedMarks) {
		if (obtainedMarks >= 50) {
			return Remarks.PASSED.toString();
		} else {
			return Remarks.FAILED.toString();
		}
	}

	/**
	 * Give the position of user with the same obtained marks.
	 * 
	 * @return
	 */
	public List<Integer> getPositionOfStudentWithSameMarks(int obtainedMarks) {
		List<Integer> positioin = new ArrayList<Integer>();
		Disposable subscribe = resultRepo.findByObtainedMarks(obtainedMarks).map(e -> {
			positioin.add(e.getPositionInClass());
			return e;
		}).subscribe();

		while (!subscribe.isDisposed()) {
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				log.error(e);
			}
		}
		return positioin;
	}

	/**
	 * Give the count of user with more obtained marks.
	 * 
	 * @return
	 */
	public int getUsersWithMoreMarks(int obtainedMarks) {
		List<Integer> rollNumbers = new ArrayList<Integer>();
		Disposable subscribe = resultRepo.findResultsWithMoreObtainMarks(obtainedMarks).map(e -> {
			rollNumbers.add(e.getObtainedMarks());
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
		return rollNumbers.size();
	}

	/**
	 * Update the position of user with less obtained numbers.
	 * 
	 * @return
	 */
	public void updateOtherStudentPosition(int obtainedMarks) {
		resultRepo.findResultsWithLessObtainMarks(obtainedMarks).map(e -> {
			e.setPositionInClass(e.getPositionInClass() + 1);
			return e;
		}).flatMap(this.resultRepo::save).subscribe();
	}

	/**
	 * Return the result of active users.
	 * 
	 * @return
	 */
	public Flux<Result> getResultOfAllActiveUsers() {
		List<Integer> rollNumbers = studentService.findRollNumbersOfAllActiveStudents();
		return this.resultRepo.findByRollNumberIn(rollNumbers)
				.switchIfEmpty(Mono.error(new Exception("NO RECORD FOUND")));
	}

}
