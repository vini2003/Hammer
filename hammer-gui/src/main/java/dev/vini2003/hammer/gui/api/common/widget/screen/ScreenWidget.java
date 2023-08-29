package dev.vini2003.hammer.gui.api.common.widget.screen;

import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;

public class ScreenWidget extends Widget {
	@Override
	public Size getStandardSize() {
		return new Size(getWidth(), getHeight());
	}
	
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
	@Environment(EnvType.CLIENT)
	public void draw(DrawContext context, float tickDelta) {
	
	}
}
