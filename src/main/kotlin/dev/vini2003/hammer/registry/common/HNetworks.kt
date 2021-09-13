package dev.vini2003.hammer.registry.common

import dev.vini2003.hammer.common.screen.handler.BaseScreenHandler
import dev.vini2003.hammer.common.util.Networks
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.PacketByteBuf

class HNetworks {
	companion object {
		@JvmStatic
		fun init() {
			ServerPlayNetworking.registerGlobalReceiver(Networks.WidgetUpdate) { server, _, _, buf, _ ->
				val syncId = buf.readInt()
				val id = buf.readIdentifier()
				
				buf.retain()
				
				server.execute {
					server!!.playerManager.playerList.forEach {
						if (it.currentScreenHandler.syncId == syncId && it.currentScreenHandler is BaseScreenHandler) {
							(it.currentScreenHandler as BaseScreenHandler).handlePacket(id, PacketByteBuf(buf.copy()))
						}
					}
				}
			}
			
			ServerPlayNetworking.registerGlobalReceiver(Networks.Initialize) { server, _, _, buf, _ ->
				val syncId = buf.readInt()
				val width = buf.readInt()
				val height = buf.readInt()
				
				buf.retain()
				
				server.execute {
					server.playerManager.playerList.forEach { it ->
						if (it.currentScreenHandler.syncId == syncId && it.currentScreenHandler is BaseScreenHandler) {
							(it.currentScreenHandler as BaseScreenHandler).also {
								it.slots.clear()
								it.widgets.clear()
								it.initialize(width, height)
							}
						}
					}
				}
			}
		}
	}
}