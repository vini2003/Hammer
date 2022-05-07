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

package dev.vini2003.hammer.permission.api.common.util.extension

import dev.vini2003.hammer.permission.api.common.role.Role
import dev.vini2003.hammer.permission.api.common.util.PermissionUtils
import net.minecraft.entity.player.PlayerEntity

fun PlayerEntity.hasRole(role: Role) = (world.isClient && role.holders.contains(uuid)) || PermissionUtils.hasPermission(this, role.inheritanceNode.key)

fun PlayerEntity.addRole(role: Role) = role.addTo(this)
fun PlayerEntity.removeRole(role: Role) = role.removeFrom(this)

fun PlayerEntity.addRoleInClient(role: Role) = role.addToClient(uuid)
fun PlayerEntity.removeRoleInClient(role: Role) = role.removeFromClient(uuid)