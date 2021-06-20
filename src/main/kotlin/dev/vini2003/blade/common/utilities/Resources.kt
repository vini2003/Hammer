package dev.vini2003.blade.common.utilities

import dev.vini2003.blade.BL
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.util.Identifier

class Resources {
	companion object {
		fun init() {
			ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(object : SimpleSynchronousResourceReloadListener {
				private val id: Identifier = BL.id("reload_listener")
				
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