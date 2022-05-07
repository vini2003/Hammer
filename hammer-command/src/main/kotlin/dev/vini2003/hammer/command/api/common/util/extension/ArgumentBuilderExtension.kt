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

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.*
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext

fun <S> CommandDispatcher<S>.command(name: String, receiver: LiteralArgumentBuilder<S>.() -> Unit): LiteralArgumentBuilder<S>? {
	val literal = LiteralArgumentBuilder.literal<S>(name)
	literal.apply(receiver)
	register(literal)
	return literal
}

fun <S> ArgumentBuilder<S, *>.execute(receiver: CommandContext<S>.() -> Unit): ArgumentBuilder<S, *>? {
	executes { context -> receiver(context); Command.SINGLE_SUCCESS }
	return this
}

fun <S> literal(name: String, receiver: LiteralArgumentBuilder<S>.() -> Unit): LiteralArgumentBuilder<S> {
	val literal = LiteralArgumentBuilder.literal<S>(name)
	literal.apply(receiver)
	return literal
}

fun <S> ArgumentBuilder<S, *>.literal(name: String, receiver: LiteralArgumentBuilder<S>.() -> Unit): ArgumentBuilder<S, *> {
	val literal = LiteralArgumentBuilder.literal<S>(name)
	literal.apply(receiver)
	then(literal)
	return this
}

fun <S> word(name: String, receiver: RequiredArgumentBuilder<S, *>.() -> Unit): ArgumentBuilder<S, *> {
	val word = RequiredArgumentBuilder.argument<S, String>(name, StringArgumentType.word())
	word.apply(receiver)
	return word
}

fun <S> ArgumentBuilder<S, *>.word(name: String, receiver: RequiredArgumentBuilder<S, *>.() -> Unit): ArgumentBuilder<S, *> {
	val word = RequiredArgumentBuilder.argument<S, String>(name, StringArgumentType.word())
	word.apply(receiver)
	then(word)
	return this
}

fun <S> string(name: String, receiver: RequiredArgumentBuilder<S, *>.() -> Unit): ArgumentBuilder<S, *> {
	val string = RequiredArgumentBuilder.argument<S, String>(name, StringArgumentType.string())
	string.apply(receiver)
	return string
}

fun <S> ArgumentBuilder<S, *>.string(name: String, receiver: RequiredArgumentBuilder<S, *>.() -> Unit): ArgumentBuilder<S, *> {
	val string = RequiredArgumentBuilder.argument<S, String>(name, StringArgumentType.string())
	string.apply(receiver)
	then(string)
	return this
}

fun <S> greedyString(name: String, receiver: RequiredArgumentBuilder<S, *>.() -> Unit): ArgumentBuilder<S, *> {
	val greedyString = RequiredArgumentBuilder.argument<S, String>(name, StringArgumentType.greedyString())
	greedyString.apply(receiver)
	return greedyString
}

fun <S> ArgumentBuilder<S, *>.greedyString(name: String, receiver: RequiredArgumentBuilder<S, *>.() -> Unit): ArgumentBuilder<S, *> {
	val greedyString = RequiredArgumentBuilder.argument<S, String>(name, StringArgumentType.greedyString())
	greedyString.apply(receiver)
	then(greedyString)
	return this
}

fun <S> boolean(name: String, receiver: RequiredArgumentBuilder<S, *>.() -> Unit): ArgumentBuilder<S, *> {
	val boolean = RequiredArgumentBuilder.argument<S, Boolean>(name, BoolArgumentType.bool())
	boolean.apply(receiver)
	return boolean
}

fun <S> ArgumentBuilder<S, *>.boolean(name: String, receiver: RequiredArgumentBuilder<S, *>.() -> Unit): ArgumentBuilder<S, *> {
	val boolean = RequiredArgumentBuilder.argument<S, Boolean>(name, BoolArgumentType.bool())
	boolean.apply(receiver)
	then(boolean)
	return this
}

fun <S> double(name: String, receiver: RequiredArgumentBuilder<S, *>.() -> Unit, min: Double = Double.MIN_VALUE, max: Double = Double.MAX_VALUE): ArgumentBuilder<S, *> {
	val double = RequiredArgumentBuilder.argument<S, Double>(name, DoubleArgumentType.doubleArg(min, max))
	double.apply(receiver)
	return double
}

fun <S> ArgumentBuilder<S, *>.double(name: String, receiver: RequiredArgumentBuilder<S, *>.() -> Unit, min: Double = Double.MIN_VALUE, max: Double = Double.MAX_VALUE): ArgumentBuilder<S, *> {
	val double = RequiredArgumentBuilder.argument<S, Double>(name, DoubleArgumentType.doubleArg(min, max))
	double.apply(receiver)
	then(double)
	return this
}

fun <S> float(name: String, receiver: RequiredArgumentBuilder<S, *>.() -> Unit, min: Float = Float.MIN_VALUE, max: Float = Float.MAX_VALUE): ArgumentBuilder<S, *> {
	val float = RequiredArgumentBuilder.argument<S, Float>(name, FloatArgumentType.floatArg(min, max))
	float.apply(receiver)
	return float
}

fun <S> ArgumentBuilder<S, *>.float(name: String, receiver: RequiredArgumentBuilder<S, *>.() -> Unit, min: Float = Float.MIN_VALUE, max: Float = Float.MAX_VALUE): ArgumentBuilder<S, *> {
	val float = RequiredArgumentBuilder.argument<S, Float>(name, FloatArgumentType.floatArg(min, max))
	float.apply(receiver)
	then(float)
	return this
}

fun <S> int(name: String, receiver: RequiredArgumentBuilder<S, *>.() -> Unit, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): ArgumentBuilder<S, *> {
	val int = RequiredArgumentBuilder.argument<S, Int>(name, IntegerArgumentType.integer(min, max))
	int.apply(receiver)
	return int
}

fun <S> ArgumentBuilder<S, *>.int(name: String, receiver: RequiredArgumentBuilder<S, *>.() -> Unit, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): ArgumentBuilder<S, *> {
	val int = RequiredArgumentBuilder.argument<S, Int>(name, IntegerArgumentType.integer(min, max))
	int.apply(receiver)
	then(int)
	return this
}

fun <S> long(name: String, receiver: RequiredArgumentBuilder<S, *>.() -> Unit, min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE): ArgumentBuilder<S, *> {
	val long = RequiredArgumentBuilder.argument<S, Long>(name, LongArgumentType.longArg(min, max))
	long.apply(receiver)
	return long
}

fun <S> ArgumentBuilder<S, *>.long(name: String, receiver: RequiredArgumentBuilder<S, *>.() -> Unit, min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE): ArgumentBuilder<S, *> {
	val long = RequiredArgumentBuilder.argument<S, Long>(name, LongArgumentType.longArg(min, max))
	long.apply(receiver)
	then(long)
	return this
}

fun <S, E, T : ArgumentType<E>> argument(name: String, type: T, receiver: RequiredArgumentBuilder<S, *>.() -> Unit): ArgumentBuilder<S, *> {
	val builder = RequiredArgumentBuilder.argument<S, E>(name, type)
	builder.apply(receiver)
	return builder
}

fun <S, E, T : ArgumentType<E>> ArgumentBuilder<S, *>.argument(name: String, type: T, receiver: RequiredArgumentBuilder<S, *>.() -> Unit): ArgumentBuilder<S, *> {
	val builder = RequiredArgumentBuilder.argument<S, E>(name, type)
	builder.apply(receiver)
	then(builder)
	return this
}