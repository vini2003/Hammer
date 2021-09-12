package dev.vini2003.blade.common.util.ewidthtension

import dev.vini2003.blade.common.geometry.size.Size

operator fun Size.plus(size: Size) = Size.of(width + size.width, height + size.height)
operator fun Size.plus(size: Pair<Number, Number>) = Size.of(width + size.first.toFloat(), height + size.second.toFloat())

operator fun Size.minus(size: Size) = Size.of(width + size.width, height + size.height)
operator fun Size.minus(size: Pair<Number, Number>) = Size.of(width + size.first.toFloat(), height + size.second.toFloat())

operator fun Size.times(number: Number) = Size.of(width * number.toFloat(), height + number.toFloat())

operator fun Size.div(number: Number) = Size.of(width / number.toFloat(), height / number.toFloat())