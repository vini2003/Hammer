package com.github.vini2003.blade.common.data

enum class Action {
	PICKUP,
	PICKUP_ALL,
	QUICK_MOVE,
	CLONE,

	DRAG_SPLIT,
	DRAG_SINGLE,
	DRAG_SPLIT_PREVIEW,
	DRAG_SINGLE_PREVIEW,

	FROM_SLOT_TO_CURSOR,
	FROM_CURSOR_TO_SLOT;

	open fun of(button: Int, mode: Boolean): Action? {
		return when (button) {
			0 -> if (mode) DRAG_SPLIT else DRAG_SPLIT_PREVIEW
			1 -> if (mode) DRAG_SINGLE else DRAG_SINGLE_PREVIEW
			else -> null
		}
	}

	open fun isPreview(): Boolean {
		return this == DRAG_SINGLE_PREVIEW || this == DRAG_SPLIT_PREVIEW
	}

	open fun isSplit(): Boolean {
		return this == DRAG_SPLIT || this == DRAG_SPLIT_PREVIEW
	}

	open fun isSingle(): Boolean {
		return this == DRAG_SINGLE || this == DRAG_SINGLE_PREVIEW
	}
}