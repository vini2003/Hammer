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

package dev.vini2003.hammer.zone.api.common.util;

import dev.vini2003.hammer.core.api.client.color.Color;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

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
