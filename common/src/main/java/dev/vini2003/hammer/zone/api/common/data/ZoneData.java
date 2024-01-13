package dev.vini2003.hammer.zone.api.common.data;

import dev.vini2003.hammer.zone.api.common.zone.Zone;
import net.minecraft.util.math.Direction;

public record ZoneData(
				Zone zone,
				Direction side,
				double distance
		) {}