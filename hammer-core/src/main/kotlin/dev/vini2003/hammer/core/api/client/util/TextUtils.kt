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

package dev.vini2003.hammer.core.api.client.util

import dev.vini2003.hammer.core.api.client.util.extension.capitalizeWords
import dev.vini2003.hammer.core.api.client.util.extension.height
import dev.vini2003.hammer.core.api.client.util.extension.width
import dev.vini2003.hammer.core.api.common.util.extension.blue
import dev.vini2003.hammer.core.api.common.util.extension.italic
import dev.vini2003.hammer.core.api.common.util.extension.toLiteralText
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object TextUtils {
	@JvmStatic
	fun width(text: Text) = text.width
	
	@JvmStatic
	fun height(text: Text) = text.height
	
	@JvmStatic
	fun getIdMod(id: Identifier): MutableText {
		val loader = FabricLoader.getInstance()
		val mod = loader.getModContainer(id.namespace)
		
		if (mod.isPresent) {
			val mod = mod.get()
			
			return mod.metadata.name.toLiteralText().blue().italic()
		} else {
			return id.namespace.replace("_", " ").replace("-", " ").capitalizeWords().toLiteralText().blue().italic()
		}
	}
}