package dev.vini2003.hammer.core.impl.common.accessor;

import dev.vini2003.hammer.core.api.common.exception.NoMixinException;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.RegistryKey;

public interface PacketByteBufAccessor {
	default <T> void hammer$writeRegistryKey(RegistryKey<T> value) {
		throw new NoMixinException();
	}
	
	default <T> RegistryKey<T> hammer$readRegistryKey() {
		throw new NoMixinException();
	}
	
	default <T> void hammer$writeTagKey(TagKey<T> value) {
		throw new NoMixinException();
	}
	
	default <T> TagKey<T> hammer$readTagKey() {
		throw new NoMixinException();
	}
	
	default void hammer$writeVec2f(Vec2f value) {
		throw new NoMixinException();
	}
	
	default Vec2f hammer$readVec2f() {
		throw new NoMixinException();
	}
	
	default void hammer$writeVec3d(Vec3d value) {
		throw new NoMixinException();
	}
	
	default Vec3d hammer$readVec3d() {
		throw new NoMixinException();
	}
	
	default void hammer$writeVector3f(Vec3f value) {
		throw new NoMixinException();
	}
	
	default Vec3f hammer$readVector3f() {
		throw new NoMixinException();
	}
	
	default void hammer$writeVec3i(Vec3i value) {
		throw new NoMixinException();
	}
	
	default Vec3i hammer$readVec3i() {
		throw new NoMixinException();
	}
	
	default void hammer$writeVector3d(Vector3d value) {
		throw new NoMixinException();
	}
	
	default Vector3d hammer$readVector3d() {
		throw new NoMixinException();
	}
	
	default void hammer$writeVector4f(Vector4f value) {
		throw new NoMixinException();
	}
	
	default Vector4f hammer$readVector4f() {
		throw new NoMixinException();
	}
	
	default void hammer$writeQuaternionf(Quaternion value) {
		throw new NoMixinException();
	}
	
	default Quaternion hammer$readQuaternionf() {
		throw new NoMixinException();
	}
}
