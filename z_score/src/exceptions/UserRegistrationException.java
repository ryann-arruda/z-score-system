package exceptions;

public class UserRegistrationException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public UserRegistrationException(String msg) {
		super(msg);
	}
}
