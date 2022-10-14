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

package dev.vini2003.hammer.zone.registry.client;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.zone.api.common.manager.ZoneGroupManager;
import dev.vini2003.hammer.zone.api.common.manager.ZoneManager;
import dev.vini2003.hammer.zone.api.common.zone.Zone;
import dev.vini2003.hammer.zone.registry.common.HZNetworking;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.text.ClickEvent;

import net.minecraft.text.Text;

import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static dev.vini2003.hammer.core.api.common.command.argument.ColorArgumentType.color;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.minecraft.command.argument.IdentifierArgumentType.identifier;

public class HZCommands {
	private static CompletableFuture<Suggestions> suggestZoneIds(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
		var client = InstanceUtil.getClient();

		for (var zone : ZoneManager.getAll(client.world)) {
			builder.suggest(zone.getId().toString());
		}
		
		return builder.buildFuture();
	}
	
	private static CompletableFuture<Suggestions> suggestZoneGroupIds(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
		for (var group : ZoneGroupManager.getGroups()) {
			builder.suggest(group.getId().toString());
		}
		
		return builder.buildFuture();
	}
	
	private static boolean requiresOp(FabricClientCommandSource source) {
		return source.hasPermissionLevel(4);
	}
	
	// Enables or disables the zone editor.
	private static int executeZoneEditor(CommandContext<FabricClientCommandSource> context) {
		var source = context.getSource();

		HZValues.ZONE_EDITOR = !HZValues.ZONE_EDITOR;
		
		source.sendFeedback(Text.translatable("command.hammer.zone.editor", HZValues.ZONE_EDITOR ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case")));
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Selects the zone the player is looking at.
	private static int executeZoneSelect(CommandContext<FabricClientCommandSource> context) {
		var source = context.getSource();

		var zone = HZValues.getSelectedZone();
		
		if (zone != null) {
			HZValues.setCommandSelectedZone(zone);
			
			source.sendFeedback(Text.translatable("command.hammer.zone.select", zone.getId()));
		} else {
			source.sendError(Text.translatable("command.hammer.zone.not_found"));
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Selects the zone with the given ID.
	private static int zoneSelectId(CommandContext<FabricClientCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();

		var zoneId = context.getArgument("id", Identifier.class);
		var zone = ZoneManager.get(player.world, zoneId);
		
		if (zone != null) {
			HZValues.setCommandSelectedZone(zone);
			
			source.sendFeedback(Text.translatable("command.hammer.zone.select", zone.getId()));
		} else {
			source.sendError(Text.translatable("command.hammer.zone.not_found"));
		}
		
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Deselects the selected zone.
	private static int executeZoneDeselect(CommandContext<FabricClientCommandSource> context) {
		var source = context.getSource();

		var zone = HZValues.getCommandSelectedZone();
		
		if (zone != null) {
			HZValues.setCommandSelectedZone(null);
			
			source.sendFeedback(Text.translatable("command.hammer.zone.deselect"));
		} else {
		
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Deletes the selected zone.
	private static int executeZoneDelete(CommandContext<FabricClientCommandSource> context) {
		var source = context.getSource();

		var zone = HZValues.getSelectedZone();
		
		if (zone != null) {
			 var buf = PacketByteBufs.create();
			 buf.writeIdentifier(zone.getId());
			
			ClientPlayNetworking.send(HZNetworking.ZONE_DELETE, buf);
			
			source.sendFeedback(Text.translatable("command.hammer.zone.delete", zone.getId()));
		} else {
			source.sendError(Text.translatable("command.hammer.zone.not_found"));
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Deletes the zone with the given ID.
	private static int executeZoneDeleteId(CommandContext<FabricClientCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		var zoneId = context.getArgument("id", Identifier.class);
		var zone = ZoneManager.get(player.world, zoneId);
		
		if (zone != null) {
			var buf = PacketByteBufs.create();
			buf.writeIdentifier(zone.getId());
			
			ClientPlayNetworking.send(HZNetworking.ZONE_DELETE, buf);
			
			source.sendFeedback(Text.translatable("command.hammer.zone.delete", zone.getId()));
		} else {
			source.sendError(Text.translatable("command.hammer.zone.not_found"));
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Creates a zone.
	private static int executeZoneCreate(CommandContext<FabricClientCommandSource> context) {
		var source = context.getSource();

		var zoneId = HC.id(UUID.randomUUID().toString().replace("-", ""));
		
		var buf = PacketByteBufs.create();
		buf.writeIdentifier(zoneId);
		
		ClientPlayNetworking.send(HZNetworking.ZONE_CREATE, buf);
		
		source.sendFeedback(Text.translatable("command.hammer.zone.create", zoneId));
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Creates a zone with the given ID.
	private static int executeZoneCreateId(CommandContext<FabricClientCommandSource> context) {
		var source = context.getSource();

		var zoneId = context.getArgument("id", Identifier.class);

		var buf = PacketByteBufs.create();
		buf.writeIdentifier(zoneId);
		
		ClientPlayNetworking.send(HZNetworking.ZONE_CREATE, buf);
		
		source.sendFeedback(Text.translatable("command.hammer.zone.create", zoneId));
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Lists the ten first zones in the world.
	private static int executeZoneList(CommandContext<FabricClientCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		var index = 0;
		
		var zones = ZoneManager.getAll(player.world);
		
		if (zones.isEmpty()) {
			source.sendFeedback(Text.translatable("command.hammer.zone.list.none"));
			
			return Command.SINGLE_SUCCESS;
		}
		
		source.sendFeedback(Text.translatable("command.hammer.zone.list.start"));
		
		for (var zone : zones) {
			if (index >= 10) {
				break;
			}
			
			index += 1;
			
			source.sendFeedback(Text.translatable("command.hammer.zone.list.entry", zone.getId(), String.format("#%08X", zone.getColor().toRgba())));
		}
		
		if (zones.size() / 10 > 0) {
			source.sendFeedback(Text.translatable("command.hammer.zone.list.end", 0, zones.size() / 10));
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Lists ten of the zones in the world at the given page.
	private static int executeZoneListPage(CommandContext<FabricClientCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		var page = context.getArgument("page", int.class);
		
		var index = 0;
		
		var zones = ZoneManager.getAll(player.world);
		
		if (zones.isEmpty()) {
			source.sendFeedback(Text.translatable("command.hammer.zone.list.none"));
			
			return Command.SINGLE_SUCCESS;
		}
		
		source.sendFeedback(Text.translatable("command.hammer.zone.list"));
		
		for (var zone : zones) {
			if (index > (page * 10) && index < ((page + 1) * 10)) {
				source.sendFeedback(Text.translatable("command.hammer.zone.list.entry", zone.getId(), String.format("#%08X", zone.getColor().toRgba())));
			}
			
			index += 1;
		}
		
		if (zones.size() / 10 > 0) {
			source.sendFeedback(Text.translatable("command.hammer.zone.list.end", page, zones.size() / 10));
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Changes the color of the selected zone.
	private static int executeZoneColor(CommandContext<FabricClientCommandSource> context) {
		var source = context.getSource();

		var zoneColor = context.getArgument("color", Color.class);
		
		var zone = HZValues.getSelectedZone();
		
		if (zone != null) {
			var buf = PacketByteBufs.create();
			buf.writeIdentifier(zone.getId());
			
			Color.toBuf(zoneColor, buf);
			
			ClientPlayNetworking.send(HZNetworking.ZONE_COLOR_CHANGED, buf);
			
			source.sendFeedback(Text.translatable("command.hammer.zone.color", String.format("#%08X", zone.getColor().toRgba())));
		} else {
			source.sendError(Text.translatable("command.hammer.zone.not_found"));
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Exports the selected zone.
	private static int executeZoneExport(CommandContext<FabricClientCommandSource> context) {
		var source = context.getSource();

		var zone = HZValues.getSelectedZone();
		
		if (zone != null) {
			var json = Zone.toJson(zone);
			
			var exportPath = InstanceUtil.getFabric().getGameDir();
			exportPath = exportPath.resolve("export");
			
			if (!Files.exists(exportPath)) {
				try {
					Files.createDirectories(exportPath);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			try {
				var zonePath = exportPath.resolve(zone.getId().toString().replace(":", "-") + ".json");

				var writer = HC.GSON.newJsonWriter(Files.newBufferedWriter(zonePath));
				
				HC.GSON.toJson(json, writer);
				
				writer.close();
				
				source.sendFeedback(Text.translatable("command.hammer.zone.export", zone.getId(), Text.literal(zonePath.getFileName().toString()).formatted(Formatting.UNDERLINE).styled(style -> {
					return style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, zonePath.toAbsolutePath().toString()));
				})));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			source.sendError(Text.translatable("command.hammer.zone.not_found"));
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Exports the zone with the given ID.
	private static int executeZoneExportId(CommandContext<FabricClientCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		var zoneId = context.getArgument("id", Identifier.class);
		var zone = ZoneManager.get(player.world, zoneId);
		
		if (zone != null) {
			var json = Zone.toJson(zone);
			
			var exportPath = InstanceUtil.getFabric().getGameDir();
			exportPath = exportPath.resolve("export");
			
			if (!Files.exists(exportPath)) {
				try {
					Files.createDirectories(exportPath);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			try {
				var zonePath = exportPath.resolve(zone.getId().toString().replace(":", "-") + ".json");
				
				var writer = HC.GSON.newJsonWriter(Files.newBufferedWriter(zonePath));
				
				HC.GSON.toJson(json, writer);
				
				writer.close();
				
				source.sendFeedback(Text.translatable("command.hammer.zone.export", zone.getId(), Text.literal(zonePath.getFileName().toString()).formatted(Formatting.UNDERLINE).styled(style -> {
					return style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, zonePath.toAbsolutePath().toString()));
				})));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			source.sendError(Text.translatable("command.hammer.zone.not_found"));
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Changes the group of the selected zone.
	private static int executeZoneGroup(CommandContext<FabricClientCommandSource> context) {
		var source = context.getSource();

		var zoneGroupId = context.getArgument("id", Identifier.class);

		var zone = HZValues.getSelectedZone();
		
		if (zone != null) {
			var buf = PacketByteBufs.create();
			buf.writeIdentifier(zone.getId());
			buf.writeIdentifier(zoneGroupId);
			
			ClientPlayNetworking.send(HZNetworking.ZONE_GROUP_CHANGED, buf);
			
			source.sendFeedback(Text.translatable("command.hammer.zone.group", zoneGroupId));
		} else {
			source.sendError(Text.translatable("command.hammer.zone.not_found"));
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Changes the color of the selected zone's group.
	// Only changes existing zones, future zones will not be affected.
	private static int zoneGroupColor(CommandContext<FabricClientCommandSource> context) {
		var source = context.getSource();

		var zoneColor = context.getArgument("color", Color.class);
		
		var zone = HZValues.getSelectedZone();
		
		if (zone != null) {
			var zoneGroup = zone.getGroup();
			
			if (zoneGroup != null) {
				for (var otherZone : zoneGroup.getZones()) {
					var buf = PacketByteBufs.create();
					buf.writeIdentifier(otherZone.getId());
					
					Color.toBuf(zoneColor, buf);
					
					ClientPlayNetworking.send(HZNetworking.ZONE_COLOR_CHANGED, buf);
				}
				
				source.sendFeedback(Text.translatable("command.hammer.zone.group.color", String.format("#%08X", zone.getColor().toRgba())));
			} else {
				source.sendError(Text.translatable("command.hammer.zone.group.not_found"));
			}
		} else {
			source.sendError(Text.translatable("command.hammer.zone.not_found"));
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Lists the ten first zone groups in the world.
	private static int executeZoneGroupList(CommandContext<FabricClientCommandSource> context) {
		var source = context.getSource();

		var index = 0;
		
		var groups = ZoneGroupManager.getGroups();
		
		if (groups.isEmpty()) {
			source.sendFeedback(Text.translatable("command.hammer.zone.group.list.none"));
			
			return Command.SINGLE_SUCCESS;
		}
		
		source.sendFeedback(Text.translatable("command.hammer.zone.group.list.start"));
		
		for (var group : groups) {
			if (index >= 10) {
				break;
			}
			
			index += 1;
			
			source.sendFeedback(Text.translatable("command.hammer.zone.group.list.entry.outer", group.getId()));
			
			for (var zone : group) {
				source.sendFeedback(Text.translatable("command.hammer.zone.group.list.entry.inner", zone.getId()));
			}
		}
		
		if (groups.size() / 10 > 0) {
			source.sendFeedback(Text.translatable("command.hammer.zone.group.list.end", 0, groups.size() / 10));
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Lists ten of the zone groups in the world at the given page.
	private static int executeZoneGroupListPage(CommandContext<FabricClientCommandSource> context) {
		var source = context.getSource();

		var page = context.getArgument("page", int.class);
		
		var index = 0;
		
		var groups = ZoneGroupManager.getGroups();
		
		if (groups.isEmpty()) {
			source.sendFeedback(Text.translatable("command.hammer.zone.group.list.none"));
			
			return Command.SINGLE_SUCCESS;
		}
		
		source.sendFeedback(Text.translatable("command.hammer.zone.group.list.start"));
		
		for (var group : groups) {
			if (index > (page * 10) && index < ((page + 1) * 10)) {
				source.sendFeedback(Text.translatable("command.hammer.zone.group.list.entry.outer", group.getId()));
				
				for (var zone : group) {
					source.sendFeedback(Text.translatable("command.hammer.zone.group.list.entry.inner", zone.getId()));
				}
			}
			
			index += 1;
		}
		
		if (groups.size() / 10 > 0) {
			source.sendFeedback(Text.translatable("command.hammer.zone.group.list.end", 0, groups.size() / 10));
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Creates a zone group.
	private static int executeZoneGroupCreate(CommandContext<FabricClientCommandSource> context) {
		var source = context.getSource();

		var groupId = context.getArgument("id", Identifier.class);
		
		var buf = PacketByteBufs.create();
		buf.writeIdentifier(groupId);
		
		ClientPlayNetworking.send(HZNetworking.ZONE_GROUP_CREATE, buf);
		
		ZoneGroupManager.getOrCreate(groupId);
		
		source.sendFeedback(Text.translatable("command.hammer.zone.group.create", groupId));
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Deletes a zone group.
	private static int executeZoneGroupDelete(CommandContext<FabricClientCommandSource> context) {
		var source = context.getSource();

		var groupId = context.getArgument("id", Identifier.class);
		
		var buf = PacketByteBufs.create();
		buf.writeIdentifier(groupId);
		
		ClientPlayNetworking.send(HZNetworking.ZONE_GROUP_DELETE, buf);
		
		ZoneGroupManager.remove(groupId);
		
		source.sendFeedback(Text.translatable("command.hammer.zone.group.delete", groupId));
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Exports a zone group.
	private static int executeZoneGroupExport(CommandContext<FabricClientCommandSource> context) {
		var source = context.getSource();

		var groupId = context.getArgument("id", Identifier.class);
		
		var group = ZoneGroupManager.getOrCreate(groupId);
		
		var exportPath = InstanceUtil.getFabric().getGameDir();
		exportPath = exportPath.resolve("export");
		
		if (!Files.exists(exportPath)) {
			try {
				Files.createDirectories(exportPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		var zoneGroupPath = exportPath.resolve(group.getId().toString().replace(":", "-") + ".json");
		
		if (!Files.exists(zoneGroupPath)) {
			try {
				Files.createFile(zoneGroupPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		for (var zone : group) {
			var json = Zone.toJson(zone);
			
			try {
				var zonePath = zoneGroupPath.resolve(zone.getId().toString().replace(":", "-") + ".json");
				
				var writer = HC.GSON.newJsonWriter(Files.newBufferedWriter(zonePath));
				
				HC.GSON.toJson(json, writer);
				
				writer.close();
				
				source.sendFeedback(Text.translatable("command.hammer.zone.group.export", zone.getId(), Text.literal(zonePath.getFileName().toString()).formatted(Formatting.UNDERLINE).styled(style -> {
					return style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, zonePath.toAbsolutePath().toString()));
				})));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		source.sendFeedback(Text.translatable("command.hammer.zone.group.export", groupId));
		
		return Command.SINGLE_SUCCESS;
	}
	
	public static void init() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			var zoneNode =
					literal("zone")
							.requires(HZCommands::requiresOp);
			
			var zoneEditorNode =
					literal("editor")
							.executes(HZCommands::executeZoneEditor);
			
			var zoneSelectNode =
					literal("select")
							.executes(HZCommands::executeZoneSelect)
							.then(
									argument("id", identifier())
											.suggests(HZCommands::suggestZoneIds)
											.executes(HZCommands::zoneSelectId)
							);
			
			var zoneDeselectNode =
					literal("deselect")
							.executes(HZCommands::executeZoneDeselect);
			
			var zoneCreateNode =
					literal("create")
							.executes(HZCommands::executeZoneCreate)
							.then(
									argument("id", identifier())
											.executes(HZCommands::executeZoneCreateId)
					);
			
			var zoneDeleteNode =
					literal("delete")
							.executes(HZCommands::executeZoneDelete)
							.then(
									argument("id", identifier())
											.suggests(HZCommands::suggestZoneIds)
											.executes(HZCommands::executeZoneDeleteId)
					);
			
			var zoneListNode =
					literal("list")
							.executes(HZCommands::executeZoneList)
							.then(
									argument("page", integer())
											.executes(HZCommands::executeZoneListPage)
							);
			
			var zoneColorNode =
					literal("color")
							.then(
									literal("set")
											.then(
													argument("color", color())
															.executes(HZCommands::executeZoneColor)
											)
							);
			
			var zoneExportNode =
					literal("export")
							.executes(HZCommands::executeZoneExport)
							.then(
									argument("id", identifier())
											.suggests(HZCommands::suggestZoneIds)
											.executes(HZCommands::executeZoneExportId)
							);
			
			var zoneGroupNode =
					literal("group");
			
			var zoneGroupSetNode =
					literal("set")
							.then(
									argument("id", identifier())
											.suggests(HZCommands::suggestZoneGroupIds)
											.executes(HZCommands::executeZoneGroup)
							);
			
			var zoneGroupListNode =
					literal("list")
							.executes(HZCommands::executeZoneGroupList)
							.then(
									argument("page", integer())
											.executes(HZCommands::executeZoneGroupListPage)
							);
			
			var zoneGroupCreateNode =
					literal("create")
							.then(
									argument("id", identifier())
											.executes(HZCommands::executeZoneGroupCreate)
							);
			
			var zoneGroupDeleteNode =
					literal("delete")
							.then(
									argument("id", identifier())
											.suggests(HZCommands::suggestZoneGroupIds)
											.executes(HZCommands::executeZoneGroupDelete)
							);
			
			var zoneGroupExportNode =
					literal("export")
							.then(
									argument("id", identifier())
											.suggests(HZCommands::suggestZoneGroupIds)
											.executes(HZCommands::executeZoneGroupExport)
							);
			
			var zoneGroupColorNode =
					literal("color")
							.then(
									argument("color", color())
											.suggests(HZCommands::suggestZoneGroupIds)
											.executes(HZCommands::zoneGroupColor)
							);
			
			zoneNode.then(zoneEditorNode);
			zoneNode.then(zoneSelectNode);
			zoneNode.then(zoneDeselectNode);
			zoneNode.then(zoneCreateNode);
			zoneNode.then(zoneDeleteNode);
			zoneNode.then(zoneListNode);
			zoneNode.then(zoneColorNode);
			zoneNode.then(zoneExportNode);
			zoneNode.then(zoneGroupNode);
			
			zoneGroupNode.then(zoneGroupSetNode);
			zoneGroupNode.then(zoneGroupListNode);
			zoneGroupNode.then(zoneGroupCreateNode);
			zoneGroupNode.then(zoneGroupDeleteNode);
			zoneGroupNode.then(zoneGroupExportNode);
			zoneGroupNode.then(zoneGroupColorNode);
			
			dispatcher.register(zoneNode);
		});
	}
}
