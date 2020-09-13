package com.github.vini2003.blade.testing.kotlin

import com.github.vini2003.blade.common.handler.BaseScreenHandler
import net.minecraft.entity.player.PlayerEntity

class DebugScreenHandler(syncId: Int, player: PlayerEntity) : BaseScreenHandler(DebugContainers.DEBUG_HANDLER, syncId, player) {
	override fun initialize(width: Int, height: Int) {

		panel {
			val panel = this

			position(48F, 48F)
			size(96F, 96F)

			button {
				size(18F, 18F)

				floatTopLeftIn(panel, 1F, 1F)
			}

			button {
				size(18F, 18F)

				floatMiddleLeftIn(panel, 1F, 1F)
			}

			button {
				size(18F, 18F)

				floatBottomLeftIn(panel, 1F, 1F)
			}

			button {
				size(18F, 18F)

				floatMiddleBottomIn(panel, 1F, 1F)
			}

			button {
				size(18F, 18F)

				floatBottomRightIn(panel, 1F, 1F)
			}

			button {
				size(18F, 18F)

				floatMiddleRightIn(panel, 1F, 1F)
			}

			button {
				size(18F, 18F)

				floatTopRightIn(panel, 1F, 1F)
			}

			button {
				size(18F, 18F)

				floatMiddleTopIn(panel, 1F, 1F)
			}

			button {
				size(18F, 18F)

				floatTopLeftOut(panel, 1F, 1F)
			}

			button {
				size(18F, 18F)

				floatMiddleLeftOut(panel, 1F, 1F)
			}

			button {
				size(18F, 18F)

				floatBottomLeftOut(panel, 1F, 1F)
			}

			button {
				size(18F, 18F)

				floatMiddleBottomOut(panel, 1F, 1F)
			}

			button {
				size(18F, 18F)

				floatBottomRightOut(panel, 1F, 1F)
			}

			button {
				size(18F, 18F)

				floatMiddleRightOut(panel, 1F, 1F)
			}

			button {
				size(18F, 18F)

				floatTopRightOut(panel, 1F, 1F)
			}

			button {
				size(18F, 18F)

				floatMiddleTopOut(panel, 1F, 1F)
			}
		}
	}

	override fun canUse(player: PlayerEntity?): Boolean = true
}