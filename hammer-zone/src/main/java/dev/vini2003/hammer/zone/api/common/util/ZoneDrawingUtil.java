package dev.vini2003.hammer.zone.api.common.util;

import dev.vini2003.hammer.core.api.client.color.Color;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;

import javax.annotation.Nullable;

public class ZoneDrawingUtil {
	public static void drawZone(
			MatrixStack matrices,
			VertexConsumerProvider provider,
			float x, float y, float z,
			float width, float height, float depth,
			Color color,
			RenderLayer layer,
			@Nullable Direction selectedSide
	) {
		var consumer = provider.getBuffer(layer);
		
		var peek = matrices.peek();
		
		// Down
		if (selectedSide == Direction.DOWN) {
			consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y - (height / 2.0F), z + (depth / 2.0F)).color(1.0F, 1.0F, 1.0F, 0.5F + 0.5F * Math.abs((float) Math.sin((Util.getMeasuringTimeMs() / 500.0F)))).next();
			consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y - (height / 2.0F), z + (depth / 2.0F)).color(1.0F, 1.0F, 1.0F, 0.5F + 0.5F * Math.abs((float) Math.sin((Util.getMeasuringTimeMs() / 500.0F)))).next();
			consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y - (height / 2.0F), z - (depth / 2.0F)).color(1.0F, 1.0F, 1.0F, 0.5F + 0.5F * Math.abs((float) Math.sin((Util.getMeasuringTimeMs() / 500.0F)))).next();
			consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y - (height / 2.0F), z - (depth / 2.0F)).color(1.0F, 1.0F, 1.0F, 0.5F + 0.5F * Math.abs((float) Math.sin((Util.getMeasuringTimeMs() / 500.0F)))).next();
		} else {
			consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y - (height / 2.0F), z + (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
			consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y - (height / 2.0F), z + (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
			consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y - (height / 2.0F), z - (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
			consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y - (height / 2.0F), z - (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
		}
		
		// Up
		if (selectedSide == Direction.UP) {
			consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y + (height / 2.0F), z - (depth / 2.0F)).color(1.0F, 1.0F, 1.0F, 0.5F + 0.5F * Math.abs((float) Math.sin((Util.getMeasuringTimeMs() / 500.0F)))).next();
			consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y + (height / 2.0F), z + (depth / 2.0F)).color(1.0F, 1.0F, 1.0F, 0.5F + 0.5F * Math.abs((float) Math.sin((Util.getMeasuringTimeMs() / 500.0F)))).next();
			consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y + (height / 2.0F), z + (depth / 2.0F)).color(1.0F, 1.0F, 1.0F, 0.5F + 0.5F * Math.abs((float) Math.sin((Util.getMeasuringTimeMs() / 500.0F)))).next();
			consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y + (height / 2.0F), z - (depth / 2.0F)).color(1.0F, 1.0F, 1.0F, 0.5F + 0.5F * Math.abs((float) Math.sin((Util.getMeasuringTimeMs() / 500.0F)))).next();
		} else {
			consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y + (height / 2.0F), z - (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
			consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y + (height / 2.0F), z + (depth  / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
			consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y + (height / 2.0F), z + (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
			consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y + (height / 2.0F), z - (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
		}
		
		// North
		if (selectedSide == Direction.NORTH) {
			consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y - (height / 2.0F), z - (depth / 2.0F)).color(1.0F, 1.0F, 1.0F, 0.5F + 0.5F * Math.abs((float) Math.sin((Util.getMeasuringTimeMs() / 500.0F)))).next();
			consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y + (height / 2.0F), z - (depth / 2.0F)).color(1.0F, 1.0F, 1.0F, 0.5F + 0.5F * Math.abs((float) Math.sin((Util.getMeasuringTimeMs() / 500.0F)))).next();
			consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y + (height / 2.0F), z - (depth / 2.0F)).color(1.0F, 1.0F, 1.0F, 0.5F + 0.5F * Math.abs((float) Math.sin((Util.getMeasuringTimeMs() / 500.0F)))).next();
			consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y - (height / 2.0F), z - (depth / 2.0F)).color(1.0F, 1.0F, 1.0F, 0.5F + 0.5F * Math.abs((float) Math.sin((Util.getMeasuringTimeMs() / 500.0F)))).next();
		} else {
			consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y - (height / 2.0F), z - (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
			consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y + (height / 2.0F), z - (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
			consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y + (height / 2.0F), z - (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
			consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y - (height / 2.0F), z - (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
		}
		
		// South
		if (selectedSide == Direction.SOUTH) {
			consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y + (height / 2.0F), z + (depth / 2.0F)).color(1.0F, 1.0F, 1.0F, 0.5F + 0.5F * Math.abs((float) Math.sin((Util.getMeasuringTimeMs() / 500.0F)))).next();
			consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y - (height / 2.0F), z + (depth / 2.0F)).color(1.0F, 1.0F, 1.0F, 0.5F + 0.5F * Math.abs((float) Math.sin((Util.getMeasuringTimeMs() / 500.0F)))).next();
			consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y - (height / 2.0F), z + (depth / 2.0F)).color(1.0F, 1.0F, 1.0F, 0.5F + 0.5F * Math.abs((float) Math.sin((Util.getMeasuringTimeMs() / 500.0F)))).next();
			consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y + (height / 2.0F), z + (depth / 2.0F)).color(1.0F, 1.0F, 1.0F, 0.5F + 0.5F * Math.abs((float) Math.sin((Util.getMeasuringTimeMs() / 500.0F)))).next();
		} else {
			consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y - (height / 2.0F), z + (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
			consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y - (height / 2.0F), z + (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
			consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y + (height / 2.0F), z + (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
			consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y + (height / 2.0F), z + (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
		}
		
		// West
		if (selectedSide == Direction.WEST) {
			consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y + (height / 2.0F), z + (depth / 2.0F)).color(1.0F, 1.0F, 1.0F, 0.5F + 0.5F * Math.abs((float) Math.sin((Util.getMeasuringTimeMs() / 500.0F)))).next();
			consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y + (height / 2.0F), z - (depth / 2.0F)).color(1.0F, 1.0F, 1.0F, 0.5F + 0.5F * Math.abs((float) Math.sin((Util.getMeasuringTimeMs() / 500.0F)))).next();
			consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y - (height / 2.0F), z - (depth / 2.0F)).color(1.0F, 1.0F, 1.0F, 0.5F + 0.5F * Math.abs((float) Math.sin((Util.getMeasuringTimeMs() / 500.0F)))).next();
			consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y - (height / 2.0F), z + (depth / 2.0F)).color(1.0F, 1.0F, 1.0F, 0.5F + 0.5F * Math.abs((float) Math.sin((Util.getMeasuringTimeMs() / 500.0F)))).next();
		} else {
			consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y + (height / 2.0F), z + (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
			consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y + (height / 2.0F), z - (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
			consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y - (height / 2.0F), z - (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
			consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y - (height / 2.0F), z + (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
		}
		
		// East
		if (selectedSide == Direction.EAST) {
			consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y - (height / 2.0F), z - (depth / 2.0F)).color(1.0F, 1.0F, 1.0F, 0.5F + 0.5F * Math.abs((float) Math.sin((Util.getMeasuringTimeMs() / 500.0F)))).next();
			consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y + (height / 2.0F), z - (depth / 2.0F)).color(1.0F, 1.0F, 1.0F, 0.5F + 0.5F * Math.abs((float) Math.sin((Util.getMeasuringTimeMs() / 500.0F)))).next();
			consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y + (height / 2.0F), z + (depth / 2.0F)).color(1.0F, 1.0F, 1.0F, 0.5F + 0.5F * Math.abs((float) Math.sin((Util.getMeasuringTimeMs() / 500.0F)))).next();
			consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y - (height / 2.0F), z + (depth / 2.0F)).color(1.0F, 1.0F, 1.0F, 0.5F + 0.5F * Math.abs((float) Math.sin((Util.getMeasuringTimeMs() / 500.0F)))).next();
		} else {
			consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y - (height / 2.0F), z - (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
			consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y + (height / 2.0F), z - (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
			consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y + (height / 2.0F), z + (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
			consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y - (height / 2.0F), z + (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
		}
	}
}
