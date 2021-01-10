package com.RESTService.creditdecisionengine.repository;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="APPLICANT_DETAILS")
@ApiModel(description=" Applicant details on sanctioned/rejected loan ")
public class ApplicantDetails {
	
	@Id
	@Column(name="SSNID")
	@ApiModelProperty(notes ="SSNID is a unique number ")
	@Pattern(regexp = "[\\s]*[0-9]*[1-9]+",message=" SSNID must be valid number")
	private long ssnID;
	
	@Column(name="LOAN_AMOUNT")
	@ApiModelProperty(notes ="Required loan amount, minimum loan amount = 1000 ")
	@Pattern(regexp = "[\\s]*[0-9]*[1-9]+",message=" Loan Amount must be valid number")
	@Min(value=1000, message = "Loan Amount should be greater than 1000 pounds" )
	private long loan_amount;
	
	@ApiModelProperty(notes ="Current annual income ")
	@Pattern(regexp = "[\\s]*[0-9]*[1-9]+",message=" Loan Amount must be valid number")
	@Column(name="CURRENT_INCOME")
	private long current_income;
	
	@ApiModelProperty(notes ="Sanctioned loan amount calculated based on current annual income ")
	@Column(name="SANCTIONED_AMOUNT")
	private long sanctionedAmount;
	
	@ApiModelProperty(notes ="The date at which credit score has been checked ")
	@Column(name="APPLIED_DATE")
	private LocalDateTime appliedDate;
	
	@ApiModelProperty(notes ="Status of the aplication calculated based on credit score ")
	@Column(name="STATUS")
	private String loanStatus;
	

}
