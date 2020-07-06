package dev.vini2003.hammer.common.geometry.size

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

@Serializable
interface Size : SizeHolder {
	@OptIn(ExperimentalSerializationApi::class)
	@Serializer(forClass = Size::class)
	companion object : KSerializer<Size> {
		@JvmStatic
		fun of(width: Number, height: Number): Size = SimpleSize(width.toFloat(), height.toFloat(), length = 0.0F)
		
		@JvmStatic
		fun of(width: Number, height: Number, length: Number): Size = SimpleSize(width.toFloat(), height.toFloat(), length.toFloat())
		
		@JvmStatic
		fun of(anchor: SizeHolder, width: Number, height: Number): Size = of(width.toFloat() + anchor.width, height.toFloat() + anchor.height)
		
		@JvmStatic
		fun of(anchor: SizeHolder, width: Number, height: Number, length: Number): Size = of(width.toFloat() + anchor.width, height.toFloat() + anchor.height, length.toFloat() + anchor.length)
		
		@JvmStatic
		fun of(anchor: SizeHolder): Size = of(anchor, 0, 0)
		
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
					
					size = SimpleSize(width, height, length)
				}
			} else {
				val width = decoder.decodeFloat()
				val height = decoder.decodeFloat()
				val length = decoder.decodeFloat()
				
				size = SimpleSize(width, height, length)
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
	
	operator fun plus(size: Size): Size = of(width + size.width, height + size.height)
	operator fun plus(size: Pair<Number, Number>): Size = of(width + size.first.toFloat(), height + size.second.toFloat())
	
	operator fun minus(size: Size): Size = of(width + size.width, height + size.height)
	operator fun minus(size: Pair<Number, Number>): Size = of(width + size.first.toFloat(), height + size.second.toFloat())
	
	operator fun times(number: Number): Size = of(width * number.toFloat(), height + number.toFloat())
	
	operator fun div(number: Number): Size = of(width / number.toFloat(), height / number.toFloat())

	@Serializable
	data class SimpleSize(override val width: Float, override val height: Float, override val length: Float) : Size
}

