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

package dev.vini2003.hammer.core.api.client.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.vini2003.hammer.core.api.common.exception.NoPlatformImplementationException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.ApiStatus;

import java.nio.file.Path;
import java.util.function.Supplier;

public class InstanceUtil {
	private static Supplier<Object> SERVER_SUPPLIER = () -> null;
	
	@ExpectPlatform
	public static Path getConfigPath() {
		throw new NoPlatformImplementationException();
	}
	
	@ExpectPlatform
	public static Path getPersistentObjectPath() {
		throw new NoPlatformImplementationException();
	}
	
	public static MinecraftClient getClient() {
		return MinecraftClient.getInstance();
	}
	
	public static MinecraftServer getServer() {
		return (MinecraftServer) SERVER_SUPPLIER.get();
	}
	
	@ExpectPlatform
	public static boolean isClient() {
		throw new NoPlatformImplementationException();
	}
	
	@ExpectPlatform
	public static boolean isServer() {
		throw new NoPlatformImplementationException();
	}
	
	@ExpectPlatform
	public static boolean isDevelopmentEnvironment() {
		throw new NoPlatformImplementationException();
	}
	
	@ApiStatus.Internal
	public static void setServerSupplier(Supplier<MinecraftServer> supplier) {
		SERVER_SUPPLIER = supplier::get;
	}
}
