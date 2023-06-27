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

package dev.vini2003.hammer.gui.api.common.widget.button;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.texture.PartitionedTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.client.util.DrawingUtil;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.util.TextUtil;
import dev.vini2003.hammer.gui.api.common.event.MouseClickedEvent;
import dev.vini2003.hammer.gui.api.common.event.type.EventType;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import dev.vini2003.hammer.gui.api.common.widget.provider.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class ButtonWidget extends Widget implements EnabledTextureProvider, DisabledTextureProvider, FocusedTextureProvider, DisabledProvider, LabelProvider {
	public static final Texture STANDARD_ENABLED_TEXTURE = new PartitionedTexture(HC.id("textures/widget/button_enabled.png"), 18.0F, 18.0F, 0.11F, 0.11F, 0.11F, 0.16F);
	public static final Texture STANDARD_DISABLED_TEXTURE = new PartitionedTexture(HC.id("textures/widget/button_disabled.png"), 18.0F, 18.0F, 0.11F, 0.11F, 0.11F, 0.16F);
	public static final Texture STANDARD_FOCUSED_TEXTURE = new PartitionedTexture(HC.id("textures/widget/button_focused.png"), 18.0F, 18.0F, 0.11F, 0.11F, 0.11F, 0.16F);
	
	protected Supplier<Texture> enabledTexture = () -> STANDARD_ENABLED_TEXTURE;
	protected Supplier<Texture> disabledTexture = () -> STANDARD_DISABLED_TEXTURE;
	protected Supplier<Texture> focusedTexture = () -> STANDARD_FOCUSED_TEXTURE;
	
	protected BooleanSupplier disabled = () -> false;
	
	protected Supplier<Text> label = () -> null;
	
	@Override
	protected void onMouseClicked(MouseClickedEvent event) {
		if (isFocused() && rootCollection != null && rootCollection.isScreenHandler()) {
			if (rootCollection.isClient()) {
				playSound();
			}
		}
		
		if (isFocused()) {
			setDisabled(() -> !isDisabled());
		}
	}
	
	@Override
	public boolean shouldSync(EventType type) {
		return type == EventType.MOUSE_CLICKED;
	}
	
	@Override
	public void draw(DrawContext context, float tickDelta) {
		var matrices = context.getMatrices();
		var provider = context.getVertexConsumers();
		
		var texture = (Texture) null;
		
		if (isDisabled()) {
			texture = disabledTexture.get();
		} else if (isFocused()) {
			texture = focusedTexture.get();
		} else {
			texture = enabledTexture.get();
		}
		
		texture.draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
		
		// In 1.20.1, it is an Immediate by default.
		// if (provider instanceof VertexConsumerProvider.Immediate immediate) {
			provider.draw();
		// }
		
		var label = this.label.get();
		
		if (label != null) {
			// In 1.20.1, we must draw using the DrawContext.
			context.drawTextWithShadow(DrawingUtil.getTextRenderer(), label.asOrderedText(), (int) (getX() + (getWidth() / 2.0F - TextUtil.getWidth(label) / 2.0F)), (int) (getY() + (getHeight() / 2.0F - TextUtil.getHeight(label) / 2.0F)), 0xFCFCFC);
		}
	}
	
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
	public Supplier<Texture> getFocusedTexture() {
		return focusedTexture;
	}
	
	@Override
	public void setFocusedTexture(Supplier<Texture> focusedTexture) {
		this.focusedTexture = focusedTexture;
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
