package dev.vini2003.hammer.core.api.common.serialization;

public class ParseException extends RuntimeException {
	public ParseException() {
		super();
	}
	
	public ParseException(String message) {
		super(message);
	}
	
	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ParseException(Throwable cause) {
		super(cause);
	}
	
	public ParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
