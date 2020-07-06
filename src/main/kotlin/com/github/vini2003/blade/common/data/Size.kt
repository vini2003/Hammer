package com.github.vini2003.blade.common.data

class Size(
    private val widthSupplier: () -> Float,
    private val heightSupplier: () -> Float
) {
    var width: Float = 0.0F
    var height: Float = 0.0F

    fun recalculate() {
        width = widthSupplier.invoke()
        height = heightSupplier.invoke()
    }

    init {
        recalculate()
    }
}