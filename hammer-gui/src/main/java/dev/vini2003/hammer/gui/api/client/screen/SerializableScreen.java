package dev.vini2003.hammer.gui.api.client.screen;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.gui.api.client.screen.base.BaseScreen;
import dev.vini2003.hammer.gui.api.common.serialization.widget.SerializationContext;
import net.minecraft.text.Text;

public class SerializableScreen extends BaseScreen {
	private final SerializationContext context;
	
	public SerializableScreen(Text title, SerializationContext context) {
		super(title);
		
		this.context = context;
	}
	
	@Override
	public void init(int width, int height) {
		add(context.get(HC.id("screen")));
	}
}
