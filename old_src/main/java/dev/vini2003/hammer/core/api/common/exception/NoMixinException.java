package dev.vini2003.hammer.core.api.common.exception;

public class NoMixinException extends UnsupportedOperationException {
	public NoMixinException() {
		super("This method should have been overridden by a Mixin!");
	}
}
