package dev.vini2003.hammer.serialization.api.common.exception;

public class DeserializerException extends RuntimeException{
	public DeserializerException() {
	}
	
	public DeserializerException(String message) {
		super(message);
	}
	
	public DeserializerException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DeserializerException(Throwable cause) {
		super(cause);
	}
	
	public DeserializerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
