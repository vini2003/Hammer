package com.github.vini2003.blade.common.utilities

import blue.endless.jankson.Jankson
import blue.endless.jankson.JsonElement
import blue.endless.jankson.JsonObject
import blue.endless.jankson.JsonPrimitive
import com.github.vini2003.blade.common.data.Style
import net.minecraft.resource.ResourceManager
import java.lang.Exception
import java.lang.RuntimeException

class Styles {
    companion object {
        private val entries: MutableMap<String, Style> = mutableMapOf()

        private val jankson: Jankson = Jankson.builder().build();

        fun get(name: String): Style {
            return entries.getOrDefault(name, Style.EMPTY)
        }

        fun load(manager: ResourceManager) {
            manager.findResources("style") { string -> string.endsWith(".style.json5") }.forEach {
                try {
                    load(it.path.replaceFirst("style/", "").replaceFirst("\\.style\\.json5", ""), jankson.load(manager.getResource(it).inputStream))
                } catch (exception: Exception) {
                    System.err.println(exception.message)
                }
            }
        }

        fun load(name: String, data: JsonObject) {
            val templates: JsonObject? = data.getObject("templates")
            val entries: JsonObject? = data.getObject("entries")

            if (templates == null || entries == null) {
                throw RuntimeException("Invalid JSON parsed for Blade theme!")
            }

            val mapped: MutableMap<String, JsonElement> = mutableMapOf()

            entries.forEach {
                mapped[it.key] = if (it.key.startsWith("$")) (templates[it.key.replace("$", "")] as JsonPrimitive).value as JsonElement else it.value
            }

            Styles.entries[name] = Style(mapped)
        }

        fun clear() {
            entries.clear()
        }
    }
}