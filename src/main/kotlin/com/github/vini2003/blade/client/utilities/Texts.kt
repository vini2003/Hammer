package com.github.vini2003.blade.client.utilities

import net.minecraft.text.Text

class Texts {
    companion object {
        fun width(text: Text): Int {
            return Instances.client().textRenderer.getWidth(text)
        }

        fun height(): Int {
            return Instances.client().textRenderer.fontHeight
        }
    }
}