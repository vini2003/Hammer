/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 vini2003
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
