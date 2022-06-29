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

package dev.vini2003.hammer.component;

import dev.vini2003.hammer.component.api.common.component.Component;
import dev.vini2003.hammer.component.api.common.component.ComponentKey;
import dev.vini2003.hammer.component.api.common.manager.ComponentManager;
import dev.vini2003.hammer.component.registry.common.HCEvents;
import dev.vini2003.hammer.component.registry.common.HCNetworking;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class HC implements ModInitializer {
	// todo: copy on player death n stuff
	
	class WorldComponent implements Component {
		private int foo = 0;
		
		private World world;
		
		public WorldComponent(World world) {
			this.world = world;
		}
		
		@Override
		public void writeToNbt(NbtCompound nbt) {
			nbt.putInt("Foo", 47);
		}
		
		@Override
		public void readFromNbt(NbtCompound nbt) {
			foo = nbt.getInt("Foo");
		}
	}
	
	class EntityComponent implements Component {
		private String bar = "foo";
		
		private Entity entity;
		
		public EntityComponent(Entity entity) {
			this.entity = entity;
		}
		
		@Override
		public void writeToNbt(NbtCompound nbt) {
			nbt.putString("Bar", bar);
		}
		
		@Override
		public void readFromNbt(NbtCompound nbt) {
			bar = nbt.getString("Bar");
		}
	}
	
	public static final ComponentKey<WorldComponent> WORLD_COMPONENT = ComponentManager.registerKey(new Identifier("hammer", "world_component"));
	public static final ComponentKey<EntityComponent> ENTITY_COMPONENT = ComponentManager.registerKey(new Identifier("hammer", "entity_component"));
	
	@Override
	public void onInitialize() {
		ComponentManager.registerForWorld(WORLD_COMPONENT, WorldComponent::new);
		ComponentManager.registerForEntity(ENTITY_COMPONENT, EntityComponent::new);
		
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (var world : server.getWorlds()) {
				var component = WORLD_COMPONENT.get(world);
				System.out.println("in server -> " + component.foo);
				WORLD_COMPONENT.sync(world);
			}
		});
		
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.world != null) {
				var component = WORLD_COMPONENT.get(client.world);
				System.out.println("in client -> " + component.foo);
			}
		});
		
		HCEvents.init();
		HCNetworking.init();
	}
}
