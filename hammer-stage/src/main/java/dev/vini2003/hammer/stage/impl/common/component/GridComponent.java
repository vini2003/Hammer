package dev.vini2003.hammer.stage.impl.common.component;

import dev.vini2003.hammer.core.api.common.component.base.Component;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GridComponent implements Component {
	private static final String POSITIONS_KEY = "Positions";
	
	private static final ThreadLocal<Random> RANDOM = ThreadLocal.withInitial(Random::new);
	
	private final World world;
	
	private final Set<Position> positions = new HashSet<>();
	
	public GridComponent(World world) {
		this.world = world;
	}
	
	public Position allocatePosition() {
		var random = RANDOM.get();
		
		var position = new Position(0.0F, 0.0F, 0.0F);
		
		while (true) {
			// Get random X and Z positions.
			var randomX = random.nextInt(24000000 * 2) - 24000000;
			var randomZ = random.nextInt(24000000 * 2) - 24000000;
			
			// Modulate it into the 1024-block grid.
			randomX %= 1024;
			randomZ %= 1024;
			
			// Get the position.
			position = new Position(randomX, 0.0F, randomZ);
			
			// Stop if the position does not exist.
			if (!positions.contains(position)) {
				positions.add(position);
				
				break;
			}
		}
		
		return position;
	}
	
	public void deallocatePosition(Position position) {
		positions.remove(position);
	}
	
	@Override
	public void writeToNbt(NbtCompound nbt) {
		var positionsNbtList = new NbtList();
		
		for (var position : positions) {
			var positionNbt = Position.toNbt(position);
			
			positionsNbtList.add(positionNbt);
		}
		
		nbt.put(POSITIONS_KEY, positionsNbtList);
	}
	
	@Override
	public void readFromNbt(NbtCompound nbt) {
		// Clear existing positions.
		positions.clear();
		
		var positionsNbtList = nbt.getList(POSITIONS_KEY, NbtElement.COMPOUND_TYPE);
		
		for (var i = 0; i < positionsNbtList.size(); i++) {
			var positionNbt = positionsNbtList.getCompound(i);
			
			var position = Position.fromNbt(positionNbt);
			
			positions.add(position);
		}
	}
}
