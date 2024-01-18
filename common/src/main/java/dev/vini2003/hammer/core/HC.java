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

package dev.vini2003.hammer.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.architectury.registry.registries.DeferredRegister;
import dev.vini2003.hammer.core.api.common.util.HammerUtil;
import dev.vini2003.hammer.core.impl.common.config.HCConfig;
import dev.vini2003.hammer.core.registry.common.HCArgumentTypes;
import dev.vini2003.hammer.core.registry.common.HCComponents;
import dev.vini2003.hammer.core.registry.common.HCEvents;
import dev.vini2003.hammer.core.registry.common.HCNetworking;
import dev.vini2003.hammer.persistence.api.common.config.Config;
import net.minecraft.block.Block;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApiStatus.Internal
public class HC {
	private static final String HAMMER_CHAT_MOD_INITIALIZER = "dev.vini2003.hammer.chat.HC";
	private static final String HAMMER_PERSISTENCE_MOD_INITIALIZER = "dev.vini2003.hammer.persistence.HP";
	private static final String HAMMER_GRAVITY_MOD_INITIALIZER = "dev.vini2003.hammer.gravity.HG";
	private static final String HAMMER_GUI_MOD_INITIALIZER = "dev.vini2003.hammer.gui.HGUI";
	private static final String HAMMER_PERMISSION_MOD_INITIALIZER = "dev.vini2003.hammer.permission.HP";
	private static final String HAMMER_PRESET_MOD_INITIALIZER = "dev.vini2003.hammer.preset.HP";
	private static final String HAMMER_UTIL_MOD_INITIALIZER = "dev.vini2003.hammer.util.HU";
	private static final String HAMMER_ZONE_MOD_INITIALIZER = "dev.vini2003.hammer.zone.HZ";
	
	public static final String BORDER_MODULE_ID = "border";
	public static final String CHAT_MODULE_ID = "chat";
	public static final String PERSISTENCE_MODULE_ID = "persistence";
	public static final String CORE_MODULE_ID = "core";
	public static final String GRAVITY_MODULE_ID = "gravity";
	public static final String GUI_MODULE_ID = "gui";
	public static final String PERMISSION_MODULE_ID = "permission";
	public static final String PRESET_MODULE_ID = "preset";
	public static final String UTIL_MODULE_ID = "util";
	public static final String ZONE_MODULE_ID = "zone";
	
	public static final String ID = "hammer";
	
	public static final HCConfig CONFIG = Config.getOrCreate(ID, HCConfig.class);
	
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	public static final Logger LOGGER = LoggerFactory.getLogger("Hammer");
	
	private static DeferredRegister<Item> ITEM_REGISTRY = null;
	private static DeferredRegister<Block> BLOCK_REGISTRY = null;
	private static DeferredRegister<ScreenHandlerType<?>> SCREEN_HANDLER_REGISTRY = null;
	private static DeferredRegister<ArgumentSerializer<?, ?>> ARGUMENT_TYPE_REGISTRY = null;
	
	public static Identifier id(String path) {
		return new Identifier(ID, path);
	}
	
	public static void onInitialize() {
		HCArgumentTypes.init();
		HCEvents.init();
		// HCItemGroups.init();
		HCNetworking.init();
		HCComponents.init();
		
		HC.LOGGER.info("Initialized [" + HC.CORE_MODULE_ID + "] module.");
		
		HammerUtil.initializeIfModuleEnabled(BORDER_MODULE_ID, HAMMER_CHAT_MOD_INITIALIZER);
		HammerUtil.initializeIfModuleEnabled(CHAT_MODULE_ID, HAMMER_CHAT_MOD_INITIALIZER);
		HammerUtil.initializeIfModuleEnabled(PERSISTENCE_MODULE_ID, HAMMER_PERSISTENCE_MOD_INITIALIZER);
		HammerUtil.initializeIfModuleEnabled(GRAVITY_MODULE_ID, HAMMER_GRAVITY_MOD_INITIALIZER);
		HammerUtil.initializeIfModuleEnabled(GUI_MODULE_ID, HAMMER_GUI_MOD_INITIALIZER);
		HammerUtil.initializeIfModuleEnabled(PERMISSION_MODULE_ID, HAMMER_PERMISSION_MOD_INITIALIZER);
		HammerUtil.initializeIfModuleEnabled(PRESET_MODULE_ID, HAMMER_PRESET_MOD_INITIALIZER);
		HammerUtil.initializeIfModuleEnabled(UTIL_MODULE_ID, HAMMER_UTIL_MOD_INITIALIZER);
		HammerUtil.initializeIfModuleEnabled(ZONE_MODULE_ID, HAMMER_ZONE_MOD_INITIALIZER);
		
		getItemRegistry().register();
		getBlockRegistry().register();
		getScreenHandlerRegistry().register();
		getArgumentSerializerRegistry().register();
	}
	
	public static DeferredRegister<Item> getItemRegistry() {
		if (ITEM_REGISTRY == null) {
			ITEM_REGISTRY = DeferredRegister.create(ID, Registry.ITEM_KEY);
		}
		
		return ITEM_REGISTRY;
	}
	
	public static DeferredRegister<Block> getBlockRegistry() {
		if (BLOCK_REGISTRY == null) {
			BLOCK_REGISTRY = DeferredRegister.create(ID, Registry.BLOCK_KEY);
		}
		
		return BLOCK_REGISTRY;
	}
	
	public static DeferredRegister<ScreenHandlerType<?>> getScreenHandlerRegistry() {
		if (SCREEN_HANDLER_REGISTRY == null) {
			// TODO: Check if this works. Registry ID may not be correct.
			// Turns out the ID is "menu".
			SCREEN_HANDLER_REGISTRY = DeferredRegister.create(ID, Registry.MENU_KEY);
		}
		
		return SCREEN_HANDLER_REGISTRY;
	}
	
	public static DeferredRegister<ArgumentSerializer<?, ?>> getArgumentSerializerRegistry() {
		if (ARGUMENT_TYPE_REGISTRY == null) {
			ARGUMENT_TYPE_REGISTRY = DeferredRegister.create(ID, Registry.COMMAND_ARGUMENT_TYPE_KEY);
		}
		
		return ARGUMENT_TYPE_REGISTRY;
	}
}
