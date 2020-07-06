package com.github.vini2003.blade

import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier

class Blade : ModInitializer {
    companion object {
        val MOD_ID = "blade"

        fun identifier(string: String): Identifier {
            return Identifier(MOD_ID, string)
        }
    }
    override fun onInitialize() {

    }
}