package dev.vini2003.hammer.interaction.mixin.common;

import dev.vini2003.hammer.interaction.common.interaction.InteractionType;
import dev.vini2003.hammer.interaction.common.manager.InteractionRuleManager;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {
	@Inject(at = @At("HEAD"), method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", cancellable = true)
	private void hammer$place(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
		if (!InteractionRuleManager.allows(context.getPlayer(), InteractionType.BLOCK_PLACE, context.getStack().getItem())) {
			cir.cancel();
		}
	}
}
