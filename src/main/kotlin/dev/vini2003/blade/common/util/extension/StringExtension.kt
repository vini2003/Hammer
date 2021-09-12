package dev.vini2003.blade.common.util.extension

import net.minecraft.text.LiteralText
import net.minecraft.text.TranslatableText

fun String.toLiteralText() = LiteralText(this)

fun String.toTranslatableText() = TranslatableText(this)
