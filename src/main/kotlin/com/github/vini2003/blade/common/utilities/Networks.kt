package com.github.vini2003.blade.common.utilities

import com.github.vini2003.blade.Blade
import io.netty.buffer.Unpooled
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier

class Networks {
    companion object {
        val SLOT_UPDATE_PACKET: Identifier = Blade.identifier("slot_update_packet")

        fun createSlotUpdatePacket(syncId: Int, slotNumber: Int, inventoryNumber: Int, stack: ItemStack): PacketByteBuf {
            val buffer = PacketByteBuf(Unpooled.buffer())
            buffer.writeInt(syncId)
            buffer.writeInt(slotNumber)
            buffer.writeInt(inventoryNumber)
            buffer.writeCompoundTag(stack.toTag(CompoundTag()))
            return buffer
        }
    }
}