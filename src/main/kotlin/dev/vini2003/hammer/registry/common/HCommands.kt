package dev.vini2003.hammer.registry.common

import dev.vini2003.hammer.common.util.extension.command
import dev.vini2003.hammer.common.util.extension.literal
import dev.vini2003.hammer.common.util.extension.runs
import dev.vini2003.hammer.testing.common.screenhandler.DebugScreenHandler
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback

class HCommands {
	companion object {
		@JvmStatic
		fun init() {
			CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
				dispatcher.command("hammer_common") {
					literal("debug") {
						literal("screen_handler") {
							runs {
								source.player.openHandledScreen(DebugScreenHandler.Factory)
							}
						}
					}
				}
			}
		}
	}
}