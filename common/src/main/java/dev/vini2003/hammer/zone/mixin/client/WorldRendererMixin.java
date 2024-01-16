package dev.vini2003.hammer.zone.mixin.client;

import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.client.util.DrawingUtil;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.zone.api.common.data.ZoneData;
import dev.vini2003.hammer.zone.api.common.manager.ZoneManager;
import dev.vini2003.hammer.zone.api.common.util.ZoneDrawingUtil;
import dev.vini2003.hammer.zone.api.common.zone.Zone;
import dev.vini2003.hammer.zone.registry.client.HZEvents;
import dev.vini2003.hammer.zone.registry.client.HZRenderLayers;
import dev.vini2003.hammer.zone.registry.client.HZValues;
import net.minecraft.client.render.*;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Matrix4f;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
	@Shadow
	@Final
	public BufferBuilderStorage bufferBuilders;
	@Nullable
	private MatrixStack hammer$matrices;
	private float hammer$tickDelta;
	@Nullable
	private VertexConsumerProvider hammer$provider;
	@Nullable
	private ClientWorld hammer$world;
	
	@Inject(at = @At("HEAD"), method = "render")
	private void hammer$render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci) {
		this.hammer$matrices = matrices;
		this.hammer$tickDelta = tickDelta;
		this.hammer$provider = bufferBuilders.getEntityVertexConsumers();
		this.hammer$world = InstanceUtil.getClient().world;
	}
	
	@Inject(method = "render", at = @At(value = "CONSTANT", args = "stringValue=blockentities", ordinal = 0))
	private void hammer$render$blockentities$afterEntities(CallbackInfo ci) {
		if (!HZValues.ZONE_EDITOR) {
			return;
		}
		
		var client = InstanceUtil.getClient();
		
		var window = client.getWindow();
		var handle = window.getHandle();
		
		HZEvents.WAS_UP_PRESSED = InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_UP);
		HZEvents.WAS_DOWN_PRESSED = InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_DOWN);
		
		var player = client.player;
		
		var camera = client.gameRenderer.getCamera();
		
		var cX = camera.getPos().getX();
		var cY = camera.getPos().getY();
		var cZ = camera.getPos().getZ();
		
		var zonesToRemove = new ArrayList<Zone>();
		
		var zoneData = new ArrayList<ZoneData>();
		
		for (var zone : ZoneManager.getAll(hammer$world)) {
			var minPos = zone.getLerpedMinPos(hammer$tickDelta / 64.0F);
			var maxPos = zone.getLerpedMaxPos(hammer$tickDelta / 64.0F);
			
			var centerPos = Position.of(
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
			
			hammer$matrices.push();
			
			hammer$matrices.translate(centerPos.getX() - cX, centerPos.getY() - cY, centerPos.getZ() - cZ);
			
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
			
			if (zone != HZEvents.SELECTED_ZONE) {
				side = null;
			}
			
			ZoneDrawingUtil.drawZone(
					hammer$matrices,
					hammer$provider,
					0.0F, 0.0F, 0.0F,
					width, height, depth,
					zone.getColor(),
					HZRenderLayers.getZone(),
					side
			);
			
			DrawingUtil.drawLineCube(
					hammer$matrices,
					hammer$provider,
					0.0F, 0.0F, 0.0F,
					width, height, depth,
					new Color((long) 0xFFFFFFFF),
					HZRenderLayers.getZoneOutline()
			);
			
			hammer$matrices.pop();
		}
		
		var selectedZoneDistance = Double.MAX_VALUE;
		
		HZEvents.SELECTED_ZONE = null;
		HZEvents.SELECTED_ZONE_SIDE = null;
		
		for (var data : zoneData) {
			if (data.distance() < selectedZoneDistance) {
				HZEvents.SELECTED_ZONE = data.zone();
				HZEvents.SELECTED_ZONE_SIDE = data.side();
				
				selectedZoneDistance = data.distance();
			}
		}
		
		HZValues.setMouseSelectedZone(HZEvents.SELECTED_ZONE);
		
		for (var zone : zonesToRemove) {
			ZoneManager.remove(hammer$world, zone);
		}
	}
}
