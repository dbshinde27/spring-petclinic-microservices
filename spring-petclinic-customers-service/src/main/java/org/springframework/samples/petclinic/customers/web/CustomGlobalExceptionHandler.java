package org.springframework.samples.petclinic.customers.web;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> resourceNotFoundException(Exception ex, WebRequest request) throws IOException {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", new Date());
		body.put("status", HttpStatus.NOT_FOUND.value());
		body.put("message", ex.getMessage());
		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);

	}

	// error handle for @Valid
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", new Date());
		body.put("status", status.value());

		// Get all fields errors
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		body.put("errors", errors);

		return new ResponseEntity<>(body, headers, status);

	}

}
