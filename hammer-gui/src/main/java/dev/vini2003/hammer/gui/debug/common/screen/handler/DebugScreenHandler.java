package dev.vini2003.hammer.gui.debug.common.screen.handler;

import dev.vini2003.hammer.gui.api.common.screen.handler.BaseScreenHandler;
import dev.vini2003.hammer.gui.api.common.widget.arrow.ArrowWidget;
import dev.vini2003.hammer.gui.api.common.widget.bar.BarWidget;
import dev.vini2003.hammer.gui.api.common.widget.bar.FluidBarWidget;
import dev.vini2003.hammer.gui.api.common.widget.bar.ImageBarWidget;
import dev.vini2003.hammer.gui.api.common.widget.bar.SpriteBarWidget;
import dev.vini2003.hammer.gui.api.common.widget.button.ButtonWidget;
import dev.vini2003.hammer.gui.api.common.widget.image.ImageWidget;
import dev.vini2003.hammer.gui.api.common.widget.item.ItemStackWidget;
import dev.vini2003.hammer.gui.api.common.widget.item.ItemWidget;
import dev.vini2003.hammer.gui.api.common.widget.list.ListWidget;
import dev.vini2003.hammer.gui.api.common.widget.list.slot.SlotListWidget;
import dev.vini2003.hammer.gui.api.common.widget.panel.PanelWidget;
import dev.vini2003.hammer.gui.api.common.widget.screen.ScreenWidget;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;
import dev.vini2003.hammer.gui.api.common.widget.tab.TabWidget;
import dev.vini2003.hammer.gui.api.common.widget.text.TextAreaWidget;
import dev.vini2003.hammer.gui.api.common.widget.text.TextEditorWidget;
import dev.vini2003.hammer.gui.api.common.widget.text.TextFieldWidget;
import dev.vini2003.hammer.gui.api.common.widget.text.TextWidget;
import dev.vini2003.hammer.gui.api.common.widget.toggle.ToggleWidget;
import dev.vini2003.hammer.gui.debug.common.util.WidgetUtil;
import dev.vini2003.hammer.gui.registry.common.debug.HGUIScreenHandlerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class DebugScreenHandler extends BaseScreenHandler {
	public DebugScreenHandler(int syncId, PlayerEntity player) {
		super(HGUIScreenHandlerTypes.DEBUG, syncId, player);
	}
	
	@Override
	public void init(int width, int height) {
		var all = WidgetUtil.createAll();
		
		var screen = new PanelWidget();
		screen.setWidth(width);
		screen.setHeight(height);
		
		add(screen);

		var x = 18.0F;
		var y = 18.0F;
		
		var i = 0;
		
		for (var widget : all) {
			i++;
			
			widget.setX(x);
			widget.setY(y);
			
			x += widget.getWidth() + 18.0F;
			
			if (x >= width) {
				x = 18.0F;
				var maxH = widget.getHeight();
				// find highest widget in this x and add to y
				for (var w : all) {
					if (w.getY() == y && w.getHeight() > maxH) {
						maxH = w.getHeight();
					}
				}
				y += maxH + 18.0F;
			}
			
			screen.add(widget);
		}
	}
	
	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}
}
