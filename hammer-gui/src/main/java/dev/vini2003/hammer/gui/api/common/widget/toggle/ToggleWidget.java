/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 vini2003
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.vini2003.hammer.gui.api.common.widget.toggle;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.texture.PartitionedTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.client.util.DrawingUtil;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.core.api.common.util.TextUtil;
import dev.vini2003.hammer.gui.api.common.event.MouseClickedEvent;
import dev.vini2003.hammer.gui.api.common.event.type.EventType;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import dev.vini2003.hammer.gui.api.common.widget.provider.DisabledProvider;
import dev.vini2003.hammer.gui.api.common.widget.provider.DisabledTextureProvider;
import dev.vini2003.hammer.gui.api.common.widget.provider.EnabledTextureProvider;
import dev.vini2003.hammer.gui.api.common.widget.provider.LabelProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class ToggleWidget extends Widget implements EnabledTextureProvider, DisabledTextureProvider, DisabledProvider, LabelProvider {
	public static final Size STANDARD_SIZE = Size.of(18.0F, 18.0F);
	
	public static final Texture STANDARD_ENABLED_TEXTURE = new PartitionedTexture(HC.id("textures/widget/toggle_enabled.png"), 18.0F, 18.0F, 0.11F, 0.11F, 0.11F, 0.16F);
	public static final Texture STANDARD_DISABLED_TEXTURE = new PartitionedTexture(HC.id("textures/widget/toggle_disabled.png"), 18.0F, 18.0F, 0.11F, 0.11F, 0.11F, 0.16F);

	protected Supplier<Texture> enabledTexture = () -> STANDARD_ENABLED_TEXTURE;
	protected Supplier<Texture> disabledTexture = () -> STANDARD_DISABLED_TEXTURE;
	
	protected BooleanSupplier disabled = () -> true;
	
	protected Supplier<Text> label = () -> null;
	
	@Override
	public Size getStandardSize() {
		return STANDARD_SIZE;
	}
	
	@Override
	protected void onMouseClicked(MouseClickedEvent event) {
		if (isFocused() && rootCollection != null && rootCollection.isScreenHandler()) {
			if (rootCollection.isClient()) {
				playSound();
			}
		}
		
		if (isFocused()) {
			var isDisabled = isDisabled();
			setDisabled(() -> !isDisabled);
		}
	}
	
	@Override
	public boolean shouldSync(EventType type) {
		return type == EventType.MOUSE_CLICKED;
	}
	
	@Override 
	@Environment(EnvType.CLIENT)
	public void draw(DrawContext context, float tickDelta) {
		onBeginDraw(context, tickDelta);
		
		var matrices = context.getMatrices();
		var provider = context.getVertexConsumers();
		
		var texture = (Texture) null;
		
		if (isDisabled()) {
			texture = disabledTexture.get();
		} else {
			texture = enabledTexture.get();
		}
		
		texture.draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
		
		provider.draw();
		
		var label = this.label.get();
		
		if (label != null) {
			var textRenderer = DrawingUtil.getTextRenderer();
			
			// TODO: Check if this is equivalent to the 1.19.2 code.
			context.drawTextWithShadow(textRenderer, label.asOrderedText(), (int) (getX() + (getWidth() / 2.0F - TextUtil.getWidth(label) / 2.0F)), (int) (getY() + (getHeight() / 2.0F - TextUtil.getHeight(label) / 2.0F)), 0xFCFCFC);
		}
		
		onEndDraw(context, tickDelta);
	}
	
	@Environment(EnvType.CLIENT)
	protected void playSound() {
		var client = InstanceUtil.getClient();
		
		client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}
	
	@Override
	public Supplier<Texture> getEnabledTexture() {
		return enabledTexture;
	}
	
	@Override
	public void setEnabledTexture(Supplier<Texture> enabledTexture) {
		this.enabledTexture = enabledTexture;
	}
	
	@Override
	public Supplier<Texture> getDisabledTexture() {
		return disabledTexture;
	}
	
	@Override
	public void setDisabledTexture(Supplier<Texture> disabledTexture) {
		this.disabledTexture = disabledTexture;
	}
	
	@Override
	public boolean isDisabled() {
		return disabled.getAsBoolean();
	}
	
	public boolean isEnabled() {
		return !isDisabled();
	}
	
	@Override
	public void setDisabled(BooleanSupplier disabled) {
		this.disabled = disabled;
	}
	
	@Override
	public BooleanSupplier getDisabled() {
		return disabled;
	}
	
	@Override
	public Supplier<Text> getLabel() {
		return label;
	}
	
	@Override
	public void setLabel(Supplier<Text> label) {
		this.label = label;
	}
}
