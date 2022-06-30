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

package dev.vini2003.hammer.component.api.common.manager;

import dev.vini2003.hammer.component.api.common.component.Component;
import dev.vini2003.hammer.component.api.common.component.key.ComponentKey;
import dev.vini2003.hammer.component.impl.common.util.ComponentUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ComponentManager {
	private static final Map<Identifier, ComponentKey<?>> KEYS = new HashMap<>();
	
	public static <C extends Component> ComponentKey<C> registerKey(Identifier id) {
		var key = new ComponentKey<C>(id);
		KEYS.put(id, key);
		return key;
	}
	
	@Nullable
	public static <C extends Component> ComponentKey<C> getKey(Identifier id) {
		return (ComponentKey<C>) KEYS.get(id);
	}
	
	public static <C extends Component> void registerForEntity(ComponentKey<C> key, Function<Entity, C> function) {
		ComponentUtil.ENTITY_ATTACHERS.put(key, function);
	}
	
	public static <C extends Component> void registerForWorld(ComponentKey<C> key, Function<World, C> function) {
		ComponentUtil.WORLD_ATTACHERS.put(key, function);
	}
}
