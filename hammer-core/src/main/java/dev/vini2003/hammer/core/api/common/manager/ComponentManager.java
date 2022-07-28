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

package dev.vini2003.hammer.core.api.common.manager;

import dev.vini2003.hammer.core.api.common.component.base.Component;
import dev.vini2003.hammer.core.api.common.component.base.PlayerComponent;
import dev.vini2003.hammer.core.api.common.component.base.key.ComponentKey;
import dev.vini2003.hammer.core.impl.common.util.ComponentUtil;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A {@link ComponentManager} is a manager which manages {@link Component}s.
 * <br>
 * Components must extend {@link Component}, or, in the case of player components, {@link PlayerComponent}.
 * <br>
 * Components must have an associated key registered with {@link #registerKey(Identifier)}.
 * <br>
 * Components must be registered with {@link #registerForEntity(ComponentKey, Function)} or {@link #registerForWorld(ComponentKey, Function)}.
 */
public class ComponentManager {
	private static final Map<Identifier, ComponentKey<?>> KEYS = new HashMap<>();
	
	/**
	 * Registes a component key.
	 *
	 * @param id the key's ID.
	 *
	 * @return the key.
	 */
	public static <C extends Component> ComponentKey<C> registerKey(Identifier id) {
		var key = new ComponentKey<C>(id);
		KEYS.put(id, key);
		return key;
	}
	
	/**
	 * Returns the key associated with the given ID.
	 *
	 * @param id the key's ID..
	 *
	 * @return the key.
	 */
	@Nullable
	public static <C extends Component> ComponentKey<C> getKey(Identifier id) {
		return (ComponentKey<C>) KEYS.get(id);
	}
	
	/**
	 * Registers a component for entities.
	 * <br>
	 * <b>Returning <code>null</code> in the factory will skip the component for that given entity.</b>
	 *
	 * @param key     the component's key.
	 * @param factory the component factory.
	 */
	public static <C extends Component> void registerForEntity(ComponentKey<C> key, Function<Entity, C> factory) {
		ComponentUtil.ENTITY_ATTACHERS.put(key, factory);
	}
	
	/**
	 * Registers a component for worlds.
	 * <br>
	 * <b>Returning <code>null</code> in the factory will skip the component for that given world.</b>
	 *
	 * @param key     the component's key.
	 * @param factory the component factory.
	 */
	public static <C extends Component> void registerForWorld(ComponentKey<C> key, Function<World, C> factory) {
		ComponentUtil.WORLD_ATTACHERS.put(key, factory);
	}
}
