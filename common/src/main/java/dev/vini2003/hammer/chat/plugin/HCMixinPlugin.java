package dev.vini2003.hammer.chat.plugin;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.common.util.HammerUtil;
import dev.vini2003.hammer.core.api.common.util.ModUtil;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class HCMixinPlugin implements IMixinConfigPlugin {
	@Override
	public void onLoad(String mixinPackage) {
	
	}
	
	@Override
	public String getRefMapperConfig() {
		return null;
	}
	
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (!HammerUtil.isModuleEnabled(HC.CHAT_MODULE_ID)) {
			return false;
		}
		
		HC.LOGGER.info("Applying 'chat' module Mixin '" + mixinClassName + "'.");
		
		// TODO: Fix in Forge. Needs to look through the mod files, because the mod list doesn't exist at this point.
//		if (ModUtil.isModLoaded("nochatreports")) {
//			if (mixinClassName.equals("dev.vini2003.hammer.chat.mixin.common.ServerMetadataMixin")) {
//				return false;
//			}
//
//			if (mixinClassName.equals("dev.vini2003.hammer.chat.mixin.common.ServerPlayNetworkHandlerMixin")) {
//				return false;
//			}
//		}
		
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
