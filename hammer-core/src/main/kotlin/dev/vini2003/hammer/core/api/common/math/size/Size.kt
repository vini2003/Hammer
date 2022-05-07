/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 vini2003
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.vini2003.hammer.core.api.common.math.size

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.internal.TaggedDecoder
import kotlinx.serialization.internal.TaggedEncoder

/**
 * A [Size] is a 3-dimensional space, which contains width, height and length components.
 */
@Serializable
class Size(
	override val width: Float,
	override val height: Float,
	override val length: Float
) : SizeHolder {
	/**
	 * Constructs a size.
	 *
	 * @param width the width size component
	 * @param height the height size component.
	 * @return the size.
	 */
	constructor(width: Float, height: Float) : this(width, height, 0.0F)
	
	/**
	 * Constructs an anchored size.
	 *
	 * @param anchor the anchor.
	 * @param width the relative with size component.
	 * @param height the relative height size component.
	 * @return the size.
	 */
	constructor(anchor: SizeHolder, width: Float, height: Float) : this(width + anchor.width, height + anchor.height, 0.0F)
	
	/**
	 * Constructs an anchored size.
	 *
	 * @param anchor the anchor.
	 * @param width the relative with size component.
	 * @param height the relative height size component.
	 * @param length the relative length size component.
	 * @return the size.
	 */
	constructor(anchor: SizeHolder, width: Float, height: Float, length: Float) : this(width + anchor.width, height + anchor.height, length + anchor.length)
	
	/**
	 * Constructs an anchor's size.
	 *
	 * @param anchor the anchor.
	 * @return the size.
	 */
	constructor(anchor: SizeHolder) : this(anchor.width, anchor.height, anchor.length)
	
	@OptIn(ExperimentalSerializationApi::class)
	@Serializer(forClass = Size::class)
	companion object : KSerializer<Size> {
		
		override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Position") {
			element<Float>("width")
			element<Float>("height")
			element<Float>("length")
		}
		
		@OptIn(InternalSerializationApi::class)
		override fun deserialize(decoder: Decoder): Size {
			var size: Size? = null
			
			if (decoder is TaggedDecoder<*>) {
				decoder.decodeStructure(descriptor) {
					val width = decodeFloatElement(descriptor, 0)
					val height = decodeFloatElement(descriptor, 1)
					val length = decodeFloatElement(descriptor, 2)
					
					size = Size(width, height, length)
				}
			} else {
				val width = decoder.decodeFloat()
				val height = decoder.decodeFloat()
				val length = decoder.decodeFloat()
				
				size = Size(width, height, length)
			}
			
			return size!!
		}
		
		@OptIn(InternalSerializationApi::class)
		override fun serialize(encoder: Encoder, value: Size) {
			if (encoder is TaggedEncoder<*>) {
				encoder.encodeStructure(descriptor) {
					encodeFloatElement(descriptor, 0, value.width)
					encodeFloatElement(descriptor, 1, value.height)
					encodeFloatElement(descriptor, 2, value.length)
				}
			} else {
				encoder.encodeFloat(value.width)
				encoder.encodeFloat(value.height)
				encoder.encodeFloat(value.length)
			}
		}
	}
	
	/**
	 * Returns this size, adding another size to it.
	 *
	 * @param size the size.
	 * @return the resulting size.
	 */
	operator fun plus(size: Size): Size {
		return Size(width + size.width, height + size.height, length + size.length)
	}
	
	/**
	 * Returns this size, subtracting another size from it.
	 *
	 * @param size the size.
	 * @return the resulting size.
	 */
	operator fun minus(size: Size): Size {
		return Size(width + size.width, height + size.height, length + size.length)
	}
	
	/**
	 * Returns this size, multiplying its components by a given number.
	 *
	 * @param number the number.
	 * @return the resulting size.
	 */
	operator fun times(number: Float): Size {
		return Size(width * number, height * number, length * number)
	}
	
	/**
	 * Returns this size, dividing its components by a given number.
	 *
	 * @param number the number.
	 * @return the resulting size.
	 */
	operator fun div(number: Float): Size {
		return Size(width / number, height / number, length / number)
	}
}

