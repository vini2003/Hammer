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

package dev.vini2003.hammer.core.api.common.util.extension

import net.minecraft.text.MutableText
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
fun MutableText.underlined() = formatted(Formatting.UNDERLINE)
fun MutableText.italic() = formatted(Formatting.ITALIC)
fun MutableText.reset() = formatted(Formatting.RESET)

operator fun MutableText.plus(string: String) = append(string.toLiteralText().setStyle(style))
operator fun MutableText.plus(text: MutableText) = append(text)