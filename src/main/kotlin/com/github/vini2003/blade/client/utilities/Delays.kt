package com.github.vini2003.blade.client.utilities

class Delays {
	companion object {
		private const val nanosecondDelay: Long = 150000000
		var lastNanoseconds: Long = 0L

		/**
		 * Retrieve the interval in nanoseconds since the last update.
		 *
		 * @return Interval in nanoseconds since the last update.
		 */
		fun interval(): Long {
			return System.nanoTime() - lastNanoseconds
		}

		fun delay(): Long {
			return nanosecondDelay
		}

		/**
		 * Update the last time, in nanoseconds, when update was called.
		 */
		fun update() {
			lastNanoseconds = System.nanoTime()
		}
	}
}