package dev.vini2003.hammer.component.api.common.component;

import dev.vini2003.hammer.component.impl.common.component.ComponentHolder;
import dev.vini2003.hammer.component.impl.common.misc.ComponentUtil;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import javax.annotation.Nullable;

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
			
			ComponentUtil.syncKey(buf);
		}
	}
}
