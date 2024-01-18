package dev.vini2003.hammer.persistence.api.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;

public class PersistentObject {
	protected transient String path = null;
	
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	public static final Logger LOGGER = LoggerFactory.getLogger("Hammer");
	
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
				LOGGER.error("Failed to load persistent object " + name + ".json!");
				e.printStackTrace();
			}
			
			return persistentObject;
		} catch (Exception e) {
			LOGGER.error("Failed to load persistent object " + name + ".json!");
			e.printStackTrace();
		}
		
		throw new UnsupportedOperationException("Could not instantiate persistent object class" + clazz.getName() + "! Did you add a no-args constructor?");
	}
	
	public void getOrCreate() {
		var persistentObjectPath = getBasePath();
		var persistentObjectFilePath = persistentObjectPath.resolve(path);
		
		if (!Files.exists(persistentObjectPath)) {
			save();
		}
		
		if (!Files.exists(persistentObjectFilePath)) {
			save();
		}
		
		try (var reader = Files.newBufferedReader(persistentObjectFilePath)) {
			var persistentObject = GSON.fromJson(reader, getClass());
			
			for (var field : getClass().getDeclaredFields()) {
				field.setAccessible(true);
				field.set(this, field.get(persistentObject));
			}
		} catch (Exception e) {
			LOGGER.error("Failed to load persistent object at " + path + "!");
			e.printStackTrace();
		}
	}
	
	public void save() {
		var persistentObjectPath = getBasePath();
		var persistentObjectFilePath = persistentObjectPath.resolve(path);
		
		try {
			Files.createDirectories(persistentObjectPath);
			
			try (var writer = Files.newBufferedWriter(persistentObjectFilePath)) {
				GSON.toJson(this, writer);
				writer.flush();
			}
		} catch (Exception e) {
			LOGGER.error("Failed to save persistent object at " + path + "!");
			e.printStackTrace();
		}
	}
}
