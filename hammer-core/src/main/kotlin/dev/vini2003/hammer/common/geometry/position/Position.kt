package dev.vini2003.hammer.common.geometry.position

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
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos

@Serializable
interface Position : PositionHolder {
	@OptIn(ExperimentalSerializationApi::class)
	@Serializer(forClass = Position::class)
	companion object : KSerializer<Position> {
		@JvmStatic
		fun of(x: Number, y: Number): Position = SimplePosition(x.toFloat(), y.toFloat(), 0.0F)
		
		@JvmStatic
		fun of(x: Number, y: Number, z: Number): Position = SimplePosition(x.toFloat(), y.toFloat(), z.toFloat())
		
		@JvmStatic
		fun of(anchor: PositionHolder, position: Position): Position = of(x = position.x + anchor.x, y = position.y + anchor.y, z = position.z + anchor.z)
		
		@JvmStatic
		fun of(anchor: PositionHolder, x: Number, y: Number): Position = of(x = x.toFloat() + anchor.x, y = y.toFloat() + anchor.y)
		
		@JvmStatic
		fun of(anchor: PositionHolder, x: Number, y: Number, z: Number): Position = of(x = x.toFloat() + anchor.x, y = y.toFloat() + anchor.y, z = z.toFloat() + anchor.z)
		
		@JvmStatic
		fun of(anchor: PositionHolder): Position = of(anchor)
		
		@JvmStatic
		fun of(blockPos: BlockPos): Position = of(x = blockPos.x, y = blockPos.y, z = blockPos.z)
		
		@JvmStatic
		fun of(chunkPos: ChunkPos): Position = of(x = chunkPos.x, y = 0.0F, z = chunkPos.z)
		
		override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Position") {
			element<Float>("x")
			element<Float>("y")
			element<Float>("z")
		}
		
		@OptIn(InternalSerializationApi::class)
		override fun deserialize(decoder: Decoder): Position {
			var position: Position? = null
			
			if (decoder is TaggedDecoder<*>) {
				decoder.decodeStructure(descriptor) {
					val x = decodeFloatElement(descriptor, 0)
					val y = decodeFloatElement(descriptor, 1)
					val z = decodeFloatElement(descriptor, 2)
					
					position = SimplePosition(x, y, z)
				}
			} else {
				val x = decoder.decodeFloat()
				val y = decoder.decodeFloat()
				val z = decoder.decodeFloat()
				
				position = SimplePosition(x, y, z)
			}
			
			return position!!
		}
		
		@OptIn(InternalSerializationApi::class)
		override fun serialize(encoder: Encoder, value: Position) {
			if (encoder is TaggedEncoder<*>) {
				encoder.encodeStructure(descriptor) {
					encodeFloatElement(descriptor, 0, value.x)
					encodeFloatElement(descriptor, 1, value.y)
					encodeFloatElement(descriptor, 2, value.z)
				}
			} else {
				encoder.encodeFloat(value.x)
				encoder.encodeFloat(value.y)
				encoder.encodeFloat(value.z)
			}
		}
	}
	
	fun offset(x: Number, y: Number): Position = of(this, x = x.toFloat(), y = y.toFloat())
	
	fun offset(x: Number, y: Number, z: Number): Position = of(this, x = x.toFloat(), y = y.toFloat(), z = z.toFloat())
	
	operator fun plus(position: Position): Position = of(x = x + position.x, y = y + position.y, z = z + position.z)

	operator fun minus(position: Position): Position = of(x = x + position.x, y = y + position.y, z = z + position.z)
	
	operator fun times(number: Number): Position = of(x = x * number.toFloat(), y = y * number.toFloat(), z = z * number.toFloat())
	
	operator fun div(number: Number): Position = of(x = x / number.toFloat(), y = y / number.toFloat(), z = z / number.toFloat())
	
	fun toBlockPos(): BlockPos = BlockPos(x.toInt(), y.toInt(), z.toInt())
	
	fun toChunkPos(): ChunkPos = ChunkPos(x.toInt(), z.toInt())
	
	@Serializable
	data class SimplePosition(override val x: Float, override val y: Float, override val z: Float) : Position
}
