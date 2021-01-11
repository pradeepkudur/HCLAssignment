package com.hcl.creditdecisionengine.service;

import org.springframework.stereotype.Service;

import com.hcl.creditdecisionengine.repository.ApplicantDetails;

@Service
public interface CreditDecisionService {
	
	public boolean canCheckCreditScore(long ssnId);
	
	public int callCreditScoreEngine(long ssnId);

	public ApplicantDetails sanctionLoan(int creditScore, ApplicantDetails applicantDetails);
	

}
