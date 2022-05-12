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

@file:Suppress("DEPRECATION", "UnstableApiUsage")

package dev.vini2003.hammer.core.api.common.util

import dev.vini2003.hammer.core.api.common.util.extension.blue
import dev.vini2003.hammer.core.api.common.util.extension.gray
import dev.vini2003.hammer.core.api.common.util.extension.toLiteralText
import dev.vini2003.hammer.core.api.common.util.extension.toTranslatableText

object TextUtils {
	@JvmField
	val FLUID = "text.hammer.fluid".toTranslatableText().blue()
	
	@JvmField
	val EMPTY = "text.hammer.empty".toTranslatableText().gray()
	
	@JvmStatic
	fun percentage(a: Number, b: Number) = "${(a.toFloat() / b.toFloat() * 100.0F).toInt()}%".toLiteralText().gray()
}