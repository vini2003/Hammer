package dev.vini2003.hammer.gui.api.common.widget.text;

import dev.vini2003.hammer.core.api.client.util.DrawingUtil;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class TextWidget extends Widget {
	protected Supplier<Text> text = () -> null;
	
	protected boolean shadow = false;
	
	protected int color = 0x404040;
	
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {
		var textRenderer = DrawingUtil.getTextRenderer();
		
		if (text.get() != null) {
			if (shadow) {
				textRenderer.drawWithShadow(matrices, text.get(), getX(), getY(), color);
			} else {
				textRenderer.draw(matrices, text.get(), getX(), getY(), color);
			}
		}
	}
}
