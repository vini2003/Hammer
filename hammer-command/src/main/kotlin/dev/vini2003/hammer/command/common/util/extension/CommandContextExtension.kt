package dev.vini2003.hammer.command.common.util.extension

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