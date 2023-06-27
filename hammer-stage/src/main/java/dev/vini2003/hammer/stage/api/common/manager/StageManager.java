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

package dev.vini2003.hammer.stage.api.common.manager;

import com.mojang.datafixers.types.Func;
import dev.vini2003.hammer.stage.api.common.stage.Stage;
import net.minecraft.util.Identifier;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class StageManager {
	private static final Map<Identifier, Function<World, Stage>> FACTORIES = new HashMap<>();
	
	private static final Map<RegistryKey<World>, Stage> ACTIVES = new HashMap<>();
	
	/**
	 * Registers a stage supplier.
	 * @param id The ID of the stage.
	 * @param supplier The supplier of the stage.
	 */
	public static void register(Identifier id, Function<World, Stage> supplier) {
		FACTORIES.put(id, supplier);
	}
	
	/**
	 * Creates a new stage with the given ID.
	 * @param id The ID of the stage.
	 * @return The new stage.
	 */
	public static Stage create(Identifier id, World world) {
		return FACTORIES.get(id).apply(world);
	}
	
	/**
	 * Returns the currently active stage.
	 * @return The currently active stage.
	 */
	public static Stage getActive(RegistryKey<World> world) {
		return ACTIVES.get(world);
	}
	
	/**
	 * Sets the currently active stage.
	 * @param active The new active stage.
	 */
	public static void setActive(RegistryKey<World> world, Stage active) {
		ACTIVES.put(world, active);
	}
	
	/**
	 * Returns all registered stage factories.
	 * @return All registered stage factories.
	 */
	public static Map<Identifier, Function<World, Stage>> getFactories() {
		return FACTORIES;
	}
}
