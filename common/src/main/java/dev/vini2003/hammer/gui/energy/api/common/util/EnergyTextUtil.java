package dev.vini2003.hammer.gui.energy.api.common.util;

import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.common.util.NumberUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class EnergyTextUtil {
	public static Color COLOR = new Color(0.67F, 0.89F, 0.47F, 1.0F);
	
	public static MutableText getEnergy() {
		return Text.translatable("text.hammer.energy").styled((style) -> style.withColor(COLOR.toRgb()));
	}
	
	public static List<Text> getDetailedTooltips(long currentEnergy, long capacity) {
		var tooltips = new ArrayList<Text>();
		
		tooltips.add(getEnergy());
		tooltips.add(Text.literal(NumberUtil.getPrettyString(currentEnergy, "E") + " / " + NumberUtil.getPrettyString(capacity, "E")).formatted(Formatting.GRAY));
		
		return tooltips;
	}
	
	public static List<Text> getShortenedTooltips(long currentEnergy, long capacity) {
		var tooltips = new ArrayList<Text>();
		
		tooltips.add(getEnergy());
		tooltips.add(Text.literal(NumberUtil.getPrettyShortenedString(currentEnergy, "E") + " / " + NumberUtil.getPrettyShortenedString(capacity, "E")).formatted(Formatting.GRAY));
		
		return tooltips;
	}
}
