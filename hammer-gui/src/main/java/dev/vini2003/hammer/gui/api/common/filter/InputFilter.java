package dev.vini2003.hammer.gui.api.common.filter;

public interface InputFilter<T> {
	public String toString(T t);
	
	public T toValue(String text);
	
	public boolean accepts(String character, String text);
}
