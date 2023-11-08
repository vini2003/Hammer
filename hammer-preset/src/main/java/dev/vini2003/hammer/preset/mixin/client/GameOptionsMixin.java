package dev.vini2003.hammer.preset.mixin.client;

import com.mojang.serialization.Codec;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.resource.VideoWarningManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.minecraft.client.option.GraphicsMode.FABULOUS;
import static net.minecraft.client.option.GraphicsMode.FANCY;

@Mixin(GameOptions.class)
public class GameOptionsMixin {
	@Mutable
	@Shadow
	@Final
	private SimpleOption<GraphicsMode> graphicsMode;
	
	@Shadow
	@Final
	private static Text FAST_GRAPHICS_TOOLTIP;
	
	@Shadow
	@Final
	private static Text FANCY_GRAPHICS_TOOLTIP;
	
	@Shadow
	@Final
	private static Text FABULOUS_GRAPHICS_TOOLTIP;
	
	@Inject(at = @At("TAIL"), method = "<init>")
	private void hammer$clinit(CallbackInfo ci) {
		if (true) return;
		// Screw it!
		
		graphicsMode = new SimpleOption<GraphicsMode>("options.graphics", (graphicsMode) -> {
			return switch (graphicsMode) {
				case FANCY -> Tooltip.of(FANCY_GRAPHICS_TOOLTIP);
				case FAST -> Tooltip.of(FAST_GRAPHICS_TOOLTIP);
				case FABULOUS -> Tooltip.of(FABULOUS_GRAPHICS_TOOLTIP);
			};
		}, (optionText, value) -> {
			MutableText mutableText = Text.translatable(value.getTranslationKey());
			return value == FABULOUS ? mutableText.formatted(Formatting.ITALIC) : mutableText;
		}, new SimpleOption.AlternateValuesSupportingCyclingCallbacks(Arrays.stream(GraphicsMode.values()).filter(it -> it != FABULOUS).collect(Collectors.toList()), Stream.of(GraphicsMode.values()).filter((graphicsMode) -> {
			return graphicsMode != FABULOUS;
		}).collect(Collectors.toList()), () -> {
			return MinecraftClient.getInstance().isRunning() && MinecraftClient.getInstance().getVideoWarningManager().hasCancelledAfterWarning();
		}, (option, graphicsMode) -> {
			MinecraftClient minecraftClient = MinecraftClient.getInstance();
			VideoWarningManager videoWarningManager = minecraftClient.getVideoWarningManager();
			if (graphicsMode == FABULOUS && videoWarningManager.canWarn()) {
				videoWarningManager.scheduleWarning();
			} else {
				option.setValue(graphicsMode);
				minecraftClient.worldRenderer.reload();
			}
		}, Codec.INT.xmap(GraphicsMode::byId, GraphicsMode::getId)), FANCY, (value) -> {
		});
	}
}
