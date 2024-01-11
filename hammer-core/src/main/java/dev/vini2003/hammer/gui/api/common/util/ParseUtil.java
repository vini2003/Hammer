package dev.vini2003.hammer.gui.api.common.util;

import com.google.gson.JsonObject;
import dev.vini2003.hammer.core.HC;
import net.minecraft.util.Identifier;

public class ParseUtil {
	private boolean has(Identifier screenId, JsonObject object, String fieldName) {
		if (!object.has(fieldName)) {
			HC.LOGGER.error("Resource for screen '" + screenId + "' did not contain '" + fieldName + "'!");
			
			return false;
		} else {
			return true;
		}
	}
	
	private boolean check(String fieldName, boolean value) {
		if (!value) {
			HC.LOGGER.error("Attribute '" + fieldName + "' contained unexpected value!");
			
			return false;
		}  else {
			return true;
		}
	}
}
