package dev.vini2003.hammer.core.api.common.util.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.text.WordUtils;

public class TextUtilImpl {
	public static MutableText getModId(Identifier id) {
		var loader = FabricLoader.getInstance();
		var optionalMod = loader.getModContainer(id.getNamespace());
		
		if (optionalMod.isPresent()) {
			var mod = optionalMod.get();
			
			return Text.literal(mod.getMetadata().getName()).formatted(Formatting.BLUE, Formatting.ITALIC);
		} else {
			return Text.literal(WordUtils.capitalize(id.getNamespace().replace("_", " ").replace("-", " "))).formatted(Formatting.BLUE, Formatting.ITALIC);
		}
	}
}
