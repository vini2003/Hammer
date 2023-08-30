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

package dev.vini2003.hammer.gui.api.common.widget.text;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.texture.PartitionedTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.core.api.common.util.TextUtil;
import dev.vini2003.hammer.gui.api.common.widget.provider.TextureProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * <b>This widget is client-side only.</b>
 */
public class TextAreaWidget extends TextEditorWidget implements TextureProvider {
	public static final Size STANDARD_SIZE = new Size(90.0F, 72.0F);
	
	public static final Texture STANDARD_TEXTURE = new PartitionedTexture(HC.id("textures/widget/text_area.png"), 18.0F, 18.0F, 0.055F, 0.055F, 0.055F, 0.055F);
	
	protected Supplier<Texture> texture = () -> STANDARD_TEXTURE;
	
	protected List<Boolean> newLine = new ArrayList<>();
	
	protected boolean lineWrap = true;
	
	@Override
	public Size getStandardSize() {
		return STANDARD_SIZE;
	}
	
	public boolean getLineWrap() {
		return lineWrap;
	}
	
	public void setLineWrap(boolean lineWrap) {
		this.lineWrap = lineWrap;
	}
	
	protected boolean hasNewLine(int index) {
		return lineWrap ? newLine.get(index) : true;
	}
	
	protected int getNewlinedLineLength(int index) {
		return getLineLength(index) + (hasNewLine(index) ? 1 : 0);
	}
	
	@Override
	protected int getStringIndex(Cursor cursor) {
		int i = 0;
		for (int y = 0; y < cursor.getY(); ++y) {
			i += getNewlinedLineLength(y);
		}
		i += cursor.getX();
		return i;
	}
	
	@Override
	public void setText(String text) {
		if (lineWrap) {
			lines.clear();
			newLine.clear();
			
			this.text = text;
			
			var currentLine = "";
			var lineWidth = 0.0F;
			
			for (var c : text.toCharArray()) {
				lineWidth += Math.round(TextUtil.getWidth(String.valueOf(c)) * scale);
				
				if (lineWidth >= getInnerWidth() || c == '\n') {
					lines.add(currentLine);
					newLine.add(c == '\n');
					lineWidth = Math.round(TextUtil.getWidth(String.valueOf(c)) * scale);
					currentLine = "";
				}
				
				if (c != '\n') currentLine += c;
			}
			
			var finalLine = currentLine;
			
			if (!finalLine.isEmpty() || text.endsWith("\n")) {
				newLine.add(text.endsWith("\n"));
				lines.add(finalLine);
				newLine.add(true);
			}
		} else {
			super.setText(text);
		}
	}
	
	@Override
	protected void processKeyActions(int keyPressed, int character, int keyModifier) {
		if (keyPressed == GLFW.GLFW_KEY_BACKSPACE && selection.isEmpty()) {
			var lineLength = getLineLength(cursor.getY());
			
			var deleteCursor = cursor.copy();
			deleteCursor.moveLeft();
			
			var deleted = removeText(deleteCursor, cursor);
			
			if (deleted.equals("")) {
				cursor.moveLeft();
				
				deleteCursor = cursor.copy();
				deleteCursor.moveLeft();
				
				removeText(deleteCursor, cursor);
			}
			
			if (deleted.equals("\n")) {
				for (int i = 0; i < lineLength; i++) {
					cursor.moveLeft();
				}
			}
			
			cursor.moveLeft();
			
			return;
		} else if (keyPressed == GLFW.GLFW_KEY_DELETE && selection.isEmpty()) {
			var deleteCursor = cursor.copy();
			deleteCursor.moveRight();
			
			var deleted = removeText(cursor, deleteCursor);
			
			if (deleted.equals("")) {
				deleteCursor = cursor.copy();
				deleteCursor.moveRight();
				deleteCursor.moveRight();
				
				removeText(cursor, deleteCursor);
			}
			
			return;
		}
		
		super.processKeyActions(keyPressed, character, keyModifier);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void draw(DrawContext context, float tickDelta) {
		var matrices = context.getMatrices();
		var provider = context.getVertexConsumers();
		
		texture.get().draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
		
		if (lineWrap && xOffset != 0.0F) {
			xOffset = 0.0F;
		}
		
		renderField(matrices, provider);
	}
	
	@Override
	public Supplier<Texture> getTexture() {
		return texture;
	}
	
	@Override
	public void setTexture(Supplier<Texture> texture) {
		this.texture = texture;
	}
}
