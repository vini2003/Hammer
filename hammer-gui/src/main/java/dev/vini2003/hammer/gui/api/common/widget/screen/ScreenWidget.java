package dev.vini2003.hammer.gui.api.common.widget.screen;

import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import dev.vini2003.hammer.gui.api.common.widget.WidgetCollection;

import java.util.ArrayList;
import java.util.Collection;

public class ScreenWidget extends Widget implements WidgetCollection {
	private final Collection<Widget> children = new ArrayList<>();
	
	@Override
	public Size getStandardSize() {
		return Size.of(getWidth(), getHeight());
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
	public Collection<Widget> getChildren() {
		return children;
	}
}
