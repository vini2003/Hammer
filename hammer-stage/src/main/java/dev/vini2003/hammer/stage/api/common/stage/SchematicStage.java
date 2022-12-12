package dev.vini2003.hammer.stage.api.common.stage;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.stage.registry.common.HSComponents;
import dev.vini2003.hammer.stage.registry.common.HSDimensions;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.io.BufferedInputStream;

public abstract class SchematicStage extends Stage {
	private final String schematicId;
	
	public SchematicStage(Identifier id, String schematicId) {
		super(id);
		
		this.schematicId = schematicId;
		
		var server = InstanceUtil.getServer();
		var world = server.getWorld(HSDimensions.VOID_WORLD_KEY);
		
		var gridComponent = HSComponents.GRID.get(world);
		var gridPosition = gridComponent.allocatePosition();
		
		setPosition(gridPosition);
		setSize(new Size(1024.0F, 1024.0F));
	}
	
	@Override
	public void load(World world) {
		var pos = getPosition();
		
		try {
			var server = world.getServer();
			if (server == null) return;
			
			var resourceManager = server.getResourceManager();
			if (resourceManager == null) return;
			
			var resources = resourceManager.findResources("stages", (resource) -> resource.getPath().endsWith(".schem"));
			
			var resource = resources.get(new Identifier("hammer:stages/test_stage.schem"));
			if (resource == null) return;
			
			var schematicFormat = BuiltInClipboardFormat.SPONGE_SCHEMATIC;
			
			var schematicInputStream = resource.getInputStream();
			var schematicBufferedInputStream = new BufferedInputStream(schematicInputStream);
			var schematicReader = schematicFormat.getReader(schematicBufferedInputStream);
			
			var schematicClipboard = schematicReader.read();
			
			System.out.println(pos.getX() + " " + pos.getY() + " " + pos.getZ());
			
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		super.load(world);
	}
}
