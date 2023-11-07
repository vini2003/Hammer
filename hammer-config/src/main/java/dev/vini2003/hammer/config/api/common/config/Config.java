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
			} catch (Exception e) {
				HC.LOGGER.error("Failed to load config " + name + ".json!");
				e.printStackTrace();
			}
			
			return config;
		} catch (Exception e) {
			HC.LOGGER.error("Failed to load config " + name + ".json!");
			e.printStackTrace();
		}
		
		throw new UnsupportedOperationException("Could not instantiate config class! Did you add a no-args constructor?");
	}
	
	public void load() {
		var configPath = InstanceUtil.getFabric().getConfigDir();
		var configFilePath = configPath.resolve(path);
		
		if (!Files.exists(configFilePath)) {
			save();
		}
		
		try (var reader = Files.newBufferedReader(configFilePath)) {
			var config = HC.GSON.fromJson(reader, getClass());
			
			for (var field : getClass().getDeclaredFields()) {
				field.setAccessible(true);
				field.set(this, field.get(config));
			}
		} catch (Exception e) {
			HC.LOGGER.error("Failed to load config at " + path + "!");
			e.printStackTrace();
		}
	}
	
	public void save() {
		var configPath = InstanceUtil.getFabric().getConfigDir();
		var configFilePath = configPath.resolve(path);
		
		try {
			Files.createDirectories(configPath);
			
			try (var writer = Files.newBufferedWriter(configFilePath)) {
				HC.GSON.toJson(this, writer);
				writer.flush();
			}
		} catch (Exception e) {
			HC.LOGGER.error("Failed to save config at " + path + "!");
			e.printStackTrace();
		}
	}
}
