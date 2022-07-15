package dev.vini2003.hammer.preset.mixin.client;

import dev.vini2003.hammer.preset.impl.client.widget.ObfuscatedTextFieldWidget;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AddServerScreen.class)
public abstract class AddServerScreenMixin extends Screen {
	@Shadow
	@Final
	private ServerInfo server;
	
	@Shadow
	protected abstract void updateAddButton();
	
	@Shadow
	private TextFieldWidget addressField;
	private ObfuscatedTextFieldWidget hammer$obfuscatedAddressField;
	
	protected AddServerScreenMixin(Text text) {
		super(text);
	}
	
	@Inject(at = @At("HEAD"), method = "init()V")
	private void hammer$init(CallbackInfo ci) {
		this.hammer$obfuscatedAddressField = new ObfuscatedTextFieldWidget(this.textRenderer, this.width / 2 - 100, 106, 200, 20, new TranslatableText("addServer.enterIp"));
		this.hammer$obfuscatedAddressField.setMaxLength(128);
		this.hammer$obfuscatedAddressField.setText(this.server.address);
		this.hammer$obfuscatedAddressField.setChangedListener(address -> this.updateAddButton());
	}
	
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/AddServerScreen;addSelectableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;", ordinal = 1), method = "init")
	private <T extends Element & Selectable> Element hammer$init$addSelectableChild(AddServerScreen instance, T element) {
		addressField = hammer$obfuscatedAddressField;
		
		addSelectableChild(hammer$obfuscatedAddressField);
		
		return hammer$obfuscatedAddressField;
	}
}
