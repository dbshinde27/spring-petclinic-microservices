package org.springframework.samples.petclinic.global;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class CustomErrorResponse {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private LocalDateTime timestamp;

	private int status;

	private String message;

	private Object errors;

	private String path;

	public CustomErrorResponse(LocalDateTime timestamp, int status, String message, Object errors, String path) {
		super();
		this.timestamp = timestamp;
		this.status = status;
		this.message = message;
		this.errors = errors;
		this.path = path;
	}

	public CustomErrorResponse() {

	}

}
