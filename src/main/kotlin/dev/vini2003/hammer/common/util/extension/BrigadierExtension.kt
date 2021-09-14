package dev.vini2003.hammer.common.util.extension

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.*
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource

fun <S : CommandSource> CommandDispatcher<S>.command(name: String, block: LiteralArgumentBuilder<S>.() -> Unit): LiteralArgumentBuilder<S>? {
	val literal = LiteralArgumentBuilder.literal<S>(name)
	literal.apply(block)
	register(literal)
	return literal
}

fun <S : CommandSource> ArgumentBuilder<S, *>.runs(block: CommandContext<S>.() -> Unit): ArgumentBuilder<S, *>? {
	executes { block(it); Command.SINGLE_SUCCESS }
	return this
}

fun <S : CommandSource> literal(name: String, block: LiteralArgumentBuilder<S>.() -> Unit): LiteralArgumentBuilder<S> {
	val literal = LiteralArgumentBuilder.literal<S>(name)
	literal.apply(block)
	return literal
}

fun <S : CommandSource> ArgumentBuilder<S, *>.literal(name: String, block: LiteralArgumentBuilder<S>.() -> Unit): ArgumentBuilder<S, *> {
	val literal = LiteralArgumentBuilder.literal<S>(name)
	literal.apply(block)
	then(literal)
	return this
}

fun <S : CommandSource> word(name: String, block: RequiredArgumentBuilder<S, *>.() -> Unit): ArgumentBuilder<S, *> {
	val word = RequiredArgumentBuilder.argument<S, String>(name, StringArgumentType.word())
	word.apply(block)
	return word
}

fun <S : CommandSource> ArgumentBuilder<S, *>.word(name: String, block: RequiredArgumentBuilder<S, *>.() -> Unit): ArgumentBuilder<S, *> {
	val word = RequiredArgumentBuilder.argument<S, String>(name, StringArgumentType.word())
	word.apply(block)
	then(word)
	return this
}

fun <S : CommandSource> string(name: String, block: RequiredArgumentBuilder<S, *>.() -> Unit): ArgumentBuilder<S, *> {
	val string = RequiredArgumentBuilder.argument<S, String>(name, StringArgumentType.string())
	string.apply(block)
	return string
}

fun <S : CommandSource> ArgumentBuilder<S, *>.string(name: String, block: RequiredArgumentBuilder<S, *>.() -> Unit): ArgumentBuilder<S, *> {
	val string = RequiredArgumentBuilder.argument<S, String>(name, StringArgumentType.string())
	string.apply(block)
	then(string)
	return this
}

fun <S : CommandSource> greedyString(name: String, block: RequiredArgumentBuilder<S, *>.() -> Unit): ArgumentBuilder<S, *> {
	val greedyString = RequiredArgumentBuilder.argument<S, String>(name, StringArgumentType.greedyString())
	greedyString.apply(block)
	return greedyString
}

fun <S : CommandSource> ArgumentBuilder<S, *>.greedyString(name: String, block: RequiredArgumentBuilder<S, *>.() -> Unit): ArgumentBuilder<S, *> {
	val greedyString = RequiredArgumentBuilder.argument<S, String>(name, StringArgumentType.greedyString())
	greedyString.apply(block)
	then(greedyString)
	return this
}

fun <S : CommandSource> boolean(name: String, block: RequiredArgumentBuilder<S, *>.() -> Unit): ArgumentBuilder<S, *> {
	val boolean = RequiredArgumentBuilder.argument<S, Boolean>(name, BoolArgumentType.bool())
	boolean.apply(block)
	return boolean
}

fun <S : CommandSource> ArgumentBuilder<S, *>.boolean(name: String, block: RequiredArgumentBuilder<S, *>.() -> Unit): ArgumentBuilder<S, *> {
	val boolean = RequiredArgumentBuilder.argument<S, Boolean>(name, BoolArgumentType.bool())
	boolean.apply(block)
	then(boolean)
	return this
}

fun <S : CommandSource> double(name: String, block: RequiredArgumentBuilder<S, *>.() -> Unit, min: Double = Double.MIN_VALUE, max: Double = Double.MAX_VALUE): ArgumentBuilder<S, *> {
	val double = RequiredArgumentBuilder.argument<S, Double>(name, DoubleArgumentType.doubleArg(min, max))
	double.apply(block)
	return double
}

fun <S : CommandSource> ArgumentBuilder<S, *>.double(name: String, block: RequiredArgumentBuilder<S, *>.() -> Unit, min: Double = Double.MIN_VALUE, max: Double = Double.MAX_VALUE): ArgumentBuilder<S, *> {
	val double = RequiredArgumentBuilder.argument<S, Double>(name, DoubleArgumentType.doubleArg(min, max))
	double.apply(block)
	then(double)
	return this
}

fun <S : CommandSource> float(name: String, block: RequiredArgumentBuilder<S, *>.() -> Unit, min: Float = Float.MIN_VALUE, max: Float = Float.MAX_VALUE): ArgumentBuilder<S, *> {
	val float = RequiredArgumentBuilder.argument<S, Float>(name, FloatArgumentType.floatArg(min, max))
	float.apply(block)
	return float
}

fun <S : CommandSource> ArgumentBuilder<S, *>.float(name: String, block: RequiredArgumentBuilder<S, *>.() -> Unit, min: Float = Float.MIN_VALUE, max: Float = Float.MAX_VALUE): ArgumentBuilder<S, *> {
	val float = RequiredArgumentBuilder.argument<S, Float>(name, FloatArgumentType.floatArg(min, max))
	float.apply(block)
	then(float)
	return this
}

fun <S : CommandSource> integer(name: String, block: RequiredArgumentBuilder<S, *>.() -> Unit, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): ArgumentBuilder<S, *> {
	val integer = RequiredArgumentBuilder.argument<S, Int>(name, IntegerArgumentType.integer(min, max))
	integer.apply(block)
	return integer
}

fun <S : CommandSource> ArgumentBuilder<S, *>.integer(name: String, block: RequiredArgumentBuilder<S, *>.() -> Unit, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): ArgumentBuilder<S, *> {
	val integer = RequiredArgumentBuilder.argument<S, Int>(name, IntegerArgumentType.integer(min, max))
	integer.apply(block)
	then(integer)
	return this
}

fun <S : CommandSource> long(name: String, block: RequiredArgumentBuilder<S, *>.() -> Unit, min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE): ArgumentBuilder<S, *> {
	val long = RequiredArgumentBuilder.argument<S, Long>(name, LongArgumentType.longArg(min, max))
	long.apply(block)
	return long
}

fun <S : CommandSource> ArgumentBuilder<S, *>.long(name: String, block: RequiredArgumentBuilder<S, *>.() -> Unit, min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE): ArgumentBuilder<S, *> {
	val long = RequiredArgumentBuilder.argument<S, Long>(name, LongArgumentType.longArg(min, max))
	long.apply(block)
	then(long)
	return this
}

fun <S : CommandSource> CommandContext<S>.getString(name: String): String {
	return StringArgumentType.getString(this, name)
}

fun <S : CommandSource> CommandContext<S>.getBoolean(name: String): Boolean {
	return BoolArgumentType.getBool(this, name)
}

fun <S : CommandSource> CommandContext<S>.getDouble(name: String): Double {
	return DoubleArgumentType.getDouble(this, name)
}

fun <S : CommandSource> CommandContext<S>.getFloat(name: String): Float {
	return FloatArgumentType.getFloat(this, name)
}

fun <S : CommandSource> CommandContext<S>.getInt(name: String): Int {
	return IntegerArgumentType.getInteger(this, name)
}

fun <S : CommandSource> CommandContext<S>.getLong(name: String): Long {
	return LongArgumentType.getLong(this, name)
}