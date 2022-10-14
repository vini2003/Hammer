package dev.vini2003.hammer.preset.mixin.client;

import dev.vini2003.hammer.preset.HP;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
	private TitleScreen hammer$self() {
		return (TitleScreen) (Object) this;
	}
	
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;", ordinal = 0), method = "initWidgetsNormal")
	private Element hammer$initWidgetsNormal$addDrawableChild(TitleScreen titleScreen, Element element) {
		if (HP.CONFIG.disableSinglePlayer) {
			return element;
		} else {
			return hammer$addDrawableChild(element);
		}
	}
	
	private <T extends Element & Drawable & Selectable> Element hammer$addDrawableChild(Object child) {
		return hammer$self().addDrawableChild((T) child);
	}
}
