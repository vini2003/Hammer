package dev.vini2003.hammer.gui.energy.api.common.util;

import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.common.util.NumberUtil;
import dev.vini2003.hammer.core.api.common.util.TextUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import team.reborn.energy.api.EnergyStorage;

import java.util.ArrayList;
import java.util.List;

public class EnergyTextUtil {
	public static Color COLOR = new Color(0.67F, 0.89F, 0.47F, 1.0F);
	
	public static MutableText getEnergy() {
		return TextUtil.toTranslatableText("text.hammer.energy").styled((style) -> style.withColor(COLOR.toRgb()));
	}
	
	public static List<Text> getDetailedTooltips(EnergyStorage storage) {
		var tooltips = new ArrayList<Text>();
		
		tooltips.add(getEnergy());
		tooltips.add(TextUtil.toLiteralText(NumberUtil.getPrettyString(storage.getAmount(), "E") + " / " + NumberUtil.getPrettyString(storage.getCapacity(), "E")).formatted(Formatting.GRAY));
		
		return tooltips;
	}
	
	public static List<Text> getShortenedTooltips(EnergyStorage storage) {
		var tooltips = new ArrayList<Text>();
		
		tooltips.add(getEnergy());
		tooltips.add(TextUtil.toLiteralText(NumberUtil.getPrettyShortenedString(storage.getAmount(), "E") + " / " + NumberUtil.getPrettyShortenedString(storage.getCapacity(), "E")).formatted(Formatting.GRAY));
		
		return tooltips;
	}
}
