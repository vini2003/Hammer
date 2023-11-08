package dev.vini2003.hammer.preset.mixin.client;

import net.minecraft.client.gui.hud.PlayerListHud;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {
	// TODO: Reimplement.
	// @ModifyVariable(at = @At(value = "INVOKE_ASSIGN", target = "Lcom/google/common/collect/Ordering;sortedCopy(Ljava/lang/Iterable;)Ljava/util/List;", shift = At.Shift.AFTER), method = "render")
	// private List<PlayerListEntry> hammer$render$sortedCopy(List<PlayerListEntry> original) {
	// 	original.removeIf(entry -> {
	// 		return PlayerListUtil.isHiddenOnPlayerList(entry.getProfile());
	// 	});
	//
	// 	return original;
	// }
}
