package com.hcl.creditdecisionengine.controller;



import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.hcl.creditdecisionengine.repository.ApplicantDetails;
import com.hcl.creditdecisionengine.service.InvokeCreditDecisionService;






@RestController
public class CreditDecisionController {
	
	@Autowired
	private InvokeCreditDecisionService invokeCreditDecisionService;
	
	
	@PostMapping("/creditDecision")
	public ApplicantDetails getSanctionedLoanAmount(@Valid @RequestBody ApplicantDetails applicantDetails ){
		return invokeCreditDecisionService.invokeService(applicantDetails);
	}

}
	