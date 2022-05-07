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

package dev.vini2003.hammer.command.api.common.util.extension

import com.mojang.brigadier.arguments.*
import com.mojang.brigadier.context.CommandContext

fun <S> CommandContext<S>.getString(name: String): String {
	return StringArgumentType.getString(this, name)
}

fun <S> CommandContext<S>.getBoolean(name: String): Boolean {
	return BoolArgumentType.getBool(this, name)
}

fun <S> CommandContext<S>.getDouble(name: String): Double {
	return DoubleArgumentType.getDouble(this, name)
}

fun <S> CommandContext<S>.getFloat(name: String): Float {
	return FloatArgumentType.getFloat(this, name)
}

fun <S> CommandContext<S>.getInt(name: String): Int {
	return IntegerArgumentType.getInteger(this, name)
}

fun <S> CommandContext<S>.getLong(name: String): Long {
	return LongArgumentType.getLong(this, name)
}