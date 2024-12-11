package chat.exception;

//自定義非授權例外
public class UnauthorizedException extends Exception{

	public UnauthorizedException(String message) {
		super(message);
	}

}
