package dev.vini2003.hammer.core.api.common.util;

import dev.vini2003.hammer.core.api.client.util.InstanceUtil;

public class GameUtil {
	public static boolean isPaused() {
		if (InstanceUtil.isServer()) {
			return false;
		} else {
			return InstanceUtil.getClient().isPaused();
		}
	}
}
