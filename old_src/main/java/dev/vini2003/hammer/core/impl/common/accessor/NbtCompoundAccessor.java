package dev.vini2003.hammer.core.impl.common.accessor;

import dev.vini2003.hammer.core.api.common.exception.NoMixinException;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4f;

public interface NbtCompoundAccessor {
	default void hammer$putBlockPos(String key, BlockPos value) {
		throw new NoMixinException();
	}
	
	default BlockPos hammer$getBlockPos(String key) {
		throw new NoMixinException();
	}
	
	default void hammer$putChunkPos(String key, BlockPos value) {
		throw new NoMixinException();
	}
	
	default ChunkPos hammer$getChestPos(String key) {
		throw new NoMixinException();
	}
	
	default void hammer$putIdentifier(String key, Identifier value) {
		throw new NoMixinException();
	}
	
	default Identifier hammer$getIdentifier(String key) {
		throw new NoMixinException();
	}
	
	default <T> void hammer$putRegistryKey(String key, RegistryKey<T> value) {
		throw new NoMixinException();
	}
	
	default <T> RegistryKey<T> hammer$getRegistryKey(String key) {
		throw new NoMixinException();
	}
	
	default <T> void hammer$putTagKey(String key, RegistryKey<T> value) {
		throw new NoMixinException();
	}
	
	default <T> TagKey<T> hammer$getTagKey(String key) {
		throw new NoMixinException();
	}
	
	default void hammer$putVec2f(String key, Vec2f value) {
		throw new NoMixinException();
	}
	
	default Vec2f hammer$getVec2f(String key) {
		throw new NoMixinException();
	}
	
	default void hammer$putVec3d(String key, Vec3d value) {
		throw new NoMixinException();
	}
	
	default Vec3d hammer$getVec3d(String key) {
		throw new NoMixinException();
	}
	
	default void hammer$putVector3f(String key, Vector3f value) {
		throw new NoMixinException();
	}
	
	default Vector3f hammer$getVector3f(String key) {
		throw new NoMixinException();
	}
	
	default void hammer$putVec3i(String key, Vec3i value) {
		throw new NoMixinException();
	}
	
	default Vec3i hammer$getVec3i(String key) {
		throw new NoMixinException();
	}
	
	default void hammer$putVector3d(String key, Vector3d value) {
		throw new NoMixinException();
	}
	
	default Vector3d hammer$getVector3d(String key) {
		throw new NoMixinException();
	}
	
	default void hammer$putVector4f(String key, Vector4f value) {
		throw new NoMixinException();
	}
	
	default Vector4f hammer$getVector4f(String key) {
		throw new NoMixinException();
	}
	
	default void hammer$putQuaternionf(String key, Quaternionf value) {
		throw new NoMixinException();
	}
	
	default Quaternionf hammer$getQuaternionf(String key) {
		throw new NoMixinException();
	}
}
