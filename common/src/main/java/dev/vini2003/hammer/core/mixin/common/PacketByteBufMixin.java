package dev.vini2003.hammer.core.mixin.common;

import dev.vini2003.hammer.core.impl.common.accessor.PacketByteBufAccessor;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PacketByteBuf.class)
public abstract class PacketByteBufMixin implements PacketByteBufAccessor {
	@Shadow
	public abstract PacketByteBuf writeIdentifier(Identifier id);
	
	@Shadow
	public abstract Identifier readIdentifier();
	
	@Shadow
	public abstract ByteBuf writeFloat(float value);
	
	@Shadow
	public abstract float readFloat();
	
	@Shadow
	public abstract ByteBuf writeDouble(double value);
	
	@Shadow
	public abstract double readDouble();
	
	@Shadow
	public abstract int readInt();
	
	@Shadow
	public abstract ByteBuf writeInt(int value);
	
	@Override
	public <T> void hammer$writeRegistryKey(RegistryKey<T> value) {
		writeIdentifier(value.getRegistry());
		writeIdentifier(value.getValue());
	}
	
	@Override
	public <T> RegistryKey<T> hammer$readRegistryKey() {
		var registryId = readIdentifier();
		var id = readIdentifier();
		
		return RegistryKey.of(RegistryKey.ofRegistry(registryId), id);
	}
	
	@Override
	public <T> void hammer$writeTagKey(TagKey<T> value) {
		writeIdentifier(value.registry().getValue());
		writeIdentifier(value.id());
	}
	
	@Override
	public <T> TagKey<T> hammer$readTagKey() {
		var registryId = readIdentifier();
		var tagId = readIdentifier();
		
		return TagKey.of(RegistryKey.ofRegistry(registryId), tagId);
	}
	
	@Override
	public void hammer$writeVec2f(Vec2f value) {
		writeFloat(value.x);
		writeFloat(value.y);
	}
	
	@Override
	public Vec2f hammer$readVec2f() {
		return new Vec2f(readFloat(), readFloat());
	}
	
	@Override
	public void hammer$writeVec3d(Vec3d value) {
		writeDouble(value.x);
		writeDouble(value.y);
		writeDouble(value.z);
	}
	
	@Override
	public Vec3d hammer$readVec3d() {
		return new Vec3d(readDouble(), readDouble(), readDouble());
	}
	
	@Override
	public void hammer$writeVector3f(Vec3f value) {
		writeFloat(value.getX());
		writeFloat(value.getY());
		writeFloat(value.getZ());
	}
	
	@Override
	public Vec3f hammer$readVector3f() {
		return new Vec3f(readFloat(), readFloat(), readFloat());
	}
	
	@Override
	public void hammer$writeVec3i(Vec3i value) {
		writeInt(value.getX());
		writeInt(value.getY());
		writeInt(value.getZ());
	}
	
	@Override
	public Vec3i hammer$readVec3i() {
		return new Vec3i(readInt(), readInt(), readInt());
	}
	
	@Override
	public void hammer$writeVector3d(Vector3d value) {
		writeDouble(value.x);
		writeDouble(value.y);
		writeDouble(value.z);
	}
	
	@Override
	public Vector3d hammer$readVector3d() {
		return new Vector3d(readDouble(), readDouble(), readDouble());
	}
	
	@Override
	public void hammer$writeVector4f(Vector4f value) {
		writeFloat(value.getX());
		writeFloat(value.getY());
		writeFloat(value.getZ());
		writeFloat(value.getW());
	}
	
	@Override
	public Vector4f hammer$readVector4f() {
		return new Vector4f(readFloat(), readFloat(), readFloat(), readFloat());
	}
	
	@Override
	public void hammer$writeQuaternionf(Quaternion value) {
		writeFloat(value.getX());
		writeFloat(value.getY());
		writeFloat(value.getZ());
		writeFloat(value.getY());
	}
	
	@Override
	public Quaternion hammer$readQuaternionf() {
		return new Quaternion(readFloat(), readFloat(), readFloat(), readFloat());
	}
}
