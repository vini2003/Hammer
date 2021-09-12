package dev.vini2003.blade.common.util.extension

import dev.vini2003.blade.common.geometry.position.Position

operator fun Position.plus(position: Position) = Position.of(x + position.x, y + position.y)
operator fun Position.plus(position: Pair<Number, Number>) = Position.of(x + position.first.toFloat(), y + position.second.toFloat())

operator fun Position.minus(position: Position) = Position.of(x + position.x, y + position.y)
operator fun Position.minus(position: Pair<Number, Number>) = Position.of(x + position.first.toFloat(), y + position.second.toFloat())

operator fun Position.times(number: Number) = Position.of(x * number.toFloat(), y + number.toFloat())

operator fun Position.div(number: Number) = Position.of(x / number.toFloat(), y / number.toFloat())