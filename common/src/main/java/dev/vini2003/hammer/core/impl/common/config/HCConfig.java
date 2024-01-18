package dev.vini2003.hammer.core.impl.common.config;

import dev.architectury.injectables.targets.ArchitecturyTarget;
import dev.vini2003.hammer.persistence.api.common.PersistentObject;
import dev.vini2003.hammer.persistence.api.common.config.Config;

public class HCConfig extends Config {
	public boolean disableYggdrasilInDevelopmentEnvironment = true;
	public boolean disableMovementSpeedWarning = true;
	
	public boolean disableEnd = false;
	public boolean disableNether = false;
	
	public boolean useBuckets = ArchitecturyTarget.getCurrentTarget().equals("forge");
	public boolean useDroplets = ArchitecturyTarget.getCurrentTarget().equals("forge");
}
