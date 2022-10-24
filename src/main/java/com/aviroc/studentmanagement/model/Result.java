package com.aviroc.studentmanagement.model;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document
@Getter
@Setter
@ToString
public class Result {
	@Id
	private String id;

	@CreatedDate
	private Date createdOn;

	@LastModifiedDate
	private Date updatedOn;

	private int totalMarks;
	
	private int obtainedMarks;

	private int rollNumber;

	private int grade;
	
	private String remarks;
	
	private int positionInClass;

}
