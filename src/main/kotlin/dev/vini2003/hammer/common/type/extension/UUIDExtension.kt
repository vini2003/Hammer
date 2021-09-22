package dev.vini2003.hammer.common.type.extension

import java.util.*

fun String.toUUID(): UUID {
	return if (isUUID()) {
		UUID.fromString(this)
	} else {
		throw IllegalArgumentException("string is not an UUID")
	}
}

fun String.isUUID(): Boolean {
	return try {
		UUID.fromString(this)
		true
	} catch (_: Exception) {
		false
	}
}