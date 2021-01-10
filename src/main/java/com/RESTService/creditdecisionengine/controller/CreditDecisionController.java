package com.RESTService.creditdecisionengine.controller;



import java.time.LocalDateTime;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.RESTService.creditdecisionengine.configuration.CreditConfiguration;
import com.RESTService.creditdecisionengine.exception.CreditCheckException;
import com.RESTService.creditdecisionengine.repository.ApplicantDetails;
import com.RESTService.creditdecisionengine.repository.ApplicantRepository;






@RestController
public class CreditDecisionController {
	
	@Autowired
	private ApplicantRepository applicantRepository;
	
	@Autowired
	private CreditConfiguration configuration;
	
	@GetMapping("/get")
	public ApplicantDetails getm() {
		ApplicantDetails app = new ApplicantDetails();
		app.setSsnID(1);
		app.setLoan_amount(1000);
		app.setCurrent_income(10000);
		return app;
	}
	
	/*
	@GetMapping("/applicant/{ssnId}/{loanAmount}/{currentIncome}/getSanctionedLoanAmount")
	public ApplicantDetails getSanctionedLoanAmount2(@PathVariable long ssnId, @PathVariable long loanAmount, @PathVariable long currentIncome ){
		
		Optional<ApplicantDetails> applicationDetails = applicantRepository.findBySsnId(ssnId,configuration.getDaysNotAllowedToCheckCreditScore());
		
		if(!applicationDetails.isPresent()) {
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Integer> responseEntity = restTemplate.getForEntity(
					"http://creditenginehost:port/getCreditScore/for/{ssnid}", Integer.class,
					ssnId);
			int creditScore = responseEntity.getBody();
			ApplicantDetails appicantDetails = new ApplicantDetails(ssnId, loanAmount, currentIncome, 0L, LocalDateTime.now(), "Rejected");
			
			if(creditScore > configuration.getMinimumCreditScore()) {
				long sanctionedLoan = currentIncome/2;
				appicantDetails.setSanctionedAmount(sanctionedLoan);
				appicantDetails.setLoanStatus("Approved");
			}
			
			applicantRepository.save(appicantDetails);
		} 
		else {
			LocalDateTime lastApplied = applicationDetails.get().getAppliedDate();
			throw new CannotCheckCreditScoreException("ssnId-"+ssnId+" because last time you checked on "+lastApplied+" you are allowed to check after "+lastApplied.plusDays(30));
		}
		
		
		return applicationDetails.get();
	}   */
	
	
	@PostMapping("/applicant/getSanctionedLoanAmount")
	public ApplicantDetails getSanctionedLoanAmount(@Valid @RequestBody ApplicantDetails appicantDetails ){
		
		long ssnId = appicantDetails.getSsnID();
		Optional<ApplicantDetails> applicationDetails = applicantRepository.findBySsnId(ssnId, configuration.getDaysNotAllowedToCheckCreditScore());
		
		/**** if applicant has not checked the credit score in last 30 days then check credit score and calculate the sanctioned loan else service unavailable****/
		if(!applicationDetails.isPresent()) {
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Integer> responseEntity = restTemplate.getForEntity(
					"http://creditenginehost:port/getCreditScore/for/{ssnid}", Integer.class,
					ssnId);
			int creditScore = responseEntity.getBody();
			
			/**** if credit score is more than minimum required credit score approve loan else reject ****/
			if(creditScore > configuration.getMinimumCreditScore()) {
				long sanctionedLoan = appicantDetails.getCurrent_income()/2;
				long loanAmount = appicantDetails.getLoan_amount();
				
				/**** If the required loan amount is less than sanctioned loan then set required loan amount else set sanctioned loan   ***/
				if(sanctionedLoan > loanAmount) {
					appicantDetails.setSanctionedAmount(loanAmount);
				}
				else {
					appicantDetails.setSanctionedAmount(sanctionedLoan);
				}
				/********/
				
				appicantDetails.setLoanStatus("Approved");
			}
			else {
				appicantDetails.setSanctionedAmount(0L);
				appicantDetails.setLoanStatus("Rejected");
			}
			/********/
			
			appicantDetails.setAppliedDate(LocalDateTime.now());
			applicantRepository.save(appicantDetails);
		} 
		else {
			LocalDateTime lastApplied = applicationDetails.get().getAppliedDate();
			throw new CreditCheckException("credit score for ssnId-"+ssnId+" has already checked on "+lastApplied+", you are allowed to check after "+lastApplied.plusDays(30));
		}
		/********/
		
		
		return appicantDetails;
	}

}
	