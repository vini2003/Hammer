package dev.vini2003.hammer.core.impl.common.accessor;

import dev.vini2003.hammer.core.api.common.exception.NoMixinException;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4f;

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
	
	default void hammer$writeVector3f(Vector3f value) {
		throw new NoMixinException();
	}
	
	default Vector3f hammer$readVector3f() {
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
	
	default void hammer$writeQuaternionf(Quaternionf value) {
		throw new NoMixinException();
	}
	
	default Quaternionf hammer$readQuaternionf() {
		throw new NoMixinException();
	}
}
