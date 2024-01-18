package dev.vini2003.hammer.core.api.common.exception;

public class NoPlatformImplementationException extends UnsupportedOperationException {
	public NoPlatformImplementationException() {
		super("This method should have been overridden by a platform implementation!");
	}
}
