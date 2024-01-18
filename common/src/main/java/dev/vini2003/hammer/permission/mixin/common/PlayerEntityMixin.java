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

package dev.vini2003.hammer.permission.mixin.common;

import dev.architectury.networking.NetworkManager;
import dev.vini2003.hammer.core.api.common.component.TrackedDataComponent;
import dev.vini2003.hammer.core.api.common.data.TrackedDataHandler;
import dev.vini2003.hammer.permission.api.common.event.RoleEvents;
import dev.vini2003.hammer.permission.api.common.manager.RoleManager;
import dev.vini2003.hammer.permission.api.common.role.Role;
import dev.vini2003.hammer.permission.impl.common.accessor.PlayerEntityAccessor;
import dev.vini2003.hammer.permission.registry.common.HPNetworking;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityAccessor {
	private final TrackedDataHandler<Boolean> hammer$roleOutline = new TrackedDataHandler<>(() -> TrackedDataComponent.get(this), Boolean.class, false, "RoleOutline");
	
	private PlayerEntity hammer$self() {
		return (PlayerEntity) (Object) this;
	}
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	public void hammer$setRoleOutline(boolean roleOutline) {
		hammer$roleOutline.set(roleOutline);
	}
	
	@Override
	public boolean hammer$hasRoleOutline() {
		return hammer$roleOutline.get();
	}
	
	@Inject(at = @At("RETURN"), method = "getDisplayName", cancellable = true)
	private void hammer$getDisplayName(CallbackInfoReturnable<Text> cir) {
		var color = RoleManager.getRolePrefixColor(hammer$self());
		
		if (color != null) {
			var formatted = ((MutableText) cir.getReturnValue()).styled((style) -> style.withColor(color));
			
			cir.setReturnValue(formatted);
		}
	}
	
	@Override
	public boolean hammer$hasRole(Role role) {
		var self = hammer$self();
		
		return role.getHolders().contains(self.getUuid());
	}
	
	@Override
	public void hammer$addRole(Role role) {
		var self = hammer$self();
		
		
		var buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeString(role.getName());
		buf.writeUuid(self.getUuid());
			
		for (var otherPlayer : self.getServer().hammer$getPlayers()) {
			NetworkManager.sendToPlayer(otherPlayer, HPNetworking.ADD_ROLE, new PacketByteBuf(buf.duplicate()));
		}
			
		role.getHolders().add(self.getUuid());
		
		RoleEvents.ADD.invoker().onAdd(self, role);
	}
	
	@Override
	public void hammer$removeRole(Role role) {
		var self = hammer$self();
		
		var buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeString(role.getName());
		buf.writeUuid(self.getUuid());
		
		for (var otherPlayer : self.getServer().hammer$getPlayers()) {
			NetworkManager.sendToPlayer(otherPlayer, HPNetworking.REMOVE_ROLE, new PacketByteBuf(buf.duplicate()));
		}
		
		role.getHolders().remove(self.getUuid());
		
		RoleEvents.REMOVE.invoker().onRemove(self, role);
	}
	
	@Override
	public Collection<Role> hammer$getRoles() {
		return RoleManager.getRoles()
						  .stream()
						  .filter(this::hammer$hasRole)
						  .toList();
	}
}
