package dev.vini2003.hammer.preset.registry.common;

import dev.vini2003.hammer.config.api.common.config.Config;

public class HPConfig extends Config {
	public boolean disableToasts = true;
	public boolean disableFabulousGraphics = true;
	public boolean disableSinglePlayer = false;
	
	public boolean hideServerAddress = true;
	
	public boolean enableChannels = false;
	public boolean enableRoles = false;
	public boolean enableWelcome = false;
	
	public boolean defaultShowDirectMessages = true;
	public boolean defaultShowWarnings = false;
	public boolean defaultShowCommandFeedback = false;
	public boolean defaultShowChat = true;
	public boolean defaultShowGlobalChat = true;
	
	public boolean defaultFastChatFade = false;
	
	public boolean disableJoinMessages = false;
	public boolean disableLeaveMessages = false;
	
	public String iconLowResPath = "/assets/hammer/icons/icon_16x16.png";
	public String iconHighResPath = "/assets/hammer/icons/icon_32x32.png";
	
	public String windowName = "";
}
