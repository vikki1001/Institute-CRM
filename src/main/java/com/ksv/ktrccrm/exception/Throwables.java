package com.ksv.ktrccrm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*@ResponseStatus(code = HttpStatus.NOT_FOUND)*/
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User Not found...!")
public class Throwables extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public Throwables(String exc) {
		super(exc);
	}

	public static Object getRootCause() {
		return "error";
	}

}
