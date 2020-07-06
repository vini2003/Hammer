package dev.vini2003.hammer.common.util.extension

import com.mojang.brigadier.StringReader
import net.minecraft.nbt.StringNbtReader
import net.minecraft.text.LiteralText
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier
import java.util.*

fun String.toLiteralText() = LiteralText(this)

fun String.toTranslatableText() = TranslatableText(this)

fun String.toNbt() = StringNbtReader(StringReader(this)).parseCompound()

fun String.toIdentifier() = Identifier(this)

fun String.toUuid(): UUID = UUID.fromString(this)