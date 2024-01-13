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

package dev.vini2003.hammer.zone.registry.client;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.networking.NetworkManager;
import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.zone.api.common.zone.Zone;
import dev.vini2003.hammer.zone.api.common.zone.ZoneGroup;
import dev.vini2003.hammer.zone.registry.common.HZNetworking;
import io.netty.buffer.Unpooled;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.DyeItem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.UUID;

// TODO: Add Stages!
// TODO: Add zone information HUD display!
// TODO: Add stage information HUD display!
// TODO: Add zone and stage exports!
// TODO: Add world synchronization mod!
// TODO: Add whole-map ReplayMod recording!
public class HZEvents {
	public static long TICKS = 5L;
	public static long PREV_TICKS = 0L;
	
	@Nullable
	public static Zone SELECTED_ZONE = null;
	
	@Nullable
	public static Direction SELECTED_ZONE_SIDE = null;
	
	public static boolean WAS_UP_PRESSED = false;
	public static boolean WAS_DOWN_PRESSED = false;
	
	@Nullable
	public static ZoneGroup COPIED_ZONE_GROUP = null;
	
	@Nullable
	public static Color COPIED_ZONE_COLOR = null;
	
	public static void init() {
		ClientTickEvent.CLIENT_POST.register(client -> {
			var window = client.getWindow();
			var handle = window.getHandle();
			
			if (TICKS - PREV_TICKS >= 2 && client.currentScreen == null && HZValues.ZONE_EDITOR) {
				if (SELECTED_ZONE == null) {
					// TODO: Move Identifier UUID method to utility class!
					
					// Create
					if (InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_INSERT)) {
						var buf = new PacketByteBuf(Unpooled.buffer());
						buf.writeIdentifier(HC.id(UUID.randomUUID().toString().replace("-", "")));
						
						NetworkManager.sendToServer(HZNetworking.ZONE_CREATE, buf);
					}
				} else {
					// Delete
					if (InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_DELETE)) {
						var buf = new PacketByteBuf(Unpooled.buffer());
						buf.writeIdentifier(SELECTED_ZONE.getId());
						
						NetworkManager.sendToServer(HZNetworking.ZONE_DELETE, buf);
					}
					
					if (Screen.hasControlDown()) {
						// Copy
						if (InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_C)) {
							COPIED_ZONE_COLOR = SELECTED_ZONE.getColor();
							COPIED_ZONE_GROUP = SELECTED_ZONE.getGroup();
						} else if (InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_V)) {
							// Paste
							var buf = new PacketByteBuf(Unpooled.buffer());
							buf.writeIdentifier(SELECTED_ZONE.getId());
							buf.writeLong(COPIED_ZONE_COLOR.toRgba());
							
							if (COPIED_ZONE_GROUP != null) {
								buf.writeBoolean(true);
								buf.writeIdentifier(COPIED_ZONE_GROUP.getId());
							} else {
								buf.writeBoolean(false);
							}
							
							NetworkManager.sendToServer(HZNetworking.ZONE_PASTE, buf);
						}
					}
				}
				
				if (SELECTED_ZONE != null && !SELECTED_ZONE.isRemoved()) {
					if (client.player != null) {
						if (client.player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof DyeItem dye) {
							if (client.mouse.wasRightButtonClicked()) {
								var components = dye.getColor().getColorComponents();
								
								var color = new Color(components[0], components[1], components[2], SELECTED_ZONE.getColor().getA());
								
								var buf = new PacketByteBuf(Unpooled.buffer());
								buf.writeIdentifier(SELECTED_ZONE.getId());
								buf.writeLong(color.toRgba());
								
								NetworkManager.sendToServer(HZNetworking.ZONE_COLOR_CHANGED, buf);
							}
						}
					}
				}

				if (SELECTED_ZONE != null && SELECTED_ZONE_SIDE != null && (WAS_UP_PRESSED || WAS_DOWN_PRESSED) && !SELECTED_ZONE.isRemoved()) {
					var buf = new PacketByteBuf(Unpooled.buffer());
					buf.writeIdentifier(SELECTED_ZONE.getId());
					buf.writeEnumConstant(SELECTED_ZONE_SIDE);
					buf.writeBoolean(Screen.hasAltDown());
					buf.writeBoolean(Screen.hasControlDown());
					
					// Stretch || Scale
					if (!Screen.hasAltDown()) {
						if (WAS_UP_PRESSED) {
							buf.writeInt(GLFW.GLFW_KEY_UP);
							
							if (Screen.hasControlDown()) {
								PREV_TICKS = TICKS;
							} else {
								PREV_TICKS = TICKS;
							}
						} else if (WAS_DOWN_PRESSED) {
							buf.writeInt(GLFW.GLFW_KEY_DOWN);
							
							if (Screen.hasControlDown()) {
								PREV_TICKS = TICKS;
							} else {
								PREV_TICKS = TICKS;
							}
						}
					} else {
						// Shift
						if (WAS_UP_PRESSED) {
							buf.writeInt(GLFW.GLFW_KEY_UP);
							
							PREV_TICKS = TICKS;
						} else if (WAS_DOWN_PRESSED) {
							buf.writeInt(GLFW.GLFW_KEY_DOWN);
							
							PREV_TICKS = TICKS;
						}
					}
					
					NetworkManager.sendToServer(HZNetworking.ZONE_INTERACT, buf);
				}
			}
			
			++TICKS;
		});
	}
}
