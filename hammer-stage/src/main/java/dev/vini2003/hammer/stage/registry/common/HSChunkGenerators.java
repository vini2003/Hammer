package dev.vini2003.hammer.stage.registry.common;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.stage.impl.common.chunk.generator.VoidChunkGenerator;
import net.minecraft.registry.Registry;

public class HSChunkGenerators {
	public static void init() {
		Registry.register(Registry.CHUNK_GENERATOR, HC.id("void"), VoidChunkGenerator.CODEC);
	}
}
