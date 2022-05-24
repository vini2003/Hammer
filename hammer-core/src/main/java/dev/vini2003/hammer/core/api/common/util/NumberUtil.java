package dev.vini2003.hammer.core.api.common.util;

public class NumberUtil {
	private static final String[] UNITS = new String[] { "", "k", "M", "G", "T", "P", "E", "Z", "Y" };
	
	public static String getUnit(int exponent) {
		if (exponent < 0) {
			return "";
		} else {
			if (exponent < 0 || exponent >= UNITS.length) {
				return "∞";
			} else {
				return UNITS[exponent];
			}
		}
	}
	
	public static int getExponent(Number number) {
		var num = number.doubleValue();
		
		var exponent = 0;
		
		while (num >= 1000.0) {
			num /= 1000.0;
			
			exponent += 1;
		}
		
		return exponent;
	}
	
	public static String getPrettyString(Number number, String unit) {
		return String.format("%,d%s", number, unit);
	}
	
	public static String getPrettyShortenedString(Number number, String unit) {
		var exponent = getExponent(number);
		var symbol = getUnit(exponent);
		
		var shortenedNumber = number.doubleValue();
		
		while (exponent > 0) {
			--exponent;
			
			shortenedNumber /= 1000;
		}
		
		return String.format("%,.2f%s%s", shortenedNumber, symbol, unit);
	}
}
