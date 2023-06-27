package dev.vini2003.hammer.core.api.common.serialization;

import org.jetbrains.annotations.Nullable;
import java.util.NoSuchElementException;

public record ParseResult<T>(
		@Nullable
		T value
) {
	public boolean ok() {
		return value != null;
	}
	
	public static <T> ParseResult<T> bad() {
		return new ParseResult<>(null);
	}
	
	public static <T> ParseResult<T> ok(T value) {
		return new ParseResult<>(value);
	}
	
	public T getOrThrow() {
		if (this.value == null) {
			throw new ParseException("No value present");
		} else {
			return this.value;
		}
	}
	
	public T getOrDefault(T value) {
		if (this.value == null) {
			return value;
		} else {
			return this.value;
		}
	}
}
