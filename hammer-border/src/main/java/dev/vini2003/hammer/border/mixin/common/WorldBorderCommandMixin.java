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

package dev.vini2003.hammer.border.mixin.common;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.vini2003.hammer.border.impl.common.border.CubicWorldBorder;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.WorldBorderCommand;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Locale;

@Mixin(WorldBorderCommand.class)
public class WorldBorderCommandMixin {
	private static final SimpleCommandExceptionType CENTER_FAILED_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.worldborder.center.failed"));
	private static final SimpleCommandExceptionType SET_FAILED_FAR_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.worldborder.set.failed.far", 2.9999984E7));
	
	@ModifyArgs(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;", ordinal = 4), method = "register")
	private static void hammer$register$argument$4(Args args) {
		args.set(1, Vec3ArgumentType.vec3());
	}
	
	@ModifyArgs(at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;executes(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;", ordinal = 4), method = "register")
	private static void hammer$register$executes$4(Args args) {
		args.set(0, new Command<ServerCommandSource>() {
			@Override
			public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
				var source = context.getSource();
				
				var pos = Vec3ArgumentType.getVec3(context, "pos");
				
				var worldBorder = source.getServer().getOverworld().getWorldBorder();
				var cubicWorldBorder = (CubicWorldBorder) source.getServer().getOverworld().getWorldBorder();
				
				if (worldBorder.getCenterX() == pos.x && cubicWorldBorder.getCenterY() == pos.y && worldBorder.getCenterZ() == pos.z) {
					throw CENTER_FAILED_EXCEPTION.create();
				}
				
				if (Math.abs(pos.x) > 2.9999984E7 || Math.abs(pos.y) > 2.9999984E7 || Math.abs(pos.z) > 2.9999984E7) {
					throw SET_FAILED_FAR_EXCEPTION.create();
				}
				
				cubicWorldBorder.setCenter(pos.x, pos.y, pos.z);
				
				source.sendFeedback(() -> 
						Text.translatable("commands.hammer.border.center.success",
								String.format(Locale.ROOT, "%.2f", (float) pos.x),
								String.format(Locale.ROOT, "%.2f", (float) pos.y),
								String.format(Locale.ROOT, "%.2f", (float) pos.z)
						), 
						true
				);
				
				return 0;
			}
		});
	}
}
