package dev.vini2003.hammer.core.api.common.supplier;

import dev.vini2003.hammer.core.api.client.texture.base.Texture;

import java.util.function.Supplier;

@FunctionalInterface
public interface TextureSupplier extends Supplier<Texture> {
}
