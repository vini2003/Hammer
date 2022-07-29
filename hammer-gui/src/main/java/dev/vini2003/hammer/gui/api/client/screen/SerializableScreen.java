package dev.vini2003.hammer.gui.api.client.screen;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.gui.api.client.screen.base.BaseScreen;
import dev.vini2003.hammer.gui.api.common.serialization.widget.ScreenSerializerContext;
import net.minecraft.text.Text;

public class SerializableScreen extends BaseScreen {
	private final ScreenSerializerContext context;
	
	public SerializableScreen(Text title, ScreenSerializerContext context) {
		super(title);
		
		this.context = context;
	}
	
	@Override
	public void init(int width, int height) {
		add(context.get(HC.id("screen")));
	}
}
