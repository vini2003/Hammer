package dev.vini2003.hammer.preset.registry.common;

import dev.vini2003.hammer.config.api.common.config.Config;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class HPConfig extends Config {
	public boolean disableToasts = true;
	public boolean disableFabulousGraphics = true;
	
	public boolean hideServerAddress = true;
	
	public String iconLowResPath = "/assets/hammer/icons/icon_16x16.png";
	public String iconHighResPath = "/assets/hammer/icons/icon_32x32.png";
	
	public String windowName = "";
}
