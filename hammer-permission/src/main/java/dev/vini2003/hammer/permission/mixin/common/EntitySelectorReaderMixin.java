package dev.vini2003.hammer.permission.mixin.common;

import dev.vini2003.hammer.permission.impl.common.accessor.EntitySelectorReaderAccessor;
import net.minecraft.command.EntitySelectorReader;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntitySelectorReader.class)
public class EntitySelectorReaderMixin implements EntitySelectorReaderAccessor {
	private boolean selectsRole;
	private boolean excludesRole;	
	
	@Override
	public boolean selectsRole() {
		return selectsRole;
	}
	
	@Override
	public void setSelectsRole(boolean selectsRole) {
		this.selectsRole = selectsRole;
	}
	
	@Override
	public boolean excludesRole() {
		return excludesRole;
	}
	
	@Override
	public void setExcludesRole(boolean excludesRole) {
		this.excludesRole = excludesRole;
	}
}
