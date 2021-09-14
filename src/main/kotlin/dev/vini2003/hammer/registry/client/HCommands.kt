package dev.vini2003.hammer.registry.client

import dev.vini2003.hammer.client.util.Instances
import dev.vini2003.hammer.common.util.extension.literal
import dev.vini2003.hammer.common.util.extension.runs
import dev.vini2003.hammer.testing.client.screen.DebugScreen
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager

class HCommands {
	companion object {
		@JvmStatic
		fun init() {
			ClientCommandManager.DISPATCHER.register(
				literal("hammer_client") {
					literal("debug") {
						literal("screen") {
							runs {
								Instances.client.send {
									Instances.client.setScreen(DebugScreen())
								}
							}
						}
					}
				}
			)
		}
	}
}