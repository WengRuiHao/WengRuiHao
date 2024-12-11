package chat.exception;

public class UserNotFoundException extends CertException {
	
	public UserNotFoundException() {
		super("查無使用者");
	}
	
	public UserNotFoundException(String message) {
		super(message);
	}
	
}
