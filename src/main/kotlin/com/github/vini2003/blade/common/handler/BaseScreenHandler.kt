package com.github.vini2003.blade.common.handler

import com.github.vini2003.blade.common.utilities.Networks
import com.github.vini2003.blade.common.data.Action
import com.github.vini2003.blade.common.data.Stacks
import com.github.vini2003.blade.common.widget.OriginalWidgetCollection
import com.github.vini2003.blade.common.widget.WidgetCollection
import com.github.vini2003.blade.common.widget.base.AbstractWidget
import com.github.vini2003.blade.common.widget.base.SlotWidget
import com.github.vini2003.blade.common.widget.base.SlotWidget.Companion.LEFT
import com.github.vini2003.blade.common.widget.base.SlotWidget.Companion.RIGHT
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.server.network.ServerPlayerEntity
import java.lang.UnsupportedOperationException
import kotlin.math.max
import kotlin.math.min

class BaseScreenHandler(type: ScreenHandlerType<out ScreenHandler>, syncId: Int, private val player: PlayerEntity) : ScreenHandler(type, syncId), OriginalWidgetCollection {
    private val widgets: List<AbstractWidget> = emptyList()

    private val playerInventory: PlayerInventory = player.inventory;

    private val inventories = emptyMap<Int, Inventory>()

    private val inventoryCache = emptyMap<Int, Map<Int, ItemStack>>()

    override fun getWidgets(): Collection<AbstractWidget> {
        return widgets
    }

    override fun addWidget(widget: AbstractWidget) {
        widgets.plus(widget)
    }

    override fun removeWidget(widget: AbstractWidget) {
        widgets.minus(widget)
    }

    override fun getInventory(inventoryNumber: Int): Inventory? {
        return inventories[inventoryNumber]
    }

    override fun addInventory(inventoryNumber: Int, inventory: Inventory) {
        inventories.plus(Pair(inventory, inventory))
    }

    override fun getPlayer(): PlayerEntity {
        return player
    }

    fun consumeAction(slotNumber: Int, inventoryNumber: Int, button: Int, action: Action, player: PlayerEntity) {
        val slotA: SlotWidget = widgets.find { widget -> widget is SlotWidget && widget.slot == slotNumber && widget.inventory == inventoryNumber } as SlotWidget

        var stackA: ItemStack = slotA.getStack().copy()
        var stackB = player.inventory.cursorStack.copy()

        when (action) {
            Action.PICKUP -> {
                if (!Stacks.equal(stackA, stackB)) {
                    when (button) {
                        LEFT -> {
                            if (!slotA.canInsert(stackB) || stackA.count == stackA.maxCount) return

                            Stacks.merge(stackA, stackB, stackB.maxCount, stackB.maxCount) { stackC: ItemStack, stackD: ItemStack ->
                                slotA.setStack(stackC)
                                playerInventory.cursorStack = stackD

                                stackA = slotA.getStack()
                                stackB = playerInventory.cursorStack
                            }
                        }
                        RIGHT -> {
                            if (!stackB.isEmpty && stackA.count < stackA.maxCount ) {
                                Stacks.merge(stackB, stackA, stackB.maxCount, stackA.count + 1) { stackC: ItemStack, stackD: ItemStack ->
                                    playerInventory.cursorStack = stackC
                                    slotA.setStack(stackD)

                                    stackA = slotA.getStack()
                                    stackB = playerInventory.cursorStack
                                }
                            } else if (!stackA.isEmpty && stackB.count < stackB.maxCount) {
                                Stacks.merge(stackA, stackB, stackB.maxCount, max(1, min(stackA.maxCount / 2, stackA.count / 2))) { stackC: ItemStack, stackD: ItemStack ->
                                    slotA.setStack(stackC)
                                    playerInventory.cursorStack = stackD

                                    stackA = slotA.getStack()
                                    stackB = playerInventory.cursorStack
                                }
                            }
                        }
                    }
                } else {
                    when (button) {
                        0 -> {
                            if (!slotA.canInsert(stackB) || stackA.count == stackA.maxCount) return

                            Stacks.merge(stackB, stackA, stackB.maxCount, stackA.maxCount) { stackC: ItemStack, stackD: ItemStack ->
                                playerInventory.cursorStack = stackC
                                slotA.setStack(stackD)

                                stackA = slotA.getStack()
                                stackB = playerInventory.cursorStack
                            }
                        }
                        else -> {
                            if (!slotA.canInsert(stackB) || stackA.count == stackA.maxCount) {
                                Stacks.merge(stackB, stackA, stackB.maxCount, stackA.count + 1) { stackC: ItemStack, stackD: ItemStack ->
                                    playerInventory.cursorStack = stackC
                                    slotA.setStack(stackD)

                                    stackA = slotA.getStack()
                                    stackB = playerInventory.cursorStack
                                }
                            }
                        }
                    }
                }
            }
            Action.CLONE -> {
                if (player.isCreative) {
                    stackB = stackA.copy()
                    stackB.count = stackB.maxCount
                    playerInventory.cursorStack = stackB
                }
            }
            Action.QUICK_MOVE -> {
                widgets.forEach{
                    if (it is SlotWidget && it.inventory != slotA.inventory) {
                        val stackC: ItemStack = it.getStack()
                        stackA = slotA.getStack()

                        if (it.canInsert(stackA) && !it.disabled) {
                            if (stackA.isEmpty && !stackB.isEmpty || (Stacks.equal(stackA, stackB) && stackC.count < stackC.maxCount)) {
                                Stacks.merge(stackA, stackB, stackA.maxCount, stackB.maxCount) {stackC: ItemStack, stackD: ItemStack ->
                                    slotA.setStack(stackC)
                                    it.setStack(stackD)

                                    stackA = slotA.getStack()
                                    stackB = playerInventory.cursorStack
                                }
                            }
                        }
                    }
                }
            }
            Action.PICKUP_ALL -> {
                widgets.forEach{
                    if (it is SlotWidget && Stacks.equal(stackA, it.getStack())) {
                        if (!it.disabled) {
                            val stackC: ItemStack = it.getStack()
                            Stacks.merge(stackC, stackB, stackC.maxCount, stackB.maxCount) {stackD: ItemStack, stackE: ItemStack ->
                                it.setStack(stackD)
                                playerInventory.cursorStack = stackE

                                stackA = slotA.getStack()
                                stackB = playerInventory.cursorStack
                            }
                        }
                    }
                }
            }
            else -> {
                return
            }
        }
    }

    override fun sendContentUpdates() {
        if (player !is ServerPlayerEntity) return

        widgets.forEach{
            if (it is SlotWidget) {
                if (!inventoryCache.containsKey(it.inventory)) inventoryCache.plus(Pair(it.inventory, emptyMap()))
                if (!inventoryCache[it.inventory]!!.containsKey(it.slot)) inventoryCache[it.inventory]!!.plus(Pair(it.slot, it.getStack()))

                val stackA: ItemStack = inventoryCache[it.inventory]!![it.slot]!!

                if (!Stacks.equal(stackA, it.getStack())) {
                    ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, Networks.SLOT_UPDATE_PACKET, Networks.createSlotUpdatePacket(syncId, it.slot, it.inventory, it.getStack()))
                }

                inventoryCache[it.inventory]!!.plus(Pair(it.slot, it.getStack()))
            }
        }
    }

    override fun onSlotClick(i: Int, j: Int, actionType: SlotActionType?, playerEntity: PlayerEntity?): ItemStack {
        return ItemStack.EMPTY
    }

    override fun addSlot(slot: Slot?): Slot {
        throw UnsupportedOperationException("Blade's BaseScreenHandler is incompatible with vanilla Slots!")
    }

    override fun canUse(player: PlayerEntity?): Boolean {
        return true
    }
}