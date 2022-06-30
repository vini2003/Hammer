package dev.vini2003.hammer.zone.registry.client;

import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.client.util.DrawingUtil;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.math.position.Position;
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

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

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
	
	// TODO: Select only the closest zone side!
	// TODO: Creation and deletion of zones!
	public static void init() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (TICKS - PREV_TICKS >= 2) {
				if (SELECTED_ZONE == null) {
					var window = client.getWindow();
					var handle = window.getHandle();
					
					// Create
					if (InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_INSERT)) {
						var buf = PacketByteBufs.create();
						
						ClientPlayNetworking.send(HZNetworking.ZONE_CREATED, buf);
					}
				} else {
					var window = client.getWindow();
					var handle = window.getHandle();
					
					// Delete
					if (InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_DELETE)) {
						var buf = PacketByteBufs.create();
						buf.writeIdentifier(SELECTED_ZONE.getId());
						
						ClientPlayNetworking.send(HZNetworking.ZONE_DELETED, buf);
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
							
							ClientPlayNetworking.send(HZNetworking.ZONE_PASTED, buf);
						}
					}
				}
				
				if (SELECTED_ZONE != null && !SELECTED_ZONE.isRemoved()) {
					if (client.player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof DyeItem dye) {
						if (client.mouse.wasRightButtonClicked()) {
							var components = dye.getColor().getColorComponents();
							
							var color = new Color(components[0], components[1], components[2], SELECTED_ZONE.getColor().getA());
							
							var buf = PacketByteBufs.create();
							buf.writeIdentifier(SELECTED_ZONE.getId());
							buf.writeLong(color.toRgba());
							
							ClientPlayNetworking.send(HZNetworking.ZONE_COLORED, buf);
						}
					}
				}

				if (SELECTED_ZONE != null && SELECTED_ZONE_SIDE != null && (WAS_UP_PRESSED || WAS_DOWN_PRESSED) && !SELECTED_ZONE.isRemoved()) {
					var window = client.getWindow();
					var handle = window.getHandle();
					
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
					
					ClientPlayNetworking.send(HZNetworking.ZONE_INTERACTION, buf);
				}
			}
			
			++TICKS;
		});
		
		// Draw all the zones contained in the world.
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
			
			var component = HZComponents.ZONES.get(world);
			
			var zonesToRemove = new ArrayList<Zone>();
			
			// TODO: Optimize!
			record ZoneData(
					Zone zone,
					Direction side
			) {}
			
			var zonesToChooseFrom = new ArrayList<ZoneData>();
			var zonesBySideDistance = new HashMap<ZoneData, Double>();
			
			for (var zone : component.getZones()) {
				var minPos = zone.getLerpedMinPos(context.tickDelta() / 64.0F);
				var maxPos = zone.getLerpedMaxPos(context.tickDelta() / 64.0F);
				
				var centerPos = new Position(
						(maxPos.getX() + minPos.getX()) / 2.0F,
						(maxPos.getY() + minPos.getY()) / 2.0F,
						(maxPos.getZ() + minPos.getZ()) / 2.0F
				);
				
				if (zone.isRemoved()) {
					if (centerPos.distanceTo(minPos) <= 0.015F) {
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
					zonesToChooseFrom.add(new ZoneData(zone, side));
					zonesBySideDistance.put(new ZoneData(zone, side), startPos.add(distance[0] * width, distance[0] * height, distance[0] * handle).distanceTo(player.getPos()));
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
			
			for (var entry : zonesBySideDistance.entrySet()) {
				if (entry.getValue() < selectedZoneDistance) {
					SELECTED_ZONE = entry.getKey().zone();
					SELECTED_ZONE_SIDE = entry.getKey().side();
					
					selectedZoneDistance = entry.getValue();
				}
			}
			
			for (var zone : zonesToRemove) {
				component.remove(zone);
			}
		});
	}
}
