package dev.vini2003.hammer.adventure.common.util.extension

import dev.vini2003.hammer.adventure.common.util.AdventureUtils
import net.minecraft.text.Text

fun Text.toComponent() = AdventureUtils.convertTextToComponent(this)