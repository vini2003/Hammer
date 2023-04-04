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

package dev.vini2003.hammer.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.vini2003.hammer.core.api.common.command.argument.PositionArgumentType;
import dev.vini2003.hammer.core.api.common.manager.CommandManager;
import dev.vini2003.hammer.core.api.common.manager.CommandManager.*;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.registry.common.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApiStatus.Internal
public class HC implements ModInitializer {
	public static final String ID = "hammer";
	
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	public static final Logger LOGGER = LoggerFactory.getLogger("Hammer");
	
	public static Identifier id(String path) {
		return new Identifier(ID, path);
	}
	
	@Override
	public void onInitialize() {
		HCArgumentTypes.init();
		HCEvents.init();
		// HCItemGroups.init();
		HCNetworking.init();
		HCComponents.init();
		
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			CommandManager.registerCommands(dispatcher, registryAccess, HC.class);
		});
	}
	
	@Command(path = "foo bar")
	public static int executeReset(
			ServerCommandSource source,
			@IntegerRange(min = 0, max = 100)
			int amount,
			@IntegerRange(min = 10, max = 25)
			int quantity,
			@EntityKind(EntityType.PLAYERS)
			List<PlayerEntity> players,
			@Type(PositionArgumentType.class)
			Position position,
			@Type(BlockPosArgumentType.class)
			BlockPos blockPos
			) {
		source.sendFeedback(Text.literal("Amount: " + amount).formatted(Formatting.GOLD), false);
		source.sendFeedback(Text.literal("Quantity: " + quantity).formatted(Formatting.GOLD), false);
		source.sendFeedback(Text.literal("Players:").formatted(Formatting.GOLD), false);
		
		players.forEach(player -> {
			source.sendFeedback(Text.of(player.getName().getString()), false);
		});
		
		return 1;
	}
}
