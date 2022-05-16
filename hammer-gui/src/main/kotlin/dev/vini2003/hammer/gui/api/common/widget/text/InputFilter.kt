package dev.vini2003.hammer.gui.api.common.widget.text

public interface InputFilter<T> {

    fun toString(t: T): String

    fun toValue(text: String): T

    fun accepts(character: String, text: String): Boolean

}