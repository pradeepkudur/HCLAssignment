package com.hcl.creditdecisionengine.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.hcl.creditdecisionengine.configuration.CreditConfiguration;
import com.hcl.creditdecisionengine.repository.ApplicantDetails;
import com.hcl.creditdecisionengine.repository.ApplicantRepository;

@Repository
public class CreditDecisionServiceImpl implements CreditDecisionService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CreditDecisionServiceImpl.class);

	@Autowired
	private ApplicantRepository applicantRepository;
	
	@Autowired
	private CreditConfiguration configuration;
	
	@Autowired
    private RestTemplate restTemplate;

	@Override
	public boolean canCheckCreditScore(long ssnId) {
		Optional<ApplicantDetails> applicationDetails = applicantRepository.findBySsnId(ssnId);
		if (applicationDetails.isPresent()) {
			LocalDateTime appliedDate = applicationDetails.get().getAppliedDate();
			LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(configuration.getDaysNotAllowedToCheckCreditScore());
			if (appliedDate.isBefore(thirtyDaysAgo)) {
				return true;
	        }
			return false;
		}
		return true;
	}

	@Override
	public int callCreditScoreEngine(long ssnId) {
		ResponseEntity<Integer> responseEntity = restTemplate
				.getForEntity("http://creditenginehost:port/CreditScore/"+ssnId, Integer.class);
		return responseEntity.getBody();
	}

	@Override
	public ApplicantDetails sanctionLoan(int creditScore, ApplicantDetails applicantDetails) {
		if(creditScore > configuration.getMinimumCreditScore()) {
			LOGGER.info("Sanctioning loan for meeting credit requirement for SSNID" + applicantDetails.getSsnID());
			long sanctionedLoan = applicantDetails.getCurrentIncome()/2;
			long loanAmount = applicantDetails.getLoanAmount();
			
			if(sanctionedLoan > loanAmount) {
				applicantDetails.setSanctionedAmount(loanAmount);
			}
			else {
				applicantDetails.setSanctionedAmount(sanctionedLoan);
			}
			
			applicantDetails.setLoanStatus("Approved");
		}
		else {
			LOGGER.info("Rejecting the application for low credit score on SSNID" + applicantDetails.getSsnID());
			applicantDetails.setSanctionedAmount(0L);
			applicantDetails.setLoanStatus("Rejected");
		}
		
		applicantDetails.setAppliedDate(LocalDateTime.now());
		applicantRepository.save(applicantDetails);

		return applicantDetails;
	}


}
