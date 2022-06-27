package dev.vini2003.hammer.permission.impl.common.accessor;

public interface EntitySelectorReaderAccessor {
	boolean selectsRole();
	
	void setSelectsRole(boolean selectsRole);
	
	boolean excludesRole();
	
	void setExcludesRole(boolean excludesRole);
}
