package dev.vini2003.blade.common.util.extension

import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

fun MutableText.black() = formatted(Formatting.BLACK)
fun MutableText.darkBlue() = formatted(Formatting.DARK_BLUE)
fun MutableText.darkGreen() = formatted(Formatting.DARK_GREEN)
fun MutableText.darkAqua() = formatted(Formatting.DARK_AQUA)
fun MutableText.darkRed() = formatted(Formatting.DARK_RED)
fun MutableText.darkPurple() = formatted(Formatting.DARK_PURPLE)
fun MutableText.gold() = formatted(Formatting.GOLD)
fun MutableText.gray() = formatted(Formatting.GRAY)
fun MutableText.darkGray() = formatted(Formatting.DARK_GRAY)
fun MutableText.blue() = formatted(Formatting.BLUE)
fun MutableText.green() = formatted(Formatting.GREEN)
fun MutableText.aqua() = formatted(Formatting.AQUA)
fun MutableText.red() = formatted(Formatting.RED)
fun MutableText.lightPurple() = formatted(Formatting.LIGHT_PURPLE)
fun MutableText.yellow() = formatted(Formatting.YELLOW)
fun MutableText.white() = formatted(Formatting.WHITE)
fun MutableText.obfuscated() = formatted(Formatting.OBFUSCATED)
fun MutableText.bold() = formatted(Formatting.BOLD)
fun MutableText.strikethrough() = formatted(Formatting.STRIKETHROUGH)
fun MutableText.underline() = formatted(Formatting.UNDERLINE)
fun MutableText.italic() = formatted(Formatting.ITALIC)
fun MutableText.reset() = formatted(Formatting.RESET)

operator fun MutableText.plus(string: String) = append(string.toLiteralText().setStyle(style))
operator fun MutableText.plus(text: MutableText) = append(text)