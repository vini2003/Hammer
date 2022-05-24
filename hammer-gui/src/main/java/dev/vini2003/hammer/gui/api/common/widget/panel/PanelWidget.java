package dev.vini2003.hammer.gui.api.common.widget.panel;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.texture.PartitionedTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import dev.vini2003.hammer.gui.api.common.widget.WidgetCollection;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class PanelWidget extends Widget implements WidgetCollection {
	public static final Texture STANDARD_TEXTURE = new PartitionedTexture(HC.id("textures/widget/panel.png"), 18.0F, 18.0F, 0.25F, 0.25F, 0.25F, 0.25F);
	
	protected Supplier<Texture> texture = () -> STANDARD_TEXTURE;
	
	protected Collection<Widget> children = new ArrayList<>();
	
	@Override
	public Collection<Widget> getChildren() {
		return children;
	}
	
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {
		texture.get().draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
		
		if (provider instanceof VertexConsumerProvider.Immediate immediate) {
			immediate.draw();
		}
		
		for (var child : getChildren()) {
			if (!child.isHidden()) {
				child.draw(matrices, provider, tickDelta);
			}
		}
	}
	
	public void setTexture(Supplier<Texture> texture) {
		this.texture = texture;
	}
	
	public void setTexture(Texture texture) {
		setTexture(() -> texture);
	}
}
