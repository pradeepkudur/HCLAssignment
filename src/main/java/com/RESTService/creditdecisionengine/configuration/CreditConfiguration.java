package com.RESTService.creditdecisionengine.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Component
@ConfigurationProperties("credit-configuration")
public class CreditConfiguration {
	
	private int minimumCreditScore;
	
	private int daysNotAllowedToCheckCreditScore;

}
