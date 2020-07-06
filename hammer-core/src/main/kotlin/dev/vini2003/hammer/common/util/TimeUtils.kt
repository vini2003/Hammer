package dev.vini2003.hammer.common.util

import net.minecraft.server.world.ServerWorld
import net.minecraft.world.GameRules

object TimeUtils {
	fun pause(world: ServerWorld) {
		disableWeatherCycle(world)
		disableDaylightCycle(world)
	}
	
	fun resume(world: ServerWorld) {
		enableWeatherCycle(world)
		enableDaylightCycle(world)
	}
	
	fun disableWeatherCycle(world: ServerWorld) {
		GameRuleUtils.disable(world, GameRules.DO_WEATHER_CYCLE)
	}
	
	fun enableWeatherCycle(world: ServerWorld) {
		GameRuleUtils.enable(world, GameRules.DO_WEATHER_CYCLE)
	}
	
	fun disableDaylightCycle(world: ServerWorld) {
		GameRuleUtils.disable(world, GameRules.DO_DAYLIGHT_CYCLE)
	}
	
	fun enableDaylightCycle(world: ServerWorld) {
		GameRuleUtils.enable(world, GameRules.DO_DAYLIGHT_CYCLE)
	}
	
	fun makeClearDay(world: ServerWorld) {
		world.timeOfDay = 1000
		world.setWeather(0, 99999, false, false)
	}
	
	fun makeClearNight(world: ServerWorld) {
		world.timeOfDay = 18000
		world.setWeather(0, 99999, false, false)
	}
	
	fun makeRainingDay(world: ServerWorld) {
		world.timeOfDay = 1000
		world.setWeather(0, 99999, true, false)
	}
	
	fun makeThunderingDay(world: ServerWorld) {
		world.timeOfDay = 1000
		world.setWeather(0, 99999, true, true)
	}
	
	fun makeRainingNight(world: ServerWorld) {
		world.timeOfDay = 18000
		world.setWeather(0, 99999, true, false)
	}
	
	fun makeThunderingNight(world: ServerWorld) {
		world.timeOfDay = 18000
		world.setWeather(0, 99999, true, true);
	}
}