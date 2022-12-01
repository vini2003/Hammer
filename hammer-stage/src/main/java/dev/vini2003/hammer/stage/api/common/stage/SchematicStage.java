package dev.vini2003.hammer.stage.api.common.stage;

import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public abstract class SchematicStage extends Stage {
	private final String schematicId;
	
	public SchematicStage(Identifier id, Position position, Size size, String schematicId) {
		super(id, position, size);
		
		this.schematicId = schematicId;
	}
	
	@Override
	public void load(World world) {
		
		
		super.load(world);
	}
}
