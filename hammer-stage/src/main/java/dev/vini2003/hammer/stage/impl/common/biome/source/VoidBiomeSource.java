package dev.vini2003.hammer.stage.impl.common.biome.source;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;

public class VoidBiomeSource extends BiomeSource {
	public static final Codec<VoidBiomeSource> CODEC = RecordCodecBuilder.create((instance) ->
			instance.group(
					RegistryOps.createRegistryCodec(Registry.BIOME_KEY).forGetter((biomeSource) -> biomeSource.registry)
			).apply(instance, instance.stable(VoidBiomeSource::new)));
	
	private final Registry<Biome> registry;
	
	public VoidBiomeSource(Registry<Biome> registry) {
		super(ImmutableList.of(registry.getOrCreateEntry(BiomeKeys.PLAINS)));
		
		this.registry = registry;
	}
	
	@Override
	protected Codec<? extends BiomeSource> getCodec() {
		return CODEC;
	}
	
	@Override
	public RegistryEntry<Biome> getBiome(int x, int y, int z, MultiNoiseUtil.MultiNoiseSampler noise) {
		return registry.getEntry(BiomeKeys.PLAINS).orElseThrow();
	}
	
}
