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

package dev.vini2003.hammer.core.api.common.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import org.jetbrains.annotations.Nullable;
import java.util.function.Predicate;

public class RaycastUtil {
	@Nullable
	public static EntityHitResult raycastEntity(PlayerEntity source, float delta, float maxDist) {
		var pos = source.getCameraPosVec(delta);
		
		var rot = source.getRotationVec(1.0F);
		
		var end = pos.add(rot.x * maxDist, rot.y * maxDist, rot.z * maxDist);
		
		var sourceBox = source.getBoundingBox().stretch(rot.multiply(maxDist)).expand(2.5D, 2.5D, 2.5D);
		
		var entityHitResult = raycastEntity(source, pos, end, sourceBox, (e) -> {
			return !e.isSpectator() && e.isCollidable();
		}, maxDist * maxDist);
		
		return entityHitResult;
	}
	
	@Nullable
	public static EntityHitResult raycastEntity(Entity source, Vec3d pos, Vec3d end, Box box, Predicate<Entity> predicate, double sqrMaxDist) {
		var world = source.getWorld();
		
		var distToGo = sqrMaxDist;
		
		Entity target = null;
		
		Vec3d targetPos = null;
		
		for (var entity : world.getOtherEntities(source, box, predicate)) {
			var entityBox = entity.getBoundingBox().expand(entity.getTargetingMargin());
			
			var optPos = entityBox.raycast(pos, end);
			
			if (entityBox.contains(pos)) {
				if (distToGo >= 0.0D) {
					target = entity;
					targetPos = optPos.orElse(pos);
					
					distToGo = 0.0D;
				}
			} else if (optPos.isPresent()) {
				var entityPos = optPos.get();
				
				var sqrDist = pos.squaredDistanceTo(entityPos);
				
				if (sqrDist < distToGo || distToGo == 0.0D) {
					if (entity.getRootVehicle() == source.getRootVehicle()) {
						if (distToGo == 0.0) {
							target = entity;
							targetPos = entityPos;
						}
					} else {
						target = entity;
						targetPos = entityPos;
						
						distToGo = sqrDist;
					}
				}
			}
		}
		
		if (target == null) {
			return null;
		}
		
		return new EntityHitResult(target, targetPos);
	}
}
