package dev.vini2003.hammer.core.api.common.util.forge;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraftforge.fml.ModList;
import org.apache.commons.lang3.StringUtils;

public class TextUtilImpl {
	public static MutableText getModId(Identifier id) {
		var optionalMod = ModList.get().getModContainerById(id.getNamespace());
		
		if (optionalMod.isPresent()) {
			var mod = optionalMod.get();
			return Text.literal(mod.getModId()).formatted(Formatting.BLUE, Formatting.ITALIC);
		} else {
			return Text.literal(StringUtils.capitalize(id.getNamespace().replace("_", " ").replace("-", " ")))
					   .formatted(Formatting.BLUE, Formatting.ITALIC);
		}
	}
}
