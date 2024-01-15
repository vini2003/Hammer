package dev.vini2003.hammer.core.mixin.common;

import dev.vini2003.hammer.core.impl.common.accessor.NbtCompoundAccessor;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NbtCompound.class)
public abstract class NbtCompoundMixin implements NbtCompoundAccessor {
	@Shadow
	public abstract void putInt(String key, int value);
	
	@Shadow
	public abstract int getInt(String key);
	
	@Shadow
	public abstract void putString(String key, String value);
	
	@Shadow
	public abstract String getString(String key);
	
	@Shadow
	public abstract void putFloat(String key, float value);
	
	@Shadow
	public abstract float getFloat(String key);
	
	@Shadow
	public abstract void putDouble(String key, double value);
	
	@Shadow
	public abstract double getDouble(String key);
	
	@Override
	public void hammer$putBlockPos(String key, BlockPos value) {
		putInt(key + "X", value.getX());
		putInt(key + "Y", value.getY());
		putInt(key + "Z", value.getZ());
	}
	
	@Override
	public BlockPos hammer$getBlockPos(String key) {
		return new BlockPos(
				getInt(key + "X"),
				getInt(key + "Y"),
				getInt(key + "Z")
		);
	}
	
	@Override
	public void hammer$putChunkPos(String key, BlockPos value) {
		putInt(key + "X", value.getX());
		putInt(key + "Z", value.getZ());
	}
	
	@Override
	public ChunkPos hammer$getChestPos(String key) {
		return new ChunkPos(
				getInt(key + "X"),
				getInt(key + "Z")
		);
	}
	
	@Override
	public void hammer$putIdentifier(String key, Identifier value) {
		putString(key, value.toString());
	}
	
	@Override
	public Identifier hammer$getIdentifier(String key) {
		return new Identifier(getString(key));
	}
	
	@Override
	public <T> void hammer$putRegistryKey(String key, RegistryKey<T> value) {
		putString(key + "RegistryId", value.getRegistry().toString());
		putString(key + "Id", value.getValue().toString());
	}
	
	@Override
	public <T> RegistryKey<T> hammer$getRegistryKey(String key) {
		var registryId = new Identifier(getString(key + "RegistryId"));
		var id = new Identifier(getString(key + "Id"));
		
		return RegistryKey.of(RegistryKey.ofRegistry(registryId), id);
	}
	
	@Override
	public <T> void hammer$putTagKey(String key, RegistryKey<T> value) {
		putString(key + "RegistryId", value.getRegistry().toString());
		putString(key + "TagId", value.getValue().toString());
	}
	
	@Override
	public <T> TagKey<T> hammer$getTagKey(String key) {
		var registryId = new Identifier(getString(key + "RegistryId"));
		var tagId = new Identifier(getString(key + "TagKey"));
		
		return TagKey.of(RegistryKey.ofRegistry(registryId), tagId);
	}
	
	@Override
	public void hammer$putVec2f(String key, Vec2f value) {
		putFloat(key + "X", value.x);
		putFloat(key + "Y", value.y);
	}
	
	@Override
	public Vec2f hammer$getVec2f(String key) {
		return new Vec2f(
				getFloat(key + "X"),
				getFloat(key + "Y")
		);
	}
	
	@Override
	public void hammer$putVec3d(String key, Vec3d value) {
		putDouble(key + "X", value.x);
		putDouble(key + "Y", value.y);
		putDouble(key + "Z", value.z);
	}
	
	@Override
	public Vec3d hammer$getVec3d(String key) {
		return new Vec3d(
				getDouble(key + "X"),
				getDouble(key + "Y"),
				getDouble(key + "Z")
		);
	}
	
	@Override
	public void hammer$putVector3f(String key, Vec3f value) {
		putFloat(key + "X", value.getX());
		putFloat(key + "Y", value.getY());
		putFloat(key + "Z", value.getZ());
	}
	
	@Override
	public Vec3f hammer$getVector3f(String key) {
		return new Vec3f(
				getFloat(key + "X"),
				getFloat(key + "Y"),
				getFloat(key + "Z")
		);
	}
	
	@Override
	public void hammer$putVec3i(String key, Vec3i value) {
		putInt(key + "X", value.getX());
		putInt(key + "Y", value.getY());
		putInt(key + "Z", value.getZ());
	}
	
	@Override
	public Vec3i hammer$getVec3i(String key) {
		return new Vec3i(
				getInt(key + "X"),
				getInt(key + "Y"),
				getInt(key + "Z")
		);
	}
	
	@Override
	public void hammer$putVector3d(String key, Vec3d value) {
		putDouble(key + "X", value.x);
		putDouble(key + "Y", value.y);
		putDouble(key + "Z", value.z);
	}
	
	@Override
	public Vec3d hammer$getVector3d(String key) {
		return new Vec3d(
				getDouble(key + "X"),
				getDouble(key + "Y"),
				getDouble(key + "Z")
		);
	}
	
	@Override
	public void hammer$putVector4f(String key, Vector4f value) {
		putFloat(key + "X", value.getX());
		putFloat(key + "Y", value.getY());
		putFloat(key + "Z", value.getZ());
		putFloat(key + "W", value.getW());
	}
	
	@Override
	public Vector4f hammer$getVector4f(String key) {
		return new Vector4f(
				getFloat(key + "X"),
				getFloat(key + "Y"),
				getFloat(key + "Z"),
				getFloat(key + "W")
		);
	}
	
	@Override
	public void hammer$putQuaternionf(String key, Quaternion value) {
		putFloat(key + "X", value.getX());
		putFloat(key + "Y", value.getY());
		putFloat(key + "Z", value.getZ());
		putFloat(key + "W", value.getW());
	}
	
	@Override
	public Quaternion hammer$getQuaternionf(String key) {
		return new Quaternion(
				getFloat(key + "X"),
				getFloat(key + "Y"),
				getFloat(key + "Z"),
				getFloat(key + "W")
		);
	}
}
