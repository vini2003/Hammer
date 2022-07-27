package dev.vini2003.hammer.gui.api.common.widget.screen;

import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public class ScreenWidget extends Widget {
	@Override
	public float getX() {
		return 0.0F;
	}
	
	@Override
	public float getY() {
		return 0.0F;
	}
	
	@Override
	public float getWidth() {
		return InstanceUtil.getClient().getWindow().getScaledWidth();
	}
	
	@Override
	public float getHeight() {
		return InstanceUtil.getClient().getWindow().getScaledHeight();
	}
	
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {
	
	}
}
