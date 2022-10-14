package dev.vini2003.hammer.config.api.common.config;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;

import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
	protected transient Path path = null;
	
	public static <T extends Config> T load(String name, Class<T> clazz) {
		try {
			var config = clazz.newInstance();
			config.path = InstanceUtil.getFabric().getConfigDir().resolve(name + ".json");
			config.load();
			
			return config;
		} catch (Exception ignored) {}
		
		throw new UnsupportedOperationException("Could not instantiate config class! Did you add a no-args constructor?");
	}
	
	public void load() {
		try (var reader = Files.newBufferedReader(path)) {
			for (var i = 0; i < 2; ++i) {
				if (i == 1) {
					save();
				} else {
					var config = HC.GSON.fromJson(reader, getClass());
					
					for (var field : getClass().getDeclaredFields()) {
						field.set(this, field.get(config));
					}
				}
			}
		} catch (Exception ignored) {}
	}
	
	public void save() {
		try (var writer = Files.newBufferedWriter(path)) {
			HC.GSON.toJson(this, writer);
			
			writer.flush();
		} catch (Exception ignored) {}
	}
}
