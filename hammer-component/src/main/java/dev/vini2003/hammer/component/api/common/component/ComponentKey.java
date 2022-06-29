package dev.vini2003.hammer.component.api.common.component;

import dev.vini2003.hammer.component.impl.common.component.ComponentHolder;
import dev.vini2003.hammer.component.registry.common.HCNetworking;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collection;

public class ComponentKey<C extends Component> {
	private Identifier id;
	
	public ComponentKey(Identifier id) {
		this.id = id;
	}
	
	public Identifier getId() {
		return id;
	}
	
	@Nullable
	public C get(Object obj) {
		if (obj instanceof ComponentHolder holder) {
			return holder.getComponentContainer().get(this);
		} else {
			return null;
		}
	}
	
	public void sync(Object obj) {
		if (obj instanceof ComponentHolder holder) {
			var comp = holder.getComponentContainer().get(this);
			
			var compNbt = new NbtCompound();
			comp.writeToNbt(compNbt);
			
			var buf = PacketByteBufs.create();
			buf.writeIdentifier(id);
			
			if (obj instanceof Entity) {
				buf.writeInt(ComponentHolder.ENTITY);
			}
			
			if (obj instanceof World) {
				buf.writeInt(ComponentHolder.WORLD);
			}
			
			buf.writeNbt(compNbt);
			
			if (obj instanceof Entity entity) {
				buf.writeInt(entity.getId());
			}
			
			if (InstanceUtil.isServer()) {
				syncOnSever(buf);
			} else {
				syncOnClient(buf);
			}
		}
	}
	
	private void syncOnClient(PacketByteBuf buf) {
		var server = InstanceUtil.getClient().getServer();
		
		syncWith(server.getPlayerManager().getPlayerList(), buf);
	}
	
	private void syncOnSever(PacketByteBuf buf) {
		var server = InstanceUtil.getServer();
		
		syncWith(server.getPlayerManager().getPlayerList(), buf);
	}
	
	private void syncWith(Collection<ServerPlayerEntity> players, PacketByteBuf buf) {
		for (var p : players) {
			ServerPlayNetworking.send(p, HCNetworking.SYNC_COMPONENT, PacketByteBufs.duplicate(buf));
		}
	}
}
