package dev.vini2003.hammer.preset.registry.common;

import dev.vini2003.hammer.persistence.api.common.PersistentObject;

public class HPConfig extends PersistentObject {
	public boolean disableToasts = true;
	public boolean disableSinglePlayer = false;
	
	public boolean hideServerAddress = true;
	
	public boolean enableChannels = false;
	public boolean enableRoles = false;
	public boolean enableWelcome = false;
	
	public boolean defaultShowDirectMessages = true;
	public boolean defaultShowWarnings = false;
	public boolean defaultShowCommandFeedback = false;
	public boolean defaultShowChat = true;
	
	public boolean defaultFastChatFade = false;
	
	public boolean disableJoinMessages = false;
	public boolean disableLeaveMessages = false;

	public String windowName = "";
	public String windowIcon = "";
}
