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

package dev.vini2003.hammer.chat.mixin.client;

import dev.vini2003.hammer.chat.registry.common.HCOptions;
import net.minecraft.client.gui.screen.option.ChatOptionsScreen;
import net.minecraft.client.option.Option;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatOptionsScreen.class)
public class ChatOptionsScreenMixin {
	@Shadow
	@Final
	private static Option[] OPTIONS;
	
	@Inject(at = @At("TAIL"), method = "<clinit>")
	private static void hammer$init(CallbackInfo ci) {
		var prevOptions = OPTIONS;
		
		OPTIONS = new Option[OPTIONS.length + 5];
		
		for (var i = 0; i < OPTIONS.length - 5; ++i) {
			OPTIONS[i] = prevOptions[i];
		}
		
		OPTIONS[prevOptions.length] = HCOptions.SHOW_CHAT;
		OPTIONS[prevOptions.length + 1] = HCOptions.SHOW_COMMAND_FEEDBACK;
		OPTIONS[prevOptions.length + 2] = HCOptions.SHOW_WARNINGS;
		OPTIONS[prevOptions.length + 3] = HCOptions.SHOW_DIRECT_MESSAGES;
		OPTIONS[prevOptions.length + 4] = HCOptions.FAST_CHAT_FADE;
	}
}
