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

package dev.vini2003.hammer.zone.registry.common;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.zone.api.common.manager.ZoneGroupManager;
import dev.vini2003.hammer.zone.api.common.manager.ZoneManager;
import dev.vini2003.hammer.zone.api.common.zone.Zone;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class HZNetworking {
	public static final Identifier ZONE_CREATE = HC.id("zone_create");
	public static final Identifier ZONE_GROUP_CREATE = HC.id("zone_group_create");
	public static final Identifier ZONE_DELETE = HC.id("zone_delete");
	public static final Identifier ZONE_GROUP_DELETE = HC.id("zone_group_delete");
	public static final Identifier ZONE_COLOR_CHANGED = HC.id("zone_color_changed");
	public static final Identifier ZONE_GROUP_CHANGED = HC.id("zone_group_changed");
	public static final Identifier ZONE_PASTE = HC.id("zone_paste");
	public static final Identifier ZONE_INTERACT = HC.id("zone_interact");
	
	public static void init() {
		NetworkManager.registerReceiver(NetworkManager.Side.C2S, ZONE_PASTE, (buf, context) -> {
			var player = (ServerPlayerEntity) context.getPlayer();
			var server = player.getServer();
			var handler = player.networkHandler;
			
			var zoneId = buf.readIdentifier();
			
			var rgba = buf.readLong();
			var color = new Color(rgba);
			
			var hasGroup = buf.readBoolean();
			
			var groupId = new Identifier[] { null };
			
			if (hasGroup) {
				groupId[0] = buf.readIdentifier();
			}
			
			server.execute(() -> {
				if (!player.hasPermissionLevel(4)) {
					return;
				}
				
				var zone = ZoneManager.get(player.getWorld(), zoneId);
				
				if (zone.isLocked()) {
					return;
				}
				
				if (groupId[0] != null) {
					var group = ZoneGroupManager.getOrCreate(groupId[0]);
					
					zone.setGroup(group);
				}
				
				zone.setColor(color);
				
				ZoneManager.sync(player.getWorld());
			});
		});
		
		NetworkManager.registerReceiver(NetworkManager.Side.C2S, ZONE_CREATE, (buf, context) -> {
			var player = (ServerPlayerEntity) context.getPlayer();
			var server = player.getServer();
			var handler = player.networkHandler;
			
			var id = buf.readIdentifier();
			
			server.execute(() -> {
				if (!player.hasPermissionLevel(4)) {
					return;
				}
				
				var hitResult = player.raycast(32.0D, 0.0F, false);
				
				if (hitResult.getType() != HitResult.Type.MISS) {
					Vec3d pos;
					
					if (hitResult instanceof BlockHitResult blockHitResult) {
						var blockPos = blockHitResult.getBlockPos();
						
						pos = new Vec3d(blockPos.getX() + 0.5D, blockPos.getY() + 0.5D + 1.0D, blockPos.getZ() + 0.5D);
					} else {
						pos = hitResult.getPos();
					}
					
					var zone = new Zone(
							player.getWorld().getRegistryKey(),
							id,
							Position.of((float) (pos.getX() - 0.5F), (float) (pos.getY() - 0.5F), (float) (pos.getZ() - 0.5F)),
							Position.of((float) (pos.getX() + 0.5F), (float) (pos.getY() + 0.5F), (float) (pos.getZ() + 0.5F))
					);
					
					for (var existingZone : ZoneManager.getAll(player.getWorld())) {
						if (existingZone.getCenterPos().equals(zone.getCenterPos())) {
							if (existingZone.getWidth() == zone.getWidth() && existingZone.getHeight() == zone.getHeight() && existingZone.getDepth() == zone.getDepth()) {
								return;
							}
						}
					}
					
					ZoneManager.add(player.getWorld(), zone);
				}
			});
		});
		
		NetworkManager.registerReceiver(NetworkManager.Side.C2S, ZONE_GROUP_CREATE, (buf, context) -> {
			var player = (ServerPlayerEntity) context.getPlayer();
			var server = player.getServer();
			var handler = player.networkHandler;
			
			var id = buf.readIdentifier();
			
			server.execute(() -> {
				ZoneGroupManager.getOrCreate(id);
			});
		});
		
		NetworkManager.registerReceiver(NetworkManager.Side.C2S, ZONE_GROUP_DELETE, (buf, context) -> {
			var player = (ServerPlayerEntity) context.getPlayer();
			var server = player.getServer();
			var handler = player.networkHandler;
			
			var id = buf.readIdentifier();
			
			server.execute(() -> {
				ZoneGroupManager.remove(id);
			});
		});
		
		NetworkManager.registerReceiver(NetworkManager.Side.C2S, ZONE_DELETE, (buf, context) -> {
			var player = (ServerPlayerEntity) context.getPlayer();
			var server = player.getServer();
			var handler = player.networkHandler;
			
			var id = buf.readIdentifier();
			
			server.execute(() -> {
				if (!player.hasPermissionLevel(4)) {
					return;
				}
				
				var zone = ZoneManager.get(player.getWorld(), id);
				
				if (zone.isLocked()) {
					return;
				}
				
				zone.setColor(new Color((long) 0xF500497E));
				
				zone.markRemoved();
				
				ZoneManager.sync(player.getWorld());
				
				ZoneManager.remove(player.getWorld(), zone);
				
				ZoneManager.sync(player.getWorld());
			});
		});
		
		NetworkManager.registerReceiver(NetworkManager.Side.C2S, ZONE_COLOR_CHANGED, (buf, context) -> {
			var player = (ServerPlayerEntity) context.getPlayer();
			var server = player.getServer();
			var handler = player.networkHandler;
			
			var id = buf.readIdentifier();
			var rgba = buf.readLong();
			
			server.execute(() -> {
				if (!player.hasPermissionLevel(4)) {
					return;
				}
				
				var zone = ZoneManager.get(player.getWorld(), id);
				
				if (zone.isLocked()) {
					return;
				}
				
				zone.setColor(new Color(rgba));
				
				ZoneManager.sync(player.getWorld());
			});
		});
		
		NetworkManager.registerReceiver(NetworkManager.Side.C2S, ZONE_GROUP_CHANGED, (buf, context) -> {
			var player = (ServerPlayerEntity) context.getPlayer();
			var server = player.getServer();
			var handler = player.networkHandler;
			
			var id = buf.readIdentifier();
			var groupId = buf.readIdentifier();
			
			server.execute(() -> {
				if (!player.hasPermissionLevel(4)) {
					return;
				}
				
				var zone = ZoneManager.get(player.getWorld(), id);
				
				if (zone.isLocked()) {
					return;
				}
				
				var zoneGroup = ZoneGroupManager.getOrCreate(groupId);
				
				zone.setGroup(zoneGroup);
				
				ZoneManager.sync(player.getWorld());
			});
		});
		
		NetworkManager.registerReceiver(NetworkManager.Side.C2S, ZONE_INTERACT, (buf, context) -> {
			var player = (ServerPlayerEntity) context.getPlayer();
			var server = player.getServer();
			var handler = player.networkHandler;
			
			var id = buf.readIdentifier();
			var side = buf.readEnumConstant(Direction.class);
			var hasAltDown = buf.readBoolean();
			var hasCtrlDown = buf.readBoolean();
			var key = buf.readInt();
			
			server.execute(() -> {
				if (!player.hasPermissionLevel(4)) {
					return;
				}
				
				var zone = ZoneManager.get(player.getWorld(), id);
				
				if (zone.isLocked()) {
					return;
				}
				
				// Stretch || Scale
				if (!hasAltDown) {
					if (key == GLFW.GLFW_KEY_UP) {
						if (hasCtrlDown) {
							zone.scale(-1);
						} else {
							zone.stretch(side, -1);
						}
					} else if (key == GLFW.GLFW_KEY_DOWN) {
						if (hasCtrlDown) {
							zone.scale(1);
						} else {
							zone.stretch(side, 1);
						}
					}
				} else {
					// Shift
					if (key == GLFW.GLFW_KEY_UP) {
						zone.move(side, -1);
					} else if (key == GLFW.GLFW_KEY_DOWN) {
						zone.move(side, 1);
					}
				}
				
				ZoneManager.sync(player.getWorld());
			});
		});
	}
}
