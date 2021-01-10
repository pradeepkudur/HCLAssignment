package com.RESTService.creditdecisionengine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class CreditCheckException extends RuntimeException {

	public CreditCheckException(String string) {
		super(string);
	}

}
