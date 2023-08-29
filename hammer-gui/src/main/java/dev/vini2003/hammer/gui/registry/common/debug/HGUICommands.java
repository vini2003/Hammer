package dev.vini2003.hammer.gui.registry.common.debug;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import dev.vini2003.hammer.gui.debug.common.screen.handler.DebugScreenHandler;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.server.command.CommandManager.literal;

public class HGUICommands {
	private static int executeDebugScreenHandler(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		if (source == null) return Command.SINGLE_SUCCESS;
		
		var player = source.getPlayer();
		if (player == null) return Command.SINGLE_SUCCESS;
		
		player.openHandledScreen(new NamedScreenHandlerFactory() {
			@Override
			public Text getDisplayName() {
				return Text.literal("Debug");
			}
			
			@Nullable
			@Override
			public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
				return new DebugScreenHandler(syncId, playerInventory.player);
			}
		});
		
		return Command.SINGLE_SUCCESS;
	}
	
	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(
					literal("debug").executes(HGUICommands::executeDebugScreenHandler)
			);
		});
	}
	
}
