package com.github.vini2003.blade.common.utilities

import com.github.vini2003.blade.Blade
import com.github.vini2003.blade.common.widget.base.AbstractWidget
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import org.apache.logging.log4j.Level
import java.util.function.Function

class Validators {
	companion object {
		@JvmStatic
		private val validators: Multimap<Class<*>, Function<AbstractWidget, Result<AbstractWidget>>> = HashMultimap.create()

		@JvmStatic
		fun register(klass: Class<*>, validator: Function<AbstractWidget, Result<AbstractWidget>>) {
			validators.put(klass, validator)
		}

		@JvmStatic
		fun validate(widget: AbstractWidget) {
			validators[widget.javaClass]
					.map { function -> function.apply(widget) }
					.filter { entry -> entry.isFailure }
					.forEach {
						Blade.LOGGER.log(Level.ERROR, it.exceptionOrNull()?.message)
					}
		}
	}
}