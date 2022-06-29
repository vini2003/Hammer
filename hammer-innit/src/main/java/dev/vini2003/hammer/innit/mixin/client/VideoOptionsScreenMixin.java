package dev.vini2003.hammer.innit.mixin.client;

import com.google.common.collect.ImmutableList;
import dev.vini2003.hammer.core.api.client.util.DrawingUtil;
import dev.vini2003.hammer.core.api.common.util.TextUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.option.Option;
import net.minecraft.client.resource.VideoWarningManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.minecraft.client.gui.DrawableHelper.drawCenteredText;

@Mixin(VideoOptionsScreen.class)
public abstract class VideoOptionsScreenMixin extends GameOptionsScreen {
	@Shadow
	@Final
	private static Option[] OPTIONS;
	
	private static final Text HAMMER$FAST_GRAPHICS_TOOLTIP = new TranslatableText("options.graphics.fast.tooltip");
	private static final Text HAMMER$FANCY_GRAPHICS_TOOLTIP = new TranslatableText("options.graphics.fancy.tooltip");;
	
	private static final Option HAMMER$GRAPHICS = CyclingOption.create("options.graphics", Stream.of(GraphicsMode.values()).filter(graphicsMode -> graphicsMode != GraphicsMode.FABULOUS).collect(Collectors.toList()),
	mode -> {
		var text = new TranslatableText(mode.getTranslationKey());
		
		if (mode == GraphicsMode.FABULOUS) {
			return text.formatted(Formatting.ITALIC);
		}
		
		return text;
	}, gameOptions -> gameOptions.graphicsMode, (gameOptions, option, mode) -> {
		var client = MinecraftClient.getInstance();
		var manager = client.getVideoWarningManager();
		
		if (mode == GraphicsMode.FABULOUS && manager.canWarn()) {
			manager.scheduleWarning();
			
			return;
		}
		
		gameOptions.graphicsMode = mode;
		
		client.worldRenderer.reload();
	}).tooltip(client -> {
		return graphicsMode -> {
			switch (graphicsMode) {
				case FAST: {
					return client.textRenderer.wrapLines(HAMMER$FAST_GRAPHICS_TOOLTIP, 200);
				}
				
				case FANCY: {
					return client.textRenderer.wrapLines(HAMMER$FANCY_GRAPHICS_TOOLTIP, 200);
				}
			}
			
			return ImmutableList.of();
		};
	});
	
	public VideoOptionsScreenMixin(Screen screen, GameOptions gameOptions, Text text) {
		super(screen, gameOptions, text);
	}
	
	@Inject(at = @At("TAIL"), method = "<clinit>")
	private static void hammer$init(CallbackInfo ci) {
		OPTIONS[0] = HAMMER$GRAPHICS;
	}
	
	@Inject(at = @At("RETURN"), method = "render")
	private void hammer$render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		drawCenteredText(matrices, this.textRenderer, new TranslatableText("text.hammer.fabulous_graphics_warning"), this.width / 2, 5 + 9 + 5, 0xFF5370);
	}
}
