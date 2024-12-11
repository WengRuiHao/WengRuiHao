package chat.exception;

public class PasswordInvalidException extends CertException {
	
	public PasswordInvalidException() {
		super("密碼無效");
	}
	
	public PasswordInvalidException(String message) {
		super(message);
	}
	
}
