package dev.vini2003.hammer.persistence.api.common;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;

import java.nio.file.Files;
import java.nio.file.Path;

public class PersistentObject {
	protected transient String path = null;
	
	protected Path getBasePath() {
		return InstanceUtil.getPersistentObjectPath();
	}
	
	public static <T extends PersistentObject> T getOrCreate(String name, Class<T> clazz) {
		try {
			var persistentObject = clazz.newInstance();
			persistentObject.path = name + ".json";
			
			try {
				persistentObject.getOrCreate();
			} catch (Exception e) {
				HC.LOGGER.error("Failed to load persistent object " + name + ".json!");
				e.printStackTrace();
			}
			
			return persistentObject;
		} catch (Exception e) {
			HC.LOGGER.error("Failed to load persistent object " + name + ".json!");
			e.printStackTrace();
		}
		
		throw new UnsupportedOperationException("Could not instantiate persistent object class" + clazz.getName() + "! Did you add a no-args constructor?");
	}
	
	public void getOrCreate() {
		var persistentObjectPath = getBasePath();
		var persistentObjectFilePath = persistentObjectPath.resolve(path);
		
		if (!Files.exists(persistentObjectFilePath)) {
			save();
		}
		
		try (var reader = Files.newBufferedReader(persistentObjectFilePath)) {
			var persistentObject = HC.GSON.fromJson(reader, getClass());
			
			for (var field : getClass().getDeclaredFields()) {
				field.setAccessible(true);
				field.set(this, field.get(persistentObject));
			}
		} catch (Exception e) {
			HC.LOGGER.error("Failed to load persistent object at " + path + "!");
			e.printStackTrace();
		}
	}
	
	public void save() {
		var persistentObjectPath = InstanceUtil.getConfigPath();
		var persistentObjectFilePath = persistentObjectPath.resolve(path);
		
		try {
			Files.createDirectories(persistentObjectPath);
			
			try (var writer = Files.newBufferedWriter(persistentObjectFilePath)) {
				HC.GSON.toJson(this, writer);
				writer.flush();
			}
		} catch (Exception e) {
			HC.LOGGER.error("Failed to save persistent object at " + path + "!");
			e.printStackTrace();
		}
	}
}
