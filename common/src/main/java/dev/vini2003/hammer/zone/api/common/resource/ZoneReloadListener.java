/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 vini2003
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.vini2003.hammer.zone.api.common.resource;

import com.google.gson.JsonObject;
import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.common.queue.ServerTaskQueue;
import dev.vini2003.hammer.zone.api.common.manager.ZoneManager;
import dev.vini2003.hammer.zone.api.common.zone.Zone;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class ZoneReloadListener implements SynchronousResourceReloader {
	private static final Identifier ID = HC.id("zone_reload");
	
	// TODO: Register this! ReloadListenerRegistry
//	@Override
//	public Identifier getFabricId() {
//		return ID;
//	}
	
	@Override
	public void reload(ResourceManager manager) {
		var resources = manager.findResources("zones", (resource) -> resource.getPath().endsWith(".json"));
		
		for (var resourceId : resources.keySet()) {
			try {
				var resource = manager.getResource(resourceId).orElseThrow();
				
				var resourceInputStream = resource.getInputStream();
				
				var resourceString = IOUtils.toString(resourceInputStream, "UTF-8");
				
				var zone = Zone.fromJson(HC.GSON.fromJson(resourceString, JsonObject.class));
				
				ServerTaskQueue.enqueue((server) -> {
					var zoneWorld = server.getWorld(zone.getWorld());
					
					ZoneManager.add(zoneWorld, zone);
				}, 1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
