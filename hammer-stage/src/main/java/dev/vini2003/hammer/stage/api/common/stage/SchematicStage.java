package dev.vini2003.hammer.stage.api.common.stage;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.stage.registry.common.HSComponents;
import dev.vini2003.hammer.stage.registry.common.HSDimensions;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.io.BufferedInputStream;

public abstract class SchematicStage extends Stage {
	private final Identifier schematicId;
	
	public SchematicStage(Identifier id, Identifier schematicId) {
		super(id);
		
		this.schematicId = schematicId;
		
		var server = InstanceUtil.getServer();
		var world = server.getWorld(HSDimensions.VOID_WORLD_KEY);
		
		try {
			var gridComponent = HSComponents.GRID.get(world);
			if (gridComponent == null) throw new RuntimeException("Grid component is null!");
			
			var gridPosition = gridComponent.allocatePosition();
			
			setPosition(gridPosition);
			setSize(new Size(1024.0F, 1024.0F));
		} catch (Exception exception) {
			throw new RuntimeException("Failed to create stage", exception);
		}
	}
	
	@Override
	public void load(World world) {
		var pos = getPosition();
		
		try {
			var server = world.getServer();
			if (server == null) throw new RuntimeException("Server is null");
			
			var resourceManager = server.getResourceManager();
			if (resourceManager == null) throw new RuntimeException("Resource manager is null");
			
			var resources = resourceManager.findResources("stages", (resource) -> resource.getPath().endsWith(".schem"));
			
			var resource = resources.get(new Identifier(schematicId.getNamespace(), "stages/" + schematicId.getPath() + ".schem"));
			if (resource == null) throw new RuntimeException("Stage schematic not found");
			
			var schematicFormat = BuiltInClipboardFormat.SPONGE_SCHEMATIC;
			
			var schematicInputStream = resource.getInputStream();
			var schematicBufferedInputStream = new BufferedInputStream(schematicInputStream);
			var schematicReader = schematicFormat.getReader(schematicBufferedInputStream);
			
			var schematicClipboard = schematicReader.read();
			
			HC.LOGGER.info("Loading schematic " + schematicId + " for stage " + id + " at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "...");
			
			var then = System.currentTimeMillis();
			
			try (var editSession = WorldEdit.getInstance().newEditSession(FabricAdapter.adapt(world))) {
				var operation = new ClipboardHolder(schematicClipboard)
						.createPaste(editSession)
						.to(BlockVector3.at(pos.getX(), pos.getY(), pos.getZ()))
						.copyBiomes(true)
						.copyEntities(true)
						.ignoreAirBlocks(false)
						.build();
				
				Operations.complete(operation);
			}
			
			var now = System.currentTimeMillis();
			
			HC.LOGGER.info("Loaded schematic " + schematicId + " for stage " + id + " in " + (now - then) + "ms.");
		} catch (Exception e) {
			throw new RuntimeException("Failed to load stage", e);
		}
		
		super.load(world);
	}
}
