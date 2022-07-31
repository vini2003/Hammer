package dev.vini2003.hammer.serialization.api.common.exception;

public class SerializerException extends RuntimeException{
	public SerializerException() {
	}
	
	public SerializerException(String message) {
		super(message);
	}
	
	public SerializerException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public SerializerException(Throwable cause) {
		super(cause);
	}
	
	public SerializerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
