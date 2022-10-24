package com.aviroc.studentmanagement.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.aviroc.studentmanagement.model.Result;

import reactor.core.publisher.Flux;

@Repository
public interface ResultRepo extends ReactiveCrudRepository<Result, String> {

	Flux<Result> findByRollNumberIn(List<Integer> rollNumber);

	Flux<Result> findByRollNumber(int rollNumber);

	Flux<Result> findByObtainedMarks(int obtainedMarks);

	@Query("{obtainedMarks : {$gt: ?0}}")
	Flux<Result> findResultsWithMoreObtainMarks(int obtainedMarks);

	@Query("{obtainedMarks : {$lt: ?0}}")
	Flux<Result> findResultsWithLessObtainMarks(int obtainedMarks);

}
