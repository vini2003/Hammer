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

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.client.util.DrawingUtil;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.zone.api.common.manager.ZoneManager;
import dev.vini2003.hammer.zone.api.common.util.ZoneDrawingUtil;
import dev.vini2003.hammer.zone.api.common.zone.Zone;
import dev.vini2003.hammer.zone.api.common.zone.ZoneGroup;
import dev.vini2003.hammer.zone.registry.common.HZComponents;
import dev.vini2003.hammer.zone.registry.common.HZNetworking;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.DyeItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import org.lwjgl.glfw.GLFW;

import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.UUID;

// TODO: Add Stages!
// TODO: Add zone information HUD display!
// TODO: Add stage information HUD display!
// TODO: Add zone and stage exports!
// TODO: Add world synchronization mod!
// TODO: Add whole-map ReplayMod recording!
public class HZEvents {
	private static long TICKS = 5L;
	private static long PREV_TICKS = 0L;
	
	@Nullable
	private static Zone SELECTED_ZONE = null;
	
	@Nullable
	private static Direction SELECTED_ZONE_SIDE = null;
	
	private static boolean WAS_UP_PRESSED = false;
	private static boolean WAS_DOWN_PRESSED = false;
	
	@Nullable
	private static ZoneGroup COPIED_ZONE_GROUP = null;
	
	@Nullable
	private static Color COPIED_ZONE_COLOR = null;
	
	public static void init() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			var window = client.getWindow();
			var handle = window.getHandle();
			
			if (TICKS - PREV_TICKS >= 2 && client.currentScreen == null && HZValues.ZONE_EDITOR) {
				if (SELECTED_ZONE == null) {
					// TODO: Move Identifier UUID method to utility class!
					
					// Create
					if (InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_INSERT)) {
						var buf = PacketByteBufs.create();
						buf.writeIdentifier(HC.id(UUID.randomUUID().toString().replace("-", "")));
						
						ClientPlayNetworking.send(HZNetworking.ZONE_CREATE, buf);
					}
				} else {
					// Delete
					if (InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_DELETE)) {
						var buf = PacketByteBufs.create();
						buf.writeIdentifier(SELECTED_ZONE.getId());
						
						ClientPlayNetworking.send(HZNetworking.ZONE_DELETE, buf);
					}
					
					if (Screen.hasControlDown()) {
						// Copy
						if (InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_C)) {
							COPIED_ZONE_COLOR = SELECTED_ZONE.getColor();
							COPIED_ZONE_GROUP = SELECTED_ZONE.getGroup();
						} else if (InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_V)) {
							// Paste
							var buf = PacketByteBufs.create();
							buf.writeIdentifier(SELECTED_ZONE.getId());
							buf.writeLong(COPIED_ZONE_COLOR.toRgba());
							
							if (COPIED_ZONE_GROUP != null) {
								buf.writeBoolean(true);
								buf.writeIdentifier(COPIED_ZONE_GROUP.getId());
							} else {
								buf.writeBoolean(false);
							}
							
							ClientPlayNetworking.send(HZNetworking.ZONE_PASTE, buf);
						}
					}
				}
				
				if (SELECTED_ZONE != null && !SELECTED_ZONE.isRemoved()) {
					if (client.player != null) {
						if (client.player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof DyeItem dye) {
							if (client.mouse.wasRightButtonClicked()) {
								var components = dye.getColor().getColorComponents();
								
								var color = new Color(components[0], components[1], components[2], SELECTED_ZONE.getColor().getA());
								
								var buf = PacketByteBufs.create();
								buf.writeIdentifier(SELECTED_ZONE.getId());
								buf.writeLong(color.toRgba());
								
								ClientPlayNetworking.send(HZNetworking.ZONE_COLOR_CHANGED, buf);
							}
						}
					}
				}

				if (SELECTED_ZONE != null && SELECTED_ZONE_SIDE != null && (WAS_UP_PRESSED || WAS_DOWN_PRESSED) && !SELECTED_ZONE.isRemoved()) {
					var buf = PacketByteBufs.create();
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
					
					ClientPlayNetworking.send(HZNetworking.ZONE_INTERACT, buf);
				}
			}
			
			++TICKS;
		});
		
		// Draw all the zones in the world.
		WorldRenderEvents.AFTER_ENTITIES.register(context -> {
			var client = InstanceUtil.getClient();
			
			var window = client.getWindow();
			var handle = window.getHandle();
			
			WAS_UP_PRESSED = InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_UP);
			WAS_DOWN_PRESSED = InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_DOWN);
			
			var player = client.player;
			
			var camera = context.camera();
			
			var cX = camera.getPos().getX();
			var cY = camera.getPos().getY();
			var cZ = camera.getPos().getZ();
			
			var matrices = context.matrixStack();
			var provider = context.consumers();
			var world = context.world();
			
			var zonesToRemove = new ArrayList<Zone>();
			
			record ZoneData(
					Zone zone,
					Direction side,
					double distance
			) {}
			
			var zoneData = new ArrayList<ZoneData>();
			
			for (var zone : ZoneManager.getAll(world)) {
				var minPos = zone.getLerpedMinPos(context.tickDelta() / 64.0F);
				var maxPos = zone.getLerpedMaxPos(context.tickDelta() / 64.0F);
				
				var centerPos = new Position(
						(maxPos.getX() + minPos.getX()) / 2.0F,
						(maxPos.getY() + minPos.getY()) / 2.0F,
						(maxPos.getZ() + minPos.getZ()) / 2.0F
				);
				
				if (zone.isRemoved()) {
					if (centerPos.distanceTo(minPos) <= 0.02F) {
						zonesToRemove.add(zone);
						
						continue;
					}
				}
				
				matrices.push();
				
				matrices.translate(centerPos.getX() - cX, centerPos.getY() - cY, centerPos.getZ() - cZ);
				
				var width = maxPos.getX() - minPos.getX();
				var height = maxPos.getY() - minPos.getY();
				var depth = maxPos.getZ() - minPos.getZ();
				
				var box = new Box(
						minPos.getX(),
						minPos.getY(),
						minPos.getZ(),
						maxPos.getX(),
						maxPos.getY(),
						maxPos.getZ()
				);
				
				var rotation = player.getRotationVec(1.0F);
				
				var startPos = player.getCameraPosVec(1.0F);
				var endPos = player.getPos().add(rotation.x * 256.0F, rotation.y * 256.0F, rotation.z * 256.0F);
				
				var distance = new double[] { 1.0 };
				
				var side = Box.traceCollisionSide(box, startPos, distance, null, endPos.getX() - startPos.getX(), endPos.getY() - startPos.getY(), endPos.getZ() - startPos.getZ());
				
				if (side != null) {
					var sidePos = startPos.add(distance[0] * width, distance[0] * height, distance[0] * handle);
					zoneData.add(new ZoneData(zone, side, sidePos.distanceTo(player.getPos())));
				}
				
				if (zone != SELECTED_ZONE) {
					side = null;
				}
				
				ZoneDrawingUtil.drawZone(
						matrices,
						provider,
						0.0F, 0.0F, 0.0F,
						width, height, depth,
						zone.getColor(),
						HZRenderLayers.getZone(),
						side
				);
				
				DrawingUtil.drawLineCube(
						matrices,
						provider,
						0.0F, 0.0F, 0.0F,
						width, height, depth,
						new Color((long) 0xFFFFFFFF),
						HZRenderLayers.getZoneOutline()
				);
				
				matrices.pop();
			}
			
			var selectedZoneDistance = Double.MAX_VALUE;
			
			SELECTED_ZONE = null;
			SELECTED_ZONE_SIDE = null;
			
			for (var data : zoneData) {
				if (data.distance < selectedZoneDistance) {
					SELECTED_ZONE = data.zone();
					SELECTED_ZONE_SIDE = data.side();
					
					selectedZoneDistance = data.distance();
				}
			}
			
			HZValues.setMouseSelectedZone(SELECTED_ZONE);
			
			for (var zone : zonesToRemove) {
				ZoneManager.remove(world, zone);
			}
		});
	}
}
