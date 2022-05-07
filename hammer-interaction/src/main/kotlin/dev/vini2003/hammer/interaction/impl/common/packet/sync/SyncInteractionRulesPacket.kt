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

package dev.vini2003.hammer.interaction.impl.common.packet.sync

import dev.vini2003.hammer.core.api.common.packet.Packet
import dev.vini2003.hammer.core.api.common.util.serializer.UnsupportedSerializer
import dev.vini2003.hammer.interaction.api.common.interaction.InteractionRule
import dev.vini2003.hammer.interaction.api.common.manager.InteractionRuleManager
import kotlinx.serialization.Serializable

@Serializable
data class SyncInteractionRulesPacket(val rules: List<InteractionRule<@Serializable(with = UnsupportedSerializer::class) Any>>) : Packet<SyncInteractionRulesPacket>() {
	// Ignore is used to differentiate the two constructors, due to having the same JVM signature after type erasure.
	constructor(rules: List<InteractionRule<*>>, ignore: Byte = 0) : this(rules.map { key -> key as InteractionRule<Any> })
	
	override fun receive(context: ClientContext) {
		context.client.executeTask {
			InteractionRuleManager.clear(context.client.player!!)
			
			rules.forEach { rule -> InteractionRuleManager.add(context.client.player!!, rule) }
		}
	}
}