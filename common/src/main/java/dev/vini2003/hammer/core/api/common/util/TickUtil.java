package dev.vini2003.hammer.core.api.common.util;

public class TickUtil {
	public static int ofSeconds(int seconds) {
		return seconds * 20;
	}
	
	public static int ofMinutes(int minutes) {
		return ofSeconds(minutes * 60);
	}
	
	public static int ofHours(int hours) {
		return ofMinutes(hours * 60);
	}
	
	public static int ofDays(int days) {
		return ofHours(days * 24);
	}
	
	public static int ofWeeks(int weeks) {
		return ofDays(weeks * 7);
	}
	
	public static int ofMonths(int months) {
		return ofDays(months * 30);
	}
	
	public static int ofYears(int years) {
		return ofDays(years * 365);
	}
}
