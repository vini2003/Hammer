package dev.vini2003.hammer.innit.registry.common;

import dev.vini2003.hammer.permission.api.common.manager.RoleManager;
import dev.vini2003.hammer.permission.api.common.role.Role;

public class HIRoles {
	public static final Role PARTICIPANT = new Role("participant", "&#83cbff\uD83C\uDF10", 1, 0x83CBFF);
	public static final Role SPECTATOR = new Role("spectator", "&#7e7e7e\uD83D\uDCF9", 1, 0x7E7E7E);
	public static final Role STAFF = new Role("staff", "&#fb00c4\uD83D\uDD2E", 2, 0xFB00C4);
	
	public static void init() {
		RoleManager.register(PARTICIPANT);
		RoleManager.register(SPECTATOR);
		RoleManager.register(STAFF);
	}
}
