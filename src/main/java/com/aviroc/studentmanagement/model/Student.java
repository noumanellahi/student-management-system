package com.aviroc.studentmanagement.model;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.aviroc.studentmanagement.enums.StudentStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Student {
	@Id
	private String id;

	@CreatedDate
	private Date createdOn;

	@LastModifiedDate
	private Date updatedOn;

	private String name;

	@Min(value = 1, message = "Required min roll number is 1")
	@Max(value = 100, message = "Required max roll number is 100")
	private int rollNumber;

	private String fatherName;

	@Min(value = 1, message = "Required min grade is 1")
	@Max(value = 10, message = "Required max grade is 10")
	private int grade;

	private String status = StudentStatus.Active.toString();

	public Student(String name, int rollNumber, String fatherName, int grade, String status) {
		super();
		this.name = name;
		this.rollNumber = rollNumber;
		this.fatherName = fatherName;
		this.grade = grade;
		this.status = status;
	}
}
