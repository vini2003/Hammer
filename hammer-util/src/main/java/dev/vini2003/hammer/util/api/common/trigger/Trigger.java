package dev.vini2003.hammer.util.api.common.trigger;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

@FunctionalInterface
public interface Trigger {
	void onTrigger(World world, PlayerEntity user, Hand hand);
}