package dev.vini2003.hammer.preset.mixin.client;

import dev.vini2003.hammer.preset.api.common.util.PlayerListUtil;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {
	@ModifyVariable(at = @At(value = "INVOKE_ASSIGN", target = "Lcom/google/common/collect/Ordering;sortedCopy(Ljava/lang/Iterable;)Ljava/util/List;", shift = At.Shift.AFTER), method = "render")
	private List<PlayerListEntry> hammer$render$sortedCopy(List<PlayerListEntry> original) {
		original.removeIf(entry -> {
			return !PlayerListUtil.isHiddenOnPlayerList(entry.getProfile());
		});
		
		return original;
	}
}
