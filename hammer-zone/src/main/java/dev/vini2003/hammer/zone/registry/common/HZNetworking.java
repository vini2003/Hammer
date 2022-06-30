package dev.vini2003.hammer.zone.registry.common;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.util.NbtUtil;
import dev.vini2003.hammer.zone.HZ;
import dev.vini2003.hammer.zone.api.common.manager.ZoneGroupManager;
import dev.vini2003.hammer.zone.api.common.zone.Zone;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.UUID;

public class HZNetworking {
	public static final Identifier ZONE_CREATED = HC.id("zone_created");
	public static final Identifier ZONE_DELETED = HC.id("zone_deleted");
	public static final Identifier ZONE_COLORED = HC.id("zone_colored");
	public static final Identifier ZONE_PASTED = HC.id("zone_pasted");
	public static final Identifier ZONE_INTERACTION = HC.id("zone_interaction");
	
	// TODO: Make shift-right-click copy settings between zones!
	// TODO: Make zoning require OP!
	public static void init() {
		ServerPlayNetworking.registerGlobalReceiver(ZONE_PASTED, (server, player, handler, buf, responseSender) -> {
			var zoneId = buf.readIdentifier();
			
			var rgba = buf.readLong();
			var color = new Color(rgba);
			
			var hasGroup = buf.readBoolean();
			
			Identifier[] groupId = { null };
			
			if (hasGroup) {
				groupId[0] = buf.readIdentifier();
			}
			
			server.execute(() -> {
				var component = HZComponents.ZONES.get(player.world);
				
				var zone = component.getZoneById(zoneId);
				
				if (groupId[0] != null) {
					var group = ZoneGroupManager.getOrCreate(groupId[0]);
					
					zone.setGroup(group);
				}
				
				zone.setColor(color);
			});
		});
		
		ServerPlayNetworking.registerGlobalReceiver(ZONE_CREATED, (server, player, handler, buf, responseSender) -> {
			server.execute(() -> {
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
							null,
							HC.id(UUID.randomUUID().toString().replace("-", "")),
							new Position((float) (pos.getX() - 0.5F), (float) (pos.getY() - 0.5F), (float) (pos.getZ() - 0.5F)),
							new Position((float) (pos.getX() + 0.5F), (float) (pos.getY() + 0.5F), (float) (pos.getZ() + 0.5F)),
							new Color((long) 0x25ABFF7E)
					);
					
					var component = HZComponents.ZONES.get(player.world);
					
					for (var existingZone : component.getZones()) {
						if (existingZone.getCenterPos().equals(zone.getCenterPos())) {
							if (existingZone.getWidth() == zone.getWidth() && existingZone.getHeight() == zone.getHeight() && existingZone.getDepth() == zone.getDepth()) {
								return;
							}
						}
					}
					
					component.add(zone);
				}
			});
		});
		
		ServerPlayNetworking.registerGlobalReceiver(ZONE_DELETED, (server, player, handler, buf, responseSender) -> {
			var id = buf.readIdentifier();
			
			server.execute(() -> {
				var component = HZComponents.ZONES.get(player.world);
				
				var zone = component.getZoneById(id);
				
				zone.setColor(new Color((long) 0xF500497E));
				
				zone.markRemoved();
				
				HZComponents.ZONES.sync(player.world);
				
				component.remove(zone);
			});
		});
		
		ServerPlayNetworking.registerGlobalReceiver(ZONE_COLORED, (server, player, handler, buf, responseSender) -> {
			var id = buf.readIdentifier();
			var rgba = buf.readLong();
			
			server.execute(() -> {
				var component = HZComponents.ZONES.get(player.world);
				
				var zone = component.getZoneById(id);
				
				zone.setColor(new Color(rgba));
			});
		});
		
		ServerPlayNetworking.registerGlobalReceiver(ZONE_INTERACTION, (server, player, handler, buf, responseSender) -> {
			var id = buf.readIdentifier();
			var side = buf.readEnumConstant(Direction.class);
			var hasAltDown = buf.readBoolean();
			var hasCtrlDown = buf.readBoolean();
			var key = buf.readInt();
			
			server.execute(() -> {
				var component = HZComponents.ZONES.get(player.world);
				
				var zone = component.getZoneById(id);
				
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
			});
		});
	}
}
