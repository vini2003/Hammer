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

package dev.vini2003.hammer.permission.impl.common.accessor;

import dev.vini2003.hammer.core.api.common.exception.NoMixinException;
import dev.vini2003.hammer.permission.api.common.role.Role;
import net.luckperms.api.node.Node;

import java.util.Collection;

public interface PlayerEntityAccessor {
	default void hammer$setRoleOutline(boolean roleOutline) {
		throw new NoMixinException();
	}
	
	default boolean hammer$hasRoleOutline() {
		throw new NoMixinException();
	}
	
	default void hammer$addPermissionNode(Node node) {
		throw new NoMixinException();
	}
	
	default void hammer$removePermissionNode(Node node) {
		throw new NoMixinException();
	}
	
	default boolean hammer$hasPermission(String node) {
		throw new NoMixinException();
	}
	
	default boolean hammer$hasRole(Role role) {
		throw new NoMixinException();
	}
	
	default void hammer$addRole(Role role) {
		throw new NoMixinException();
	}
	
	default void hammer$removeRole(Role role) {
		throw new NoMixinException();
	}
	
	default Collection<Role> hammer$getRoles() {
		throw new NoMixinException();
	}
}
