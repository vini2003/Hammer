package dev.vini2003.hammer.gui.api.common.widget.text;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.texture.PartitionedTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.common.supplier.TextureSupplier;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public class TextFieldWidget extends TextEditorWidget {
	public static final Texture STANDARD_TEXTURE = new PartitionedTexture(HC.id("textures/widget/text_field.png"), 18.0F, 18.0F, 0.055F, 0.055F, 0.055F, 0.055F);
	
	protected TextureSupplier texture = () -> STANDARD_TEXTURE;
	
	protected int length = 15;
	
	@Override
	public void setText(String text) {
		text = text.replace("\n", "");
		
		if (length >= 0 && length <= text.length()) {
			text = text.substring(0, length);
		}
		
		super.setText(text);
	}
	
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {
		texture.get().draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
		
		renderField(matrices, provider);
	}
	
	public void setLength(int length) {
		this.length = length;
	}
}
