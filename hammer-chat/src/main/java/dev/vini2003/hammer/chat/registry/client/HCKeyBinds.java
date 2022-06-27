package dev.vini2003.hammer.chat.registry.client;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class HCKeyBinds {
	public static final KeyBinding SHOW_CHAT = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.hammer.show_chat", InputUtil.Type.KEYSYM, InputUtil.GLFW_KEY_P, "key.categories.hammer"));
	
	public static void init() {
	
	}
}
