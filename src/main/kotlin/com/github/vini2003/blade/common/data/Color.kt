package com.github.vini2003.blade.common.data

class Color(val r: Float, val g: Float, val b: Float, val a: Float) {
    companion object {
        fun of(string: String): Color {
            return of(string.toInt())
        }

        fun of(int: Int): Color {
            return Color((int shr 24 and 0xFF) / 255F, (int shr 16 and 0xFF) / 255F, (int shr 8 and 0xFF) / 255F, (int and 0xFF) / 255F)
        }

        fun default(): Color {
            return Color(1F, 1F, 1F, 1F)
        }
    }
}