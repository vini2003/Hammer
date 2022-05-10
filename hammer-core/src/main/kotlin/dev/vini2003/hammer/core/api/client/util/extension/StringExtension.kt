package dev.vini2003.hammer.core.api.client.util.extension

import java.util.*

fun String.capitalizeWords(): String =
		split(" ").map { string ->
			string.lowercase(Locale.getDefault()).replaceFirstChar { char ->
				if (char.isLowerCase()) {
					char.titlecase(Locale.getDefault())
				} else {
					char.toString()
				} }
		}.joinToString(" ")