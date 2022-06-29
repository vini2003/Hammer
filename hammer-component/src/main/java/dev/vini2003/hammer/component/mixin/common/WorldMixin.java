package dev.vini2003.hammer.component.mixin.common;

import dev.vini2003.hammer.component.impl.common.misc.ComponentUtil;
import dev.vini2003.hammer.component.impl.common.component.ComponentContainer;
import dev.vini2003.hammer.component.impl.common.component.ComponentHolder;
import dev.vini2003.hammer.component.impl.common.state.ComponentPersistentState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(World.class)
public class WorldMixin implements ComponentHolder {
	private ComponentContainer hammer$componentContainer = new ComponentContainer();
	
	@Inject(at = @At("RETURN"), method = "<init>")
	private void hammer$init(MutableWorldProperties mutableWorldProperties, RegistryKey<World> registryKey, RegistryEntry<DimensionType> registryEntry, Supplier<Profiler> supplier, boolean bl, boolean bl2, long l, CallbackInfo ci) {
		if (!((Object) this instanceof ServerWorld)) {
			ComponentUtil.attachToWorld((World) (Object) this);
		}
	}
	
	@Override
	public ComponentContainer getComponentContainer() {
		if ((Object) this instanceof ServerWorld world) {
			var state = world.getPersistentStateManager().get((nbt) -> new ComponentPersistentState(world, nbt), "Hammer$Components");
			return state.getComponentContainer();
		} else {
			return hammer$componentContainer;
		}
	}
}
