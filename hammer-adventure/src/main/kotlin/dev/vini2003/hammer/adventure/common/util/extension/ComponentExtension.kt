package dev.vini2003.hammer.adventure.common.util.extension

import dev.vini2003.hammer.adventure.common.util.AdventureUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

fun Component.toText() = AdventureUtils.convertComponentToText(this)

fun Component.black() = color(NamedTextColor.BLACK)
fun Component.darkBlue() = color(NamedTextColor.DARK_BLUE)
fun Component.darkGreen() = color(NamedTextColor.DARK_GREEN)
fun Component.darkAqua() = color(NamedTextColor.DARK_AQUA)
fun Component.darkRed() = color(NamedTextColor.DARK_RED)
fun Component.darkPurple() = color(NamedTextColor.DARK_PURPLE)
fun Component.gold() = color(NamedTextColor.GOLD)
fun Component.gray() = color(NamedTextColor.GRAY)
fun Component.darkGray() = color(NamedTextColor.DARK_GRAY)
fun Component.blue() = color(NamedTextColor.BLUE)
fun Component.green() = color(NamedTextColor.GREEN)
fun Component.aqua() = color(NamedTextColor.AQUA)
fun Component.red() = color(NamedTextColor.RED)
fun Component.lightPurple() = color(NamedTextColor.LIGHT_PURPLE)
fun Component.yellow() = color(NamedTextColor.YELLOW)
fun Component.white() = color(NamedTextColor.WHITE)

fun Component.obfuscated() = decorate(TextDecoration.OBFUSCATED)
fun Component.bold() = decorate(TextDecoration.BOLD)
fun Component.strikethrough() = decorate(TextDecoration.STRIKETHROUGH)
fun Component.underlined() = decorate(TextDecoration.UNDERLINED)
fun Component.italic() = decorate(TextDecoration.ITALIC)

operator fun Component.plus(component: Component) = append(component)
operator fun Component.plus(string: String) = append(Component.text(string).style(style()))