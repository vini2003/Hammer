package dev.vini2003.hammer.core.api.common.util;

import java.util.Collection;

public class CollectionUtil {
	public static <T> T getRandom(T[] array) {
		return array[(int) (Math.random() * array.length)];
	}
	
	public static <T> T getRandom(Collection<T> iterable) {
		int index = (int) (Math.random() * iterable.size());
		for (T t : iterable) {
			if (index == 0) {
				return t;
			}
			index--;
		}
		return null;
	}
}
