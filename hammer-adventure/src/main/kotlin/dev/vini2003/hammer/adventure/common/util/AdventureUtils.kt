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

package dev.vini2003.hammer.adventure.common.util

import net.kyori.adventure.platform.fabric.FabricServerAudiences
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.minecraft.server.MinecraftServer
import net.minecraft.text.MutableText
import net.minecraft.text.Text

object AdventureUtils {
	@JvmStatic
	fun createAudience(server: MinecraftServer): FabricServerAudiences {
		return FabricServerAudiences.of(server)
	}
	
	@JvmStatic
	fun formatWithColorValuesToComponent(string: String): Component {
		return LegacyComponentSerializer.legacyAmpersand().deserialize(string).toBuilder().build()
	}
	
	@JvmStatic
	fun formatWithColorValuesToText(string: String): MutableText {
		return convertComponentToText(formatWithColorValuesToComponent(string))
	}
	
	@JvmStatic
	fun convertComponentToText(component: Component): MutableText {
		return Text.Serializer.fromJson(GsonComponentSerializer.gson().serializeToTree(component))!!
	}
	
	@JvmStatic
	fun convertTextToComponent(text: Text): Component {
		return GsonComponentSerializer.gson().deserializeFromTree(Text.Serializer.toJsonTree(text))
	}
}