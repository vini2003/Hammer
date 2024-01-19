package dev.vini2003.hammer.core.api.common.util.forge;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.toml.TomlParser;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipFile;

public class HammerUtilImpl {
	@Nullable
	private static Set<String> enabledModules = null;
	
	public static boolean isModuleEnabled(String moduleId) {
		if (enabledModules == null) {
			enabledModules = new HashSet<>();
			
			LoadingModList.get()
						  .getModFiles()
						  .stream()
						  .map(ModFileInfo::getFile)
						  .map(ModFile::getFilePath)
						  .forEach(path -> {
							  File file;
							  
							  try {
								  file = path.toFile();
							  } catch (Exception e) {
								  return;
							  }
							  
							  CommentedConfig hammerToml = null;
							  
							  if (file.isDirectory()) {
								  var hammerTomlPath = Path.of(file.getAbsolutePath(), "META-INF", "hammer.toml");
								  
								  if (hammerTomlPath.toFile().exists()) {
									  try (var hammerTomlInputStream = new FileInputStream(hammerTomlPath.toFile())) {
										  hammerToml = new TomlParser().parse(new BufferedReader(new InputStreamReader(hammerTomlInputStream, StandardCharsets.UTF_8)));
									  } catch (Exception ignored) {
									  }
								  }
							  } else {
								  try (var zipFile = new ZipFile(file)) {
									  var hammerTomlEntry = zipFile.getEntry("META-INF/hammer.toml");
									  
									  if (hammerTomlEntry != null) {
										  try (var hammerTomlInputStream = zipFile.getInputStream(hammerTomlEntry)) {
											  hammerToml = new TomlParser().parse(new BufferedReader(new InputStreamReader(hammerTomlInputStream, StandardCharsets.UTF_8)));
										  }
									  }
								  } catch (Exception ignored) {
								  }
							  }
							  
							  if (hammerToml == null) {
								  return;
							  }
							  
							  var modules = hammerToml.getOrElse("modules", Collections.emptyList());
							  
							  modules.stream()
									 .map(Object::toString)
									 .forEach(module -> {
										 enabledModules.add(module);
									 });
						  });
		}
		
		return enabledModules.contains(moduleId);
	}
}
