package dev.vini2003.hammer.gui.registry.client

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW

object HGUIKeyBinds {
	fun init() = Unit
	
	@JvmField
	val SHOW_DETAILED_VALUES = KeyBindingHelper.registerKeyBinding(KeyBinding("key.hammer.show_detailed_values", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_SHIFT, "category.hammer"))
}