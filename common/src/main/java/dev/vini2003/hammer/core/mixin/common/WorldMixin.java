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

package dev.vini2003.hammer.core.mixin.common;

import dev.vini2003.hammer.core.impl.common.component.container.ComponentContainer;
import dev.vini2003.hammer.core.impl.common.component.holder.ComponentHolder;
import dev.vini2003.hammer.core.impl.common.state.ComponentPersistentState;
import dev.vini2003.hammer.core.impl.common.util.ComponentUtil;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(World.class)
public class WorldMixin implements ComponentHolder {
	private final ComponentContainer hammer$componentContainer = new ComponentContainer();
	
	private World hammer$self() {
		return (World) (Object) this;
	}
	
	@Inject(at = @At("RETURN"), method = "<init>")
	private void hammer$init(MutableWorldProperties properties, RegistryKey registryRef, DynamicRegistryManager registryManager, RegistryEntry dimensionEntry, Supplier profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates, CallbackInfo ci) {
		if (!(hammer$self() instanceof ServerWorld)) {
			ComponentUtil.attachToWorld(hammer$self());
		}
	}
	
	@Override
	public ComponentContainer getComponentContainer() {
		if (hammer$self() instanceof ServerWorld serverWorld) {
			var state = serverWorld.getPersistentStateManager().get((nbt) -> new ComponentPersistentState(serverWorld, nbt), "Hammer$Components");
			return state.getComponentContainer();
		} else {
			return hammer$componentContainer;
		}
	}
}
