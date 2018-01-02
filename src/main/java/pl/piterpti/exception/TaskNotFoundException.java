package pl.piterpti.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Task not found")
public class TaskNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public TaskNotFoundException(String messsage) {
		super(messsage);
	}
}
