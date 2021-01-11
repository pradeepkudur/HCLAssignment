package com.hcl.creditdecisionengine;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

import java.util.List;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Matchers.anyLong;

import com.hcl.creditdecisionengine.repository.ApplicantRepository;
import com.hcl.creditdecisionengine.service.CreditDecisionService;
import com.hcl.creditdecisionengine.service.CreditDecisionServiceImpl;

@SpringBootTest
class CreditScoreEngineTest {
	
	private static final int expectedCreditScore = 701;
	

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Mock
    private RestTemplate restTemplate;
	
	@InjectMocks
	private CreditDecisionServiceImpl creditDecisionServiceImpl;

	
	@Test
	public void mockCallCreditScoreEngine() {

		ResponseEntity<Integer> myresponseEntity = new ResponseEntity<>(expectedCreditScore, HttpStatus.ACCEPTED);

		when(restTemplate.getForEntity("http://creditenginehost:port/CreditScore/1", Integer.class)).thenReturn(myresponseEntity);
		
		int actualScore = creditDecisionServiceImpl.callCreditScoreEngine(1);
		
		assertEquals(expectedCreditScore,actualScore);

	}

}
