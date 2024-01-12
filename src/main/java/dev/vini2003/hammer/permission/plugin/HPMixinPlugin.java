package dev.vini2003.hammer.permission.plugin;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.common.util.HammerUtil;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class HPMixinPlugin implements IMixinConfigPlugin {
	@Override
	public void onLoad(String mixinPackage) {
	
	}
	
	@Override
	public String getRefMapperConfig() {
		return null;
	}
	
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (!HammerUtil.isModuleEnabled(HC.PERMISSION_MODULE_ID)) {
			return false;
		}
		
		HC.LOGGER.info("Applying '" + HC.PERMISSION_MODULE_ID + "' module Mixins.");
		
		return true;
	}
	
	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
	
	}
	
	@Override
	public List<String> getMixins() {
		return null;
	}
	
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	
	}
	
	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	
	}
}
