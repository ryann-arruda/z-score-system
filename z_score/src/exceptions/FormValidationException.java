package exceptions;

import java.util.HashMap;
import java.util.Map;

public class FormValidationException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	private Map<String, String> errors = new HashMap<>();
	
	public FormValidationException(String msg) {
		super(msg);
	}
	
	public Map<String, String> getErros() {
		return new HashMap<>(errors);
	}
	
	public void addError(String fieldName, String errorMessage) {
		errors.put(fieldName, errorMessage);
	}
}
