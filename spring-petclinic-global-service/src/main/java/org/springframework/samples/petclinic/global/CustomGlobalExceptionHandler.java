package org.springframework.samples.petclinic.global;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
	public ResponseEntity<CustomErrorResponse> resourceNotFoundException(Exception ex, WebRequest request)
			throws IOException {

		CustomErrorResponse response = new CustomErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(),
				"ERROR", null, request.getDescription(false));
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	// error handle for @Valid
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		CustomErrorResponse response = new CustomErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
				"ERROR", null, request.getDescription(false));
		// Get all fields errors
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		response.setErrors(errors);

		return new ResponseEntity<>(response, headers, status);

	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<CustomErrorResponse> globalExceptionHandler(Exception ex, WebRequest request) {
		CustomErrorResponse response = new CustomErrorResponse(LocalDateTime.now(),
				HttpStatus.INTERNAL_SERVER_ERROR.value(), "ERROR", null, request.getDescription(false));
		return new ResponseEntity<CustomErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
