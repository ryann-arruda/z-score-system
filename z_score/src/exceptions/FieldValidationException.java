package exceptions;

import java.util.HashMap;
import java.util.Map;

public class FieldValidationException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	private Map<String, String> errors = new HashMap<>();
	
	public FieldValidationException(String msg) {
		super(msg);
	}
	
	public Map<String, String> getErrors() {
		return new HashMap<>(errors);
	}
	
	public void addError(String fieldName, String errorMessage) {
		errors.put(fieldName, errorMessage);
	}
}
