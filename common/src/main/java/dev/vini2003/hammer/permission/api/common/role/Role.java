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

package dev.vini2003.hammer.permission.api.common.role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class Role {
	private String name;
	private String prefix = null;
	
	private int prefixWeight = -1;
	private int prefixColor = -1;
	
	private final Collection<UUID> holders = new ArrayList<>();
	
	public Role(String name) {
		this.name = name;
	}
	
	public Role(String name, String prefix, int prefixWeight, int prefixColor) {
		this.name = name;
		this.prefix = prefix;
		
		this.prefixWeight = prefixWeight;
		this.prefixColor = prefixColor;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public int getPrefixWeight() {
		return prefixWeight;
	}
	
	public int getPrefixColor() {
		return prefixColor;
	}
	
	public Collection<UUID> getHolders() {
		return holders;
	}
}
