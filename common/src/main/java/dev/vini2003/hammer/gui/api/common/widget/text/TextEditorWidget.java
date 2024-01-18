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

import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.client.scissor.Scissors;
import dev.vini2003.hammer.core.api.client.util.DrawingUtil;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.client.util.LayerUtil;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.position.StaticPosition;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.core.api.common.util.TextUtil;
import dev.vini2003.hammer.gui.api.common.event.*;
import dev.vini2003.hammer.gui.api.common.filter.InputFilter;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <b>This widget is client-side only.</b>
 */
public abstract class TextEditorWidget extends Widget {
	public static final Size STANDARD_SIZE = Size.of(90.0F, 72.0F);
	
	protected final List<String> lines = new ArrayList<>();
	
	protected final Cursor cursor = new Cursor(0, 0);
	protected final Cursor prevMouseCursor = new Cursor(0, 0);
	
	protected String text = "";
	
	protected double scale = 1.0;
	
	protected boolean editable = true;
	
	protected float xOffset = 0;
	protected float yOffset = 0;
	
	protected int cursorTick = 20;
	
	protected InputFilter<?> filter;
	
	protected Selection selection = new Selection();
	
	@Override
	public Size getStandardSize() {
		return STANDARD_SIZE;
	}
	
	public void onTick() {
		if (cursorTick > 0) {
			--cursorTick;
		} else {
			cursorTick = 20;
		}
		
		super.onTick();
	}
	
	@Override
	protected void onKeyPressed(KeyPressedEvent event) {
		super.onKeyPressed(event);
		
		if (!held) {
			return;
		}
		
		processKeyActions(event.keyCode(), event.scanCode(), event.keyModifiers());
		
		var selectionStart = selection.getStart();
		var selectionEnd = selection.getEnd();
		
		if (selectionStart.equals(selectionEnd)) {
			setSelection(new Selection());
		}
		
		cursorTick = 20;
	}
	
	@Override
	protected void onCharacterTyped(CharacterTypedEvent event) {
		super.onCharacterTyped(event);
		
		if (filter == null || filter.accepts(String.valueOf(event.character()), text)) {
			if (held) {
				if (!selection.isEmpty()) {
					var selectionStart = selection.getStart();
					var selectionEnd = selection.getEnd();
					
					cursor.copyFrom(selectionStart);
					
					removeText(selectionStart, selectionEnd);
					
					setSelection(new Selection());
				}
				
				addText(String.valueOf(event.character()));
				
				var prevY = cursor.getY();
				
				cursor.moveRight();
				
				if (cursor.getY() != prevY) {
					cursor.moveRight();
				}
				
				onCursorMoved();
			}
			
			cursorTick = 20;
		}
	}
	
	@Override
	protected void onMouseClicked(MouseClickedEvent event) {
		super.onMouseClicked(event);
		
		if (!isHidden() && editable && event.button() == 0) {
			setHeld(isPointWithin(event.x(), event.y()));
			
			cursorTick = 20;
			
			if (isHeld()) {
				var mouseCursor = getCursor(event.x(), event.y());
				
				if (!mouseCursor.isEmpty()) {
					prevMouseCursor.copyFrom(mouseCursor);
					cursor.copyFrom(prevMouseCursor);
					
					onCursorMoved();
				}
				
				if (!selection.isEmpty()) {
					setSelection(new Selection());
				}
			}
		}
	}
	
	@Override
	protected void onMouseDragged(MouseDraggedEvent event) {
		super.onMouseDragged(event);
		
		if (!isHidden() && editable && event.button() == 0 && held) {
			cursorTick = 20;
			
			var mousePos = getCursor(event.x(), event.y());
			
			var cursorUpdated = false;
			
			var selectionStart = selection.getStart();
			var selectionEnd = selection.getEnd();
			
			if (!mousePos.isEmpty()) {
				if (mousePos.lessThan(prevMouseCursor)) {
					selectionStart.copyFrom(mousePos);
					selectionEnd.copyFrom(prevMouseCursor);
				} else {
					selectionStart.copyFrom(prevMouseCursor);
					selectionEnd.copyFrom(mousePos);
				}
				
				cursor.copyFrom(mousePos);
				
				cursorUpdated = true;
			}
			
			if (event.y() > getInnerPosition().getY() + getInnerSize().getHeight()) {
				cursor.moveDown();
				
				selectionEnd.moveDown();
				
				cursorUpdated = true;
			} else if (event.y() < getInnerPosition().getY()) {
				cursor.moveUp();
				
				selectionStart.moveUp();
				
				cursorUpdated = true;
			}
			
			if (cursorUpdated) {
				onCursorMoved();
			}
		}
	}
	
	@Override
	public boolean isFocused() {
		return super.isFocused() || held;
	}
	
	@Override
	protected void onMouseScrolled(MouseScrolledEvent event) {
		super.onMouseScrolled(event);
		
		var charHeight = getCharHeight();
		
		var textHeight = (lines.size() - 1) * charHeight;
		
		if (textHeight <= getInnerSize().getHeight()) {
			return;
		}
		
		yOffset += event.deltaY() * charHeight;
		
		if (yOffset > 0) {
			yOffset = 0;
		}
		
		if (yOffset < -textHeight) {
			yOffset = -textHeight;
		}
	}
	
	protected void processKeyActions(int keyPressed, int character, int keyModifier) {
		int prevY;
		Cursor prevCursor;
		
		switch (keyPressed) {
			case GLFW.GLFW_KEY_ESCAPE: {
				setHeld(false);
				
				break;
			}
			
			case GLFW.GLFW_KEY_ENTER: {
				addText("\n");
				
				cursor.moveRight();
				
				onCursorMoved();
				
				break;
			}
			
			case GLFW.GLFW_KEY_C: {
				if (Screen.hasControlDown() && !selection.isEmpty()) {
					var client = InstanceUtil.getClient();
					
					var selectionStart = selection.getStart();
					var selectionEnd = selection.getEnd();
					
					client.keyboard.setClipboard(getSelectionText(selectionStart, selectionEnd));
				}
				
				break;
			}
			
			case GLFW.GLFW_KEY_V: {
				if (Screen.hasControlDown()) {
					var client = InstanceUtil.getClient();
					
					var clipboard = client.keyboard.getClipboard();
					
					var selectionStart = selection.getStart();
					var selectionEnd = selection.getEnd();
					
					if (!selection.isEmpty()) {
						cursor.copyFrom(selectionStart);
						
						removeText(selectionStart, selectionEnd);
						
						setSelection(new Selection());
					}
					
					addText(clipboard);
					
					prevY = cursor.getY();
					
					for (var i = 0; i < clipboard.length(); i++) {
						cursor.moveRight();
					}
					
					if (cursor.getY() != prevY) {
						cursor.moveRight();
					}
					
					onCursorMoved();
				}
				
				break;
			}
			
			case GLFW.GLFW_KEY_D: {
				if (Screen.hasControlDown()) {
					cursor.setX(0);
					cursor.setY(0);
					
					setText("");
					
					onCursorMoved();
				}
				
				break;
			}
			
			case GLFW.GLFW_KEY_X: {
				if (Screen.hasControlDown()) {
					var client = InstanceUtil.getClient();
					
					var selectionStart = selection.getStart();
					var selectionEnd = selection.getEnd();
					
					client.keyboard.setClipboard(getSelectionText(selectionStart, selectionEnd));
					
					cursor.copyFrom(selectionStart);
					
					onCursorMoved();
					
					removeText(selectionStart, selectionEnd);
					
					setSelection(new Selection());
				}
				
				break;
			}
			
			case GLFW.GLFW_KEY_A: {
				if (Screen.hasControlDown()) {
					var selectionStart = selection.getStart();
					var selectionEnd = selection.getEnd();
					
					selectionStart.copyFrom(new Cursor(0, 0));
					selectionEnd.copyFrom(new Cursor(getLineLength(lines.size() - 1), lines.size() - 1));
					
					cursor.copyFrom(selectionEnd);
					
					onCursorMoved();
				}
				
				break;
			}
			
			case GLFW.GLFW_KEY_BACKSPACE: {
				if (!selection.isEmpty()) {
					var selectionStart = selection.getStart();
					var selectionEnd = selection.getEnd();
					
					cursor.copyFrom(selectionStart);
					
					removeText(selectionStart, selectionEnd);
					
					setSelection(new Selection());
				} else {
					var lineLength = getLineLength(cursor.getY());
					
					var deleteCursor = cursor.copy();
					deleteCursor.moveLeft();
					
					var deleted = removeText(deleteCursor, cursor);
					
					if (deleted.equals("\n")) {
						for (var i = 0; i < lineLength; i++) {
							cursor.moveLeft();
						}
					}
					
					cursor.moveLeft();
				}
				
				onCursorMoved();
				
				break;
			}
			
			case GLFW.GLFW_KEY_DELETE: {
				if (!selection.isEmpty()) {
					var selectionStart = selection.getStart();
					var selectionEnd = selection.getEnd();
					
					cursor.copyFrom(selectionStart);
					
					onCursorMoved();
					
					removeText(selectionStart, selectionEnd);
					
					setSelection(new Selection());
				} else {
					var deleteCursor = cursor.copy();
					deleteCursor.moveRight();
					
					removeText(cursor, deleteCursor);
				}
				
				break;
			}
			
			case GLFW.GLFW_KEY_LEFT: {
				if (!selection.isEmpty() && !Screen.hasShiftDown()) {
					setSelection(new Selection());
				}
				
				var selectionStart = selection.getStart();
				var selectionEnd = selection.getEnd();
				
				if (Screen.hasShiftDown()) {
					if (selection.isEmpty()) {
						selectionEnd.copyFrom(cursor);
						selectionStart.copyFrom(cursor);
					}
				}
				
				prevY = cursor.getY();
				
				cursor.moveLeft();
				
				if (Screen.hasShiftDown()) {
					if (cursor.getY() != prevY) {
						cursor.moveLeft();
					}
					
					if (cursor.lessThan(selectionStart)) {
						selectionStart.moveLeft();
						
						if (cursor.getY() != prevY) {
							selectionStart.moveLeft();
						}
					} else if (cursor.greaterThan(selectionStart)) {
						selectionEnd.moveLeft();
						
						if (cursor.getY() != prevY) {
							selectionEnd.moveLeft();
						}
					}
				}
				
				onCursorMoved();
				
				break;
			}
			
			case GLFW.GLFW_KEY_RIGHT: {
				if (!selection.isEmpty() && !Screen.hasShiftDown()) {
					setSelection(new Selection());
				}
				
				var selectionStart = selection.getStart();
				var selectionEnd = selection.getEnd();
				
				if (Screen.hasShiftDown()) {
					if (!!selection.isEmpty()) {
						selectionEnd.copyFrom(cursor);
						selectionStart.copyFrom(cursor);
					}
				}
				
				prevY = cursor.getY();
				
				cursor.moveRight();
				
				if (Screen.hasShiftDown()) {
					if (cursor.getY() != prevY) {
						cursor.moveRight();
					}
					
					if (cursor.greaterThan(selectionEnd)) {
						selectionEnd.moveRight();
						
						if (cursor.getY() != prevY) {
							selectionEnd.moveRight();
						}
					} else if (cursor.lessThan(selectionEnd)) {
						selectionStart.moveRight();
						
						if (cursor.getY() != prevY) {
							selectionStart.moveRight();
						}
					} else {
						setSelection(new Selection());
					}
				}
				
				onCursorMoved();
				
				break;
			}
			
			case GLFW.GLFW_KEY_UP: {
				if (!selection.isEmpty() && !Screen.hasShiftDown()) {
					setSelection(new Selection());
				}
				
				var selectionStart = selection.getStart();
				var selectionEnd = selection.getEnd();
				
				if (Screen.hasShiftDown()) {
					if (selection.isEmpty()) {
						selectionEnd.copyFrom(cursor);
					} else {
						if (cursor.equals(selectionStart)) {
							selectionStart.copyFrom(selectionEnd);
						}
					}
				}
				
				prevCursor = cursor.copy();
				
				cursor.moveUp();
				
				if (Screen.hasShiftDown()) {
					if (cursor.lessThan(selectionStart)) {
						if (!prevCursor.lessThan(selectionEnd)) {
							selectionEnd.copyFrom(selectionStart);
						}
						
						selectionStart.copyFrom(cursor);
					} else {
						if (selection.isEmpty()) {
							selectionEnd.copyFrom(prevCursor);
							selectionStart.copyFrom(cursor);
						} else {
							selectionEnd.copyFrom(cursor);
						}
					}
				}
				
				onCursorMoved();
				
				break;
			}
			
			case GLFW.GLFW_KEY_DOWN: {
				if (!selection.isEmpty() && !Screen.hasShiftDown()) {
					setSelection(new Selection());
				}
				
				var selectionStart = selection.getStart();
				var selectionEnd = selection.getEnd();
				
				if (Screen.hasShiftDown()) {
					if (selection.isEmpty()) {
						selectionStart.copyFrom(cursor);
					} else {
						if (cursor.equals(selectionStart)) {
							selectionStart.copyFrom(selectionEnd);
						}
					}
				}
				
				cursor.moveDown();
				
				if (Screen.hasShiftDown()) {
					if (cursor.greaterThan(selectionEnd)) {
						selectionEnd.copyFrom(cursor);
					} else if (cursor.lessThan(selectionEnd)) {
						selectionStart.copyFrom(cursor);
					}
				}
				
				onCursorMoved();
				
				break;
			}
			
			case GLFW.GLFW_KEY_HOME: {
				if (!selection.isEmpty() && !Screen.hasShiftDown()) {
					setSelection(new Selection());
				}
				
				var selectionStart = selection.getStart();
				var selectionEnd = selection.getEnd();
				
				if (Screen.hasShiftDown()) {
					if (selection.isEmpty()) {
						selectionEnd.copyFrom(cursor);
					} else {
						if (!cursor.lessThan(selectionEnd)) {
							selectionEnd.copyFrom(selectionStart);
						}
					}
				}
				
				cursor.setX(0);
				
				if (Screen.hasControlDown()) {
					cursor.setY(0);
				}
				
				if (Screen.hasShiftDown()) {
					if (!cursor.lessThan(selectionEnd)) {
						selectionEnd.copyFrom(cursor);
					} else {
						selectionStart.copyFrom(cursor);
					}
				}
				
				onCursorMoved();
				
				break;
			}
			
			case GLFW.GLFW_KEY_END: {
				if (!selection.isEmpty() && !Screen.hasShiftDown()) {
					setSelection(new Selection());
				}
				
				var selectionStart = selection.getStart();
				var selectionEnd = selection.getEnd();
				
				if (Screen.hasShiftDown()) {
					if (selection.isEmpty()) {
						selectionStart.copyFrom(cursor);
					} else {
						if (!cursor.greaterThan(selectionStart)) {
							selectionStart.copyFrom(selectionEnd);
						}
					}
				}
				
				if (Screen.hasControlDown()) {
					cursor.setY(lines.size() - 1);
				}
				
				cursor.setX(getLineLength(cursor.getY()));
				
				if (Screen.hasShiftDown()) {
					selectionEnd.copyFrom(cursor);
				}
				
				onCursorMoved();
				
				break;
			}
			
			case GLFW.GLFW_KEY_PAGE_UP: {
				if (!selection.isEmpty() && !Screen.hasShiftDown()) {
					setSelection(new Selection());
				}
				
				var selectionStart = selection.getStart();
				var selectionEnd = selection.getEnd();
				
				if (Screen.hasShiftDown()) {
					if (selection.isEmpty()) {
						selectionEnd.copyFrom(cursor);
					} else {
						if (cursor.equals(selectionEnd)) {
							selectionEnd.copyFrom(selectionStart);
						}
					}
				}
				
				for (var i = 0; i < Math.min(lines.size() - 1, getVisibleLines()); i++) {
					cursor.moveUp();
				}
				
				if (Screen.hasShiftDown()) {
					selectionStart.copyFrom(cursor);
				}
				
				onCursorMoved();
				
				break;
			}
			
			case GLFW.GLFW_KEY_PAGE_DOWN: {
				if (!selection.isEmpty() && !Screen.hasShiftDown()) {
					setSelection(new Selection());
				}
				
				var selectionStart = selection.getStart();
				var selectionEnd = selection.getEnd();
				
				if (Screen.hasShiftDown()) {
					if (selection.isEmpty()) {
						selectionStart.copyFrom(cursor);
					} else {
						if (cursor.equals(selectionStart)) {
							selectionStart.copyFrom(selectionEnd);
						}
					}
				}
				
				for (var i = 0; i < Math.min(lines.size() - 1, getVisibleLines()); i++) {
					cursor.moveDown();
				}
				
				if (Screen.hasShiftDown()) {
					selectionEnd.copyFrom(cursor);
				}
				
				onCursorMoved();
				
				break;
			}
		}
	}
	
	protected void onCursorMoved() {
		var innerPos = getInnerPosition();
		var innerSize = getInnerSize();
		
		var innerX = getInnerX();
		var innerY = getInnerY();
		
		var innerWidth = getInnerWidth();
		var innerHeight = getInnerHeight();
		
		var lineOffset = getLineOffset();
		
		var charHeight = getCharHeight();
		
		var cursorX = innerX + getCharX(cursor.getY(), cursor.getX()) - 1;
		var cursorY = innerY + (charHeight + 2) * (cursor.getY() - lineOffset) - 2;
		
		var offsetCursorX = cursorX + xOffset;
		var yRenderOffset = yOffset + lineOffset * charHeight;
		
		var offsetCursorStartY = cursorY + yRenderOffset;
		var offsetCursorEndY = cursorY + yRenderOffset + charHeight;
		
		if (offsetCursorX > innerX + innerWidth) {
			xOffset -= (offsetCursorX - (innerX + innerWidth)) + 2;
		} else if (offsetCursorX < innerX) {
			var compensateOffset = xOffset + (innerX - offsetCursorX);
			
			var widthOffset = xOffset + innerWidth;
			
			xOffset = Math.min(0, Math.max(widthOffset, compensateOffset));
		}
		
		if (offsetCursorEndY > innerY + innerHeight) {
			yOffset -= offsetCursorEndY - (innerY + innerHeight);
		} else if (offsetCursorStartY < innerY) {
			yOffset = Math.min(0, yOffset + innerY - (offsetCursorStartY + 2));
		}
	}
	
	protected void renderField(MatrixStack matrices, VertexConsumerProvider provider) {
		var z = getZ();
		
		var innerPos = getInnerPosition();
		var innerSize = getInnerSize();
		
		var innerX = getInnerX();
		var innerY = getInnerY();
		
		var innerWidth = getInnerWidth();
		var innerHeight = getInnerHeight();
		
		var textRenderer = DrawingUtil.getTextRenderer();
		
		if (isEmpty() && !held) {
			textRenderer.draw(text, innerX, innerY, 0xFFFFFF, false, matrices.peek().getPositionMatrix(), provider, TextRenderer.TextLayerType.NORMAL, 0, 15728880);
			
			return;
		}
		
		var client = InstanceUtil.getClient();
		
		var scaleFactor = client.getWindow().getScaleFactor();
		
		var windowHeight = client.getWindow().getHeight();
		
		var scissors = new Scissors((int) innerX - 1, (int) innerY - 2, (int) innerWidth, (int) innerHeight, provider);
		
		var lineOffset = getLineOffset();
		
		var charHeight = getCharHeight();
		
		var cursorX = innerX + getCharX(cursor.getY(), cursor.getX()) - 1;
		var cursorY = innerY + (charHeight + 2) * (cursor.getY() - lineOffset) - 2;
		
		var yRenderOffset = yOffset + lineOffset * charHeight;
		
		matrices.push();
		
		matrices.translate(xOffset, yRenderOffset, 0.0F);
		
		for (var i = (int) lineOffset; i < lineOffset + getVisibleLines(); i++) {
			if (i < 0 || !isLineVisible(i) || i > lines.size() - 1) {
				continue;
			}
			
			var adjustedI = i - lineOffset;
			
			var line = lines.get(i);
			
			textRenderer.draw(line, innerX, innerY + (charHeight + 2) * adjustedI, 0xFFFFFF, false, matrices.peek().getPositionMatrix(), provider, TextRenderer.TextLayerType.NORMAL, 0, 15728880);
			
			var selection = getSelection(i);
			
			if (selection != null) {
				var selW = getCharX(i, selection.getEnd().x) - getCharX(i, selection.getStart().getX());
				
				DrawingUtil.drawQuad(
						matrices,
						provider,
						innerX + (getCharX(i, selection.getStart().getX())), innerY + (charHeight + 2) * adjustedI, 0.0F,
						selW, charHeight + 1,
						new Color(0.0F, 0.0F, 0.5F, 0.33F),
						LayerUtil.getInterface()
				);
			}
		}
		
		if (held && cursorTick > 10) {
			DrawingUtil.drawQuad(
					matrices,
					provider,
					cursorX, cursorY, 0.0F,
					1, charHeight + 2,
					Color.WHITE,
					LayerUtil.getInterface()
			);
		}
		
		matrices.pop();
		
		scissors.destroy();
	}
	
	protected void addText(String insert) {
		var cursorIndex = getStringIndex(cursor);
		
		setText(text.substring(0, cursorIndex) + insert + text.substring(cursorIndex));
	}
	
	protected String removeText(Cursor start, Cursor end) {
		var startIndex = getStringIndex(start);
		var endIndex = getStringIndex(end);
		
		if (endIndex == 0 || startIndex > text.length() || endIndex > text.length()) {
			return "";
		}
		
		var deleted = text.substring(startIndex, endIndex);
		
		setText(text.substring(0, startIndex) + text.substring(endIndex));
		
		return deleted;
	}
	
	public boolean isEmpty() {
		return text.isEmpty();
	}
	
	protected float getLineOffset() {
		return (-yOffset / getCharHeight());
	}
	
	protected int getStringIndex(Cursor cursor) {
		var i = 0;
		
		for (var y = 0; y < cursor.getY(); y++) {
			i += getNewlinedLineLength(y);
		}
		
		i += cursor.getX();
		
		return i;
	}
	
	protected String getSelectionText(Cursor start, Cursor end) {
		var startIndex = getStringIndex(start);
		var endIndex = getStringIndex(end);
		
		if (startIndex < 0 || startIndex > text.length() || endIndex < 0 || endIndex > text.length()) {
			return "";
		}
		
		return text.substring(startIndex, endIndex);
	}
	
	protected Cursor getCursor(float mouseX, float mouseY) {
		var innerPos = getInnerPosition();
		
		var x = -1;
		var y = -1;
		
		var lineOffset = getLineOffset();
		
		var charHeight = getCharHeight();
		
		var offsetMouseX = -xOffset + mouseX;
		
		for (var i = 0; i < getVisibleLines(); i++) {
			if (mouseY >= getInnerY() + (charHeight + 2) * i && mouseY <= getInnerY() + (charHeight + 2) * i + charHeight) {
				if (lineOffset + i > lines.size() - 1) {
					y = lines.size() - 1;
					
					if (offsetMouseX >= getInnerX() + getLineWidth(y)) {
						x = getLineLength(y);
					}
					
					break;
				}
				
				y = (int) (lineOffset + i);
				
				if (offsetMouseX >= getInnerX() + getLineWidth(y)) {
					x = getLineLength(y);
					
					break;
				}
			}
		}
		
		for (var j = 0; j < getLineLength(y); j++) {
			if (offsetMouseX + 2 >= getInnerX() + getCharX(y, j) && offsetMouseX + 2 <= getInnerX() + getCharX(y, j + 1)) {
				x = j;
				
				break;
			}
		}
		
		return new Cursor(x, y);
	}
	
	protected float getCharX(int lineIndex, int charIndex) {
		if (charIndex < 0 || lines.isEmpty() || lineIndex > lines.size() - 1) {
			return 0;
		}
		
		var line = lines.get(lineIndex);
		
		var endIndex = Math.min(charIndex, line.length());
		
		return (float) (TextUtil.getWidth(line.substring(0, endIndex)) * scale);
	}
	
	protected boolean isLineVisible(int line) {
		var lineOffset = getLineOffset();
		
		return line >= lineOffset && line <= lineOffset + getVisibleLines();
	}
	
	protected Selection getSelection(int lineIndex) {
		var selectionStart = selection.getStart();
		var selectionEnd = selection.getEnd();
		
		if (selectionStart.isEmpty() || selectionEnd.isEmpty()) {
			return null;
		}
		
		if (lineIndex < selectionStart.getY() || lineIndex > selectionEnd.getY()) {
			return null;
		}
		
		var startX = selectionStart.getY() == lineIndex ? selectionStart.getX() : 0;
		var endX = selectionEnd.getY() == lineIndex ? selectionEnd.getX() : getLineLength(lineIndex);
		
		return new Selection(new Cursor(startX, selectionStart.getY()), new Cursor(endX, selectionEnd.getY()));
	}
	
	protected int getLineWidth(int lineIndex) {
		if (lineIndex < 0) {
			return 0;
		}
		
		if (lineIndex > lines.size() - 1) {
			return 0;
		}
		
		return (int) (TextUtil.getWidth(lines.get(lineIndex)) * scale);
	}
	
	protected int getLineLength(int index) {
		if (lines.isEmpty()) {
			return 0;
		}
		
		if (index < 0 || index > lines.size() - 1) {
			return 0;
		}
		
		return lines.get(index).length();
	}
	
	protected int getNewlinedLineLength(int index) {
		return getLineLength(index) + 1;
	}
	
	public double getScale() {
		return scale;
	}
	
	public boolean isEditable() {
		return editable;
	}
	
	protected int getVisibleColumns() {
		return (int) (getInnerSize().getWidth() / getCharWidth());
	}
	
	protected int getVisibleLines() {
		return (int) Math.max(1, getInnerSize().getHeight() / getCharHeight());
	}
	
	protected int getCharHeight() {
		return (int) Math.round(TextUtil.getHeight(text) * scale);
	}
	
	protected int getCharWidth() {
		return (int) Math.round(TextUtil.getWidth("m") * scale);
	}
	
	public StaticPosition getInnerPosition() {
		return Position.of(this, 3.0F, 3.0F);
	}
	
	public float getInnerX() {
		return getInnerPosition().getX();
	}
	
	public float getInnerY() {
		return getInnerPosition().getY();
	}
	
	public float getInnerZ() {
		return getInnerPosition().getZ();
	}
	
	public Size getInnerSize() {
		return Size.of(getWidth() - 6.0F, getHeight() - 6.0F);
	}
	
	public float getInnerWidth() {
		return getInnerSize().getWidth();
	}
	
	public float getInnerHeight() {
		return getInnerSize().getHeight();
	}
	
	public Selection getSelection() {
		return selection;
	}
	
	public void setSelection(Selection selection) {
		this.selection = selection;
	}
	
	public Cursor getSelectionEnd() {
		return selection.getEnd();
	}
	
	public Cursor getSelectionStart() {
		return selection.getStart();
	}
	
	public <T> void setFilter(InputFilter<T> filter) {
		this.filter = filter;
	}
	
	public <T> InputFilter<T> getFilter() {
		return (InputFilter<T>) filter;
	}
	
	public String getText() {
		return text;
	}
	
	public String getLine(int index) {
		if (index < 0 || index > lines.size() - 1) {
			return "";
		}
		
		return lines.get(index);
	}
	
	public void setText(String text) {
		lines.clear();
		
		this.text = text;
		
		for (var line : text.split("\n", -1)) {
			lines.add(line);
		}
	}
	
	class Selection {
		protected Cursor start = new Cursor();
		protected Cursor end = new Cursor();
		
		public Selection() {}
		
		public Selection(Cursor start, Cursor end) {
			this.start = start;
			this.end = end;
		}
		
		public boolean isEmpty() {
			return start.isEmpty() && end.isEmpty();
		}
		
		public Cursor getStart() {
			return start;
		}
		
		public void setStart(Cursor start) {
			this.start = start;
		}
		
		public Cursor getEnd() {
			return end;
		}
		
		public void setEnd(Cursor end) {
			this.end = end;
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			
			if (!(o instanceof Selection)) {
				return false;
			}
			
			var selection = (Selection) o;
			
			return Objects.equals(start, selection.start) && Objects.equals(end, selection.end);
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(start, end);
		}
	}
	
	class Cursor {
		private int x = -1;
		private int y = -1;
		
		public Cursor() {}
		
		public Cursor(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public boolean isEmpty() {
			return x == -1 && y == -1;
		}
		
		public void moveLeft() {
			x--;
			
			if (x < 0) {
				var prevY = y;
				
				moveUp();
				
				if (y != prevY) {
					x = getLineLength(y);
				} else {
					x = 0;
				}
			}
		}
		
		public void moveRight() {
			x++;
			
			if (x > getLineLength(y)) {
				var prevY = y;
				
				moveDown();
				
				if (y != prevY) {
					x = 0;
				} else {
					x = getLineLength(y);
				}
			}
		}
		
		public void moveUp() {
			y = Math.max(y - 1, 0);
			
			if (x > getLineLength(y)) {
				x = getLineLength(y);
			}
		}
		
		public void moveDown() {
			y = Math.min(y + 1, lines.size() - 1);
			
			if (x > getLineLength(y)) {
				x = getLineLength(y);
			}
		}
		
		public int getX() {
			return x;
		}
		
		public void setX(int x) {
			this.x = x;
		}
		
		public int getY() {
			return y;
		}
		
		public void setY(int y) {
			this.y = y;
		}
		
		public boolean lessThan(Cursor cursor) {
			return getStringIndex(this) < getStringIndex(cursor);
		}
		
		public boolean greaterThan(Cursor cursor) {
			return getStringIndex(this) > getStringIndex(cursor);
		}
		
		public Cursor copyFrom(Cursor cursor) {
			x = cursor.x;
			y = cursor.y;
			
			return this;
		}
		
		public Cursor copy() {
			return new Cursor(x, y);
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			
			if (!(o instanceof Cursor)) {
				return false;
			}
			
			var cursor = (Cursor) o;
			
			return x == cursor.x && y == cursor.y;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(x, y);
		}
	}
}