package dev.vini2003.hammer.core.api.common.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

import java.util.List;

public class FireworkUtil {
	public enum Type {
		SMALL_BALL,
		LARGE_BALL,
		STAR,
		CREEPER,
		BURST
	}
	
	private static final String KEY_TYPE = "Type";
	private static final String KEY_COLORS = "Colors";
	private static final String KEY_FADE_COLORS = "FadeColors";
	private static final String KEY_FLICKER = "Flicker";
	private static final String KEY_TRAIL = "Trail";
	private static final String KEY_FIREWORKS = "Fireworks";
	private static final String KEY_FLIGHT = "Flight";
	private static final String KEY_EXPLOSIONS = "Explosions";
	
	public static class FireworkBuilder {
		private final ItemStack firework;
		
		private final NbtList explosions;
		
		public FireworkBuilder() {
			firework = new ItemStack(Items.FIREWORK_ROCKET);
			explosions = new NbtList();
		}
		
		public FireworkBuilder addSmallBallExplosion(List<Integer> colors, List<Integer> fadeColors, boolean flicker, boolean trail) {
			return addExplosion(Type.SMALL_BALL, colors, fadeColors, flicker, trail);
		}
		
		public FireworkBuilder addLargeBallExplosion(List<Integer> colors, List<Integer> fadeColors, boolean flicker, boolean trail) {
			return addExplosion(Type.LARGE_BALL, colors, fadeColors, flicker, trail);
		}
		
		public FireworkBuilder addStarExplosion(List<Integer> colors, List<Integer> fadeColors, boolean flicker, boolean trail) {
			return addExplosion(Type.STAR, colors, fadeColors, flicker, trail);
		}
		
		public FireworkBuilder addCreeperExplosion(List<Integer> colors, List<Integer> fadeColors, boolean flicker, boolean trail) {
			return addExplosion(Type.CREEPER, colors, fadeColors, flicker, trail);
		}
		
		public FireworkBuilder addBurstExplosion(List<Integer> colors, List<Integer> fadeColors, boolean flicker, boolean trail) {
			return addExplosion(Type.BURST, colors, fadeColors, flicker, trail);
		}
		
		
		public FireworkBuilder addExplosion(Type type, List<Integer> colors, List<Integer> fadeColors, boolean flicker, boolean trail) {
			var explosion = new NbtCompound();
			
			explosion.putByte(KEY_TYPE, (byte) type.ordinal());
			explosion.putIntArray(KEY_COLORS, colors);
			explosion.putIntArray(KEY_FADE_COLORS, fadeColors);
			explosion.putBoolean(KEY_FLICKER, flicker);
			explosion.putBoolean(KEY_TRAIL, trail);
			
			explosions.add(explosion);
			
			return this;
		}
		
		public FireworkBuilder setFlightDuration(int duration) {
			var fireworkTag = firework.getOrCreateSubNbt(KEY_FIREWORKS);
			fireworkTag.putByte(KEY_FLIGHT, (byte) duration);
			return this;
		}
		
		public ItemStack build() {
			var fireworkTag = firework.getOrCreateSubNbt(KEY_FIREWORKS);
			fireworkTag.put(KEY_EXPLOSIONS, explosions);
			return firework;
		}
	}
	
	public static FireworkBuilder builder() {
		return new FireworkBuilder();
	}
}
