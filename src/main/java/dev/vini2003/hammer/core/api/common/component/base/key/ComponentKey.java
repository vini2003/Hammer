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

package dev.vini2003.hammer.core.api.common.component.base.key;

import dev.vini2003.hammer.core.api.common.component.base.Component;
import dev.vini2003.hammer.core.impl.common.component.holder.ComponentHolder;
import dev.vini2003.hammer.core.impl.common.util.ComponentUtil;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link ComponentKey} is the key with which a {@link Component}'s instances
 * are associated.
 */
public class ComponentKey<C extends Component> {
	private Identifier id;
	
	public ComponentKey(Identifier id) {
		this.id = id;
	}
	
	public Identifier getId() {
		return id;
	}
	
	/**
	 * <p>Returns the associated component in the given object,
	 * if any.</p>
	 *
	 * @param obj the object.
	 * @return the component, or <code>null</code> if not present.
	 */
	@Nullable
	public C get(Object obj) {
		if (obj instanceof ComponentHolder holder) {
			return holder.getComponentContainer().get(this);
		} else {
			return null;
		}
	}
	
	/**
	 * <p>Synchronizes the associated component in the given object,
	 * if any.</p>
	 *
	 * @param obj the object.
	 */
	public void sync(Object obj) {
		if (obj instanceof ComponentHolder holder) {
			var component = holder.getComponentContainer().get(this);
			
			var componentNbt = new NbtCompound();
			component.writeToNbt(componentNbt);
			
			var buf = PacketByteBufs.create();
			buf.writeIdentifier(id);
			
			if (obj instanceof Entity) {
				buf.writeInt(ComponentHolder.ENTITY);
			}
			
			if (obj instanceof World) {
				buf.writeInt(ComponentHolder.WORLD);
			}
			
			buf.writeNbt(componentNbt);
			
			if (obj instanceof Entity entity) {
				buf.writeInt(entity.getId());
			}
			
			if (obj instanceof Entity entity) {
				ComponentUtil.syncKey(entity.getWorld(), buf);
			} else if (obj instanceof World world) {
				ComponentUtil.syncKey(world, buf);
			}
		}
	}
}
