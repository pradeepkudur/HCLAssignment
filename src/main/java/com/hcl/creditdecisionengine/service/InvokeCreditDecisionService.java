package com.hcl.creditdecisionengine.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hcl.creditdecisionengine.exception.CreditCheckException;
import com.hcl.creditdecisionengine.repository.ApplicantDetails;

@Component
public class InvokeCreditDecisionService {
	
	@Autowired
	private CreditDecisionService creditDecisionService;
	
	public ApplicantDetails invokeService(ApplicantDetails applicantDetails) {
		long ssnId = applicantDetails.getSsnID();
		if(creditDecisionService.canCheckCreditScore(ssnId)) {
			int creditScore = creditDecisionService.callCreditScoreEngine(ssnId);
			return creditDecisionService.sanctionLoan(creditScore, applicantDetails);
		}
		else {
			LocalDateTime lastApplied = applicantDetails.getAppliedDate();
			throw new CreditCheckException("credit score for ssnId-"+ssnId+" has already checked on "+lastApplied+", you are allowed to check after "+lastApplied.plusDays(30));
		}
	}

}
