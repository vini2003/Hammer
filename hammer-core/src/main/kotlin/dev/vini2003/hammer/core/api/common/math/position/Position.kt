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

package dev.vini2003.hammer.core.api.common.math.position

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
import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * A [Position] is 3-dimensional coordinate, which contains X, Y and Z components.
 */
@Serializable
class Position constructor(
	override val x: Float,
	override val y: Float,
	override val z: Float
) : PositionHolder {
	/**
	 * Constructs a position.
	 *
	 * @param x the X position component.
	 * @param y the Y position component.
	 * @return the position.
	 */
	constructor(
		x: Float,
		y: Float
	) : this(x, y, 0.0F)
	
	/**
	 * Constructs an anchored position.
	 *
	 * @param anchor the anchor.
	 * @param relativePosition the relative position.
	 * @return the position.
	 */
	constructor(
		anchor: PositionHolder,
		relativePosition: Position
	) : this(relativePosition.x + anchor.x,  relativePosition.y + anchor.y,  relativePosition.z + anchor.z)
	
	/**
	 * Constructs an anchored position.
	 *
	 * @param anchor the anchor.
	 * @param relativeX the relative X component.
	 * @param relativeY the relative Y component.
	 * @return the position.
	 */
	constructor(
		anchor: PositionHolder,
		relativeX: Float,
		relativeY: Float
	) : this(relativeX + anchor.x,  relativeY + anchor.y,  0.0F)
	
	/**
	 * Constructs an anchored position.
	 *
	 * @param anchor the anchor.
	 * @param x the relative X component.
	 * @param y the relative Y component.
	 * @param z the relative Z component.
	 * @return the position.
	 */
	constructor(
		anchor: PositionHolder,
		x: Float,
		y: Float,
		z: Float
	) : this(x + anchor.x, y + anchor.y, z + anchor.z)
	
	/**
	 * Constructs an anchor's position.
	 *
	 * @param anchor the anchor.
	 * @return the position.
	 */
	constructor(anchor: PositionHolder) : this(anchor.x, anchor.y, anchor.z)
	
	/**
	 * Converts a [BlockPos] to a position.
	 *
	 * @param blockPos the block pos.
	 * @return the position.
	 */
	constructor(blockPos: BlockPos) : this(blockPos.x.toFloat(),  blockPos.y.toFloat(),  blockPos.z.toFloat())
	
	/**
	 * Converts a [ChunkPos] to a position.
	 *
	 * @param chunkPos the chunk pos.
	 * @return the position.
	 */
	constructor(chunkPos: ChunkPos) : this(chunkPos.x.toFloat(),  0.0F,  chunkPos.z.toFloat())
	
	@OptIn(ExperimentalSerializationApi::class)
	@Serializer(forClass = Position::class)
	companion object : KSerializer<Position> {
		/**
		 * Returns a stream of positions between the given ones.
		 *
		 * @param startPosition the first position.
		 * @param endPosition the second position.
		 * @return the sequence.
		 */
		@JvmStatic
		fun sequence(startPosition: Position, endPosition: Position): Sequence<Position> {
			val minPosition = Position(min(startPosition.x, endPosition.x), min(startPosition.y, endPosition.y), min(startPosition.z, endPosition.z))
			val maxPosition = Position(max(startPosition.x, endPosition.y), max(startPosition.y, endPosition.y), max(startPosition.z, endPosition.z))
			
			var x: Int = minPosition.x.toInt()
			var y: Int = minPosition.y.toInt()
			var z: Int = minPosition.z.toInt()
			
			return generateSequence {
				if (x < maxPosition.x.toInt() && y < maxPosition.y.toInt() && z < maxPosition.z.toInt()) {
					z += 1
					
					if (z >= maxPosition.z) {
						y += 1
						
						if (y > maxPosition.y) {
							x += 1
						}
					}
					
					Position(x.toFloat(), y.toFloat(), z.toFloat())
				} else null
			}
		}
		
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
					
					position = Position(x, y, z)
				}
			} else {
				val x = decoder.decodeFloat()
				val y = decoder.decodeFloat()
				val z = decoder.decodeFloat()
				
				position = Position(x, y, z)
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
	
	/**
	 * Converts this position to a [BlockPos].
	 *
	 * @return the block pos.
	 */
	fun toBlockPos(): BlockPos = BlockPos(x.toInt(), y.toInt(), z.toInt())
	
	/**
	 * Converts this position to a [ChunkPos].
	 *
	 * @return the chunk pos.
	 */
	fun toChunkPos(): ChunkPos = ChunkPos(x.toInt(), z.toInt())
	
	/**
	 * Returns the distance between this and the distant position.
	 *
	 * @param position the distant position.
	 * @return the distance.
	 */
	fun distanceTo(position: Position): Float {
		return sqrt((x - position.x).pow(2) + (y - position.y).pow(2) + (z - position.z).pow(2))
	}
	
	/**
	 * Returns the squared distance between this and the distant position.
	 *
	 * @param position the distant position.
	 * @return the distance.
	 */
	fun squaredDistanceTo(position: Position): Float {
		return (x - position.x).pow(2) + (y - position.y).pow(2) + (z - position.z).pow(2)
	}
	
	/**
	 * Returns this position offset by X and Y components.
	 *
	 * @param x the X component to offset this position by.
	 * @param y the Y component to offset this position by.
	 * @return the resulting position.
	 */
	fun offset(x: Float, y: Float): Position {
		return Position(this.x + x, this.y + y, this.z)
	}
	
	/**
	 * Returns this position offset by X, Y and Z components.
	 *
	 * @param x the X component to offset this position by.
	 * @param y the Y component to offset this position by.
	 * @param z the Z component to offset this position by.
	 * @return the resulting position.
	 */
	fun offset(x: Float, y: Float, z: Float): Position {
		return Position(this.x + x, this.y + y, this.z + z)
	}
	
	/**
	 * Returns this position, adding another position to it.
	 *
	 * @param position the position.
	 * @return the resulting position.
	 */
	operator fun plus(position: Position): Position {
		return Position(x + position.x, y + position.y, z + position.z)
	}
	
	/**
	 * Returns this position, subtracting another position from it.
	 *
	 * @param position the position.
	 * @return the resulting position.
	 */
	operator fun minus(position: Position): Position {
		return Position(x + position.x, y + position.y, z + position.z)
	}
	
	/**
	 * Returns this position, multiplying its components by a given number.
	 *
	 * @param number the number.
	 * @return the resulting position.
	 */
	operator fun times(number: Float): Position {
		return Position(x * number, y * number, z * number)
	}
	
	/**
	 * Returns this position, dividing its components by a given number.
	 *
	 * @param number the number.
	 * @return the resulting position.
	 */
	operator fun div(number: Float): Position {
		return Position(x / number, y / number, z / number)
	}
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Position) return false
		
		if (x != other.x) return false
		if (y != other.y) return false
		if (z != other.z) return false
		
		return true
	}
	
	override fun hashCode(): Int {
		var result = x.hashCode()
		result = 31 * result + y.hashCode()
		result = 31 * result + z.hashCode()
		return result
	}
}
