package dev.vini2003.hammer.stage.mixin.common.common;

import com.mojang.serialization.Lifecycle;
import dev.vini2003.hammer.stage.registry.common.HSDimensions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.dimension.DimensionOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	@Shadow
	@Final
	protected SaveProperties saveProperties;
	
	@Inject(at = @At("HEAD"), method = "createWorlds")
	private void hammer$createWorlds(CallbackInfo ci) {
		var generatorOptions = saveProperties.getGeneratorOptions();
		var registry = generatorOptions.getDimensions();
		
		for (var entry : HSDimensions.DIMENSIONS.entrySet()) {
			var key = entry.getKey();
			var value = entry.getValue();
			
			if (!registry.contains(key)) {
				((SimpleRegistry<DimensionOptions>) registry).add(key, value.get(), Lifecycle.stable());
			}
		}
	}
}
