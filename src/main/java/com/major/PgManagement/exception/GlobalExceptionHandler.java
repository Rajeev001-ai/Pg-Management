package com.major.pgmanagement.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleRuntimeException(RuntimeException ex, Model model) {
		model.addAttribute("errorMessage", ex.getMessage());
		return "error/500";
	}

	@ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleValidationException(Exception ex, Model model) {
		model.addAttribute("errorMessage", "Please check the form values and try again.");
		return "error/500";
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleNotFound(HttpServletRequest request, Model model) {
		model.addAttribute("path", request.getRequestURI());
		return "error/404";
	}
}
