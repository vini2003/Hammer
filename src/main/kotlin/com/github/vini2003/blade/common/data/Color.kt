package com.github.vini2003.blade.common.data

class Color(val r: Float, val g: Float, val b: Float, val a: Float) {
    companion object {
        fun of(string: String): Color {
            val a = Integer.decode("0x" + string.substring(2, 4)) / 255F
            val r = Integer.decode("0x" + string.substring(4, 6)) / 255F
            val g = Integer.decode("0x" + string.substring(6, 8)) / 255F
            val b = Integer.decode("0x" + string.substring(8, 10)) / 255F

            return Color(r, g, b, a)
        }

        fun of(int: Int): Color {
            return Color((int shr 24 and 0xFF) / 255F, (int shr 16 and 0xFF) / 255F, (int shr 8 and 0xFF) / 255F, (int and 0xFF) / 255F)
        }

        fun default(): Color {
            return Color(1F, 1F, 1F, 1F)
        }
    }
}