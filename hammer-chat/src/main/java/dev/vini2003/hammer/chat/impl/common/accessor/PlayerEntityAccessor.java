package dev.vini2003.hammer.chat.impl.common.accessor;

public interface PlayerEntityAccessor {
	void hammer$setShowChat(boolean showChat);
	
	boolean hammer$shouldShowChat();
	
	void hammer$setShowGlobalChat(boolean showGlobalChat);
	
	boolean hammer$shouldShowGlobalChat();
	
	void hammer$setShowCommandFeedback(boolean showFeedback);
	
	boolean hammer$shouldShowCommandFeedback();
	
	void hammer$setShowWarnings(boolean showWarnings);
	
	boolean hammer$shouldShowWarnings();
	
	void hammer$setMuted(boolean muted);
	
	boolean hammer$isMuted();
}
