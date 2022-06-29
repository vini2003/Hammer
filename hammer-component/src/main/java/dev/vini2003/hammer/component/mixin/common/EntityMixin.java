package dev.vini2003.hammer.component.mixin.common;

import dev.vini2003.hammer.component.impl.common.misc.ComponentUtil;
import dev.vini2003.hammer.component.impl.common.component.ComponentContainer;
import dev.vini2003.hammer.component.impl.common.component.ComponentHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin implements ComponentHolder {
	private ComponentContainer hammer$componentContainer = new ComponentContainer();
	
	@Inject(at = @At("RETURN"), method = "<init>")
	private void hammer$init(EntityType<?> entityType, World world, CallbackInfo ci) {
		ComponentUtil.attachToEntity((Entity) (Object) this);
	}
	
	@Inject(at = @At("HEAD"), method = "copyFrom")
	private void hammer$copyFrom(Entity original, CallbackInfo ci) {
		if (original instanceof ComponentHolder originalHolder) {
			hammer$componentContainer = originalHolder.getComponentContainer();
		}
	}
	
	@Inject(method = "readNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
	public void readNbt(NbtCompound nbt, CallbackInfo ci) {
		hammer$componentContainer.readFromNbt(nbt);
	}
	
	@Inject(method = "writeNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
	public void writeNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
		hammer$componentContainer.writeToNbt(nbt);
	}
	
	@Override
	public ComponentContainer getComponentContainer() {
		return hammer$componentContainer;
	}
}
