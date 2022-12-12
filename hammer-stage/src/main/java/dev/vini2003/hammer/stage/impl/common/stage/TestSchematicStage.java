package dev.vini2003.hammer.stage.impl.common.stage;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.stage.api.common.stage.SchematicStage;
import net.minecraft.util.Identifier;

public class TestSchematicStage extends SchematicStage {
	public TestSchematicStage() {
		super(HC.id("test"), "test_stage");
	}
}
