package dev.vini2003.hammer.persistence.api.common.config;

import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.persistence.api.common.PersistentObject;

import java.nio.file.Path;

public class Config extends PersistentObject {
	@Override
	protected Path getBasePath() {
		return InstanceUtil.getConfigPath();
	}
}
