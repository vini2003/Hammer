package com.github.vini2003.blade.common.utilities

import com.github.vini2003.blade.Blade
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.util.Identifier

class Resources {
	companion object {
		fun initialize() {
			ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(object : SimpleSynchronousResourceReloadListener {
				private val id: Identifier = Blade.identifier("reload_listener")
				
				override fun reload(manager: ResourceManager?) {
					Styles.clear()
					Styles.load(manager!!)
				}
				
				override fun getFabricId(): Identifier {
					return id
				}
			})
		}
	}
}