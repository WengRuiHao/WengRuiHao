package chat.exception;

public class ChatNotFoundException extends RuntimeException{

	public ChatNotFoundException() {
		super("查無此聊天室");
	}

	public ChatNotFoundException(String message) {
		super(message);
	}

}
