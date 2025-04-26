package com.obras.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(InternalAuthenticationServiceException.class)
	public ResponseEntity<Object> handleInternalAuthenticationService(InternalAuthenticationServiceException ex, HttpServletRequest request) {
		ExceptionBody body = ExceptionBody.builder()
				.status(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.name())
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
		List<String> errors = ex.getBindingResult().getAllErrors().stream().map(error -> {
			if (error instanceof FieldError) {
				return ((FieldError) error).getField() + ": " + error.getDefaultMessage();
			} else {
				return error.getObjectName() + ": " + error.getDefaultMessage();
			}
		}).collect(Collectors.toList());

		ExceptionBody body = ExceptionBody.builder()
				.status(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.name())
				.message("Validation errors occurred")
				.path(request.getRequestURI())
				.errors(errors)
				.build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Object> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
		ExceptionBody body = ExceptionBody.builder()
				.status(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.name())
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAllExceptions(Exception ex, HttpServletRequest request) {
		ExceptionBody body = ExceptionBody.builder()
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.error(HttpStatus.INTERNAL_SERVER_ERROR.name())
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
	}
	
	@RestControllerAdvice
	public class GlobalExceptionHandler {

	    @ExceptionHandler(ConstraintViolationException.class)
	    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
	        List<String> errors = ex.getConstraintViolations().stream()
	            .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
	            .collect(Collectors.toList());
	        
	        return ResponseEntity.badRequest()
	            .body(new ErrorResponse("Erro de validação", errors));
	    }
	}

}