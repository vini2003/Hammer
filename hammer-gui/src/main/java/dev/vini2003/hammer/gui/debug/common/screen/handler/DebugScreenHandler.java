package dev.vini2003.hammer.gui.debug.common.screen.handler;

import dev.vini2003.hammer.gui.api.common.screen.handler.BaseScreenHandler;
import dev.vini2003.hammer.gui.api.common.widget.panel.PanelWidget;
import dev.vini2003.hammer.gui.debug.common.util.WidgetUtil;
import dev.vini2003.hammer.gui.registry.common.debug.HGUIScreenHandlerTypes;
import net.minecraft.entity.player.PlayerEntity;

public class DebugScreenHandler extends BaseScreenHandler {
	public DebugScreenHandler(int syncId, PlayerEntity player) {
		super(HGUIScreenHandlerTypes.DEBUG, syncId, player);
	}
	
	@Override
	public void init(int width, int height) {
		var all = WidgetUtil.createAll();
		
		var panel = new PanelWidget();
		panel.setWidth(width);
		panel.setHeight(height);
		
		add(panel);

		var x = 18.0F;
		var y = 18.0F;
		
		for (var widget : all) {
			widget.setX(x);
			widget.setY(y);
			
			x += widget.getWidth() + 18.0F;
			
			if (x >= width) {
				x = 18.0F;
				
				var maxHeight = widget.getHeight();
				
				for (var w : all) {
					if (w.getY() == y && w.getHeight() > maxHeight) {
						maxHeight = w.getHeight();
					}
				}
				y += maxHeight + 18.0F;
			}
			
			panel.add(widget);
		}
	}
	
	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}
}
