package dev.vini2003.hammer.config.api.common.config;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.impl.FabricLoaderImpl;

import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
	protected transient String path = null;
	
	public static <T extends Config> T load(String name, Class<T> clazz) {
		try {
			var config = clazz.newInstance();
			config.path = name + ".json";
			
			try {
				config.load();
			} catch (Exception ignored) {
				System.err.println("Failed to load config " + name + ".json!");
			}
			
			return config;
		} catch (Exception ignored) {
			ignored.printStackTrace();
		}
		
		throw new UnsupportedOperationException("Could not instantiate config class! Did you add a no-args constructor?");
	}
	
	public void load() {
		var configPath = InstanceUtil.getFabric().getConfigDir();
		var configFilePath = configPath.resolve(path);
		
		try (var reader = Files.newBufferedReader(configFilePath)) {
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
		var configPath = InstanceUtil.getFabric().getConfigDir();
		var configFilePath = configPath.resolve(path);
		
		try (var writer = Files.newBufferedWriter(configFilePath)) {
			HC.GSON.toJson(this, writer);
			
			writer.flush();
		} catch (Exception ignored) {}
	}
}
