package dev.vini2003.hammer.stage.registry.common;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.stage.impl.common.biome.source.VoidBiomeSource;
import dev.vini2003.hammer.stage.impl.common.chunk.generator.VoidChunkGenerator;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalLong;
import java.util.function.Supplier;

public class HSDimensions {
	public static final Map<RegistryKey<DimensionOptions>, Supplier<DimensionOptions>> DIMENSIONS = new HashMap<>();
	
	public static final RegistryKey<DimensionOptions> VOID_DIMENSION_OPTIONS_KEY = RegistryKey.of(Registry.DIMENSION_KEY, HC.id("void"));
	public static final RegistryKey<DimensionType> VOID_DIMENSION_TYPE_KEY = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, HC.id("void"));
	public static final RegistryKey<World> VOID_WORLD_KEY = RegistryKey.of(Registry.WORLD_KEY, HC.id("void"));
	
	public static final DimensionType.MonsterSettings VOID_DIMENSION_TYPE_MONSTER_SETTINGS = new DimensionType.MonsterSettings(false, true, UniformIntProvider.create(0, 7), 0);
	public static final DimensionType VOID_DIMENSION_TYPE = new DimensionType(OptionalLong.empty(), true, false, false, false, 1, true, true, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD, HC.id("void"), 15, VOID_DIMENSION_TYPE_MONSTER_SETTINGS);
	
	public static void init() {
		DIMENSIONS.put(VOID_DIMENSION_OPTIONS_KEY, () -> new DimensionOptions(BuiltinRegistries.DIMENSION_TYPE.getOrCreateEntry(VOID_DIMENSION_TYPE_KEY), new VoidChunkGenerator(BuiltinRegistries.STRUCTURE_SET, new VoidBiomeSource(BuiltinRegistries.BIOME))));
	}
}
