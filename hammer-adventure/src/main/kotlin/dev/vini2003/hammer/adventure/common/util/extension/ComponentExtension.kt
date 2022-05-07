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