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

package dev.vini2003.hammer.permission.impl.packet.sync

import dev.vini2003.hammer.core.api.common.packet.Packet
import dev.vini2003.hammer.core.api.common.util.serializer.UUIDSerializer
import dev.vini2003.hammer.permission.api.common.manager.RoleManager
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class SyncRolesPacket(val roles: Map<String, List<@Serializable(with = UUIDSerializer::class) UUID>>) : Packet<SyncRolesPacket>() {
	override fun receive(context: ClientContext) {
		roles.forEach { (name, holders) ->
			val role = RoleManager.getRoleByName(name)
			
			if (role != null) {
				role.holders.clear()
				
				holders.forEach { holder -> role.holders.add(holder) }
			}
		}
	}
}