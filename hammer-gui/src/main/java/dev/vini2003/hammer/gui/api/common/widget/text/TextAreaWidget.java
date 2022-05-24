package dev.vini2003.hammer.gui.api.common.widget.text;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.texture.PartitionedTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.common.util.TextUtil;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TextAreaWidget extends TextEditorWidget {
	public static final Texture STANDARD_TEXTURE = new PartitionedTexture(HC.id("textures/widget/text_area.png"), 18.0F, 18.0F, 0.055F, 0.055F, 0.055F, 0.055F);
	
	protected Supplier<Texture> texture = () -> STANDARD_TEXTURE;
	
	protected List<Boolean> newLines = new ArrayList<>();
	
	protected boolean lineWrap = true;
	
	protected boolean hasNewLine(int index) {
		if (lineWrap) {
			return newLines.get(index);
		} else {
			return true;
		}
	}

	protected int getNewlinedLineLength(int index) {
		var lineLength = getLineLength(index);
		
		if (hasNewLine(index)) {
			return lineLength + 1;
		} else {
			return lineLength;
		}
	}
	
	@Override
	protected int getStringIndex(Cursor cursor) {
		var index = 0;
		
		for (var y = 0; y < cursor.getY(); ++y) {
			index += getNewlinedLineLength(y);
		}
		
		index += cursor.getX();
		
		return index;
	}
	
	@Override
	public void setText(String text) {
		if (lineWrap) {
			this.text = text;
			
			lines.clear();
			newLines.clear();
			
			var currentLine = "";
			
			var lineWidth = 0.0F;
			
			for (var character : text.toCharArray()) {
				lineWidth += Math.round(TextUtil.getWidth(String.valueOf(character)) * scale);
				
				if (lineWidth >= getInnerWidth() || character == '\n') {
					lines.add(currentLine);
					newLines.add(character == '\n');
					
					lineWidth = Math.round(TextUtil.getWidth(String.valueOf(character)) * scale);
					
					currentLine = "";
				}
				
				if (character == '\n') {
					currentLine += character;
				}
			}
			
			var finalLine = currentLine.toString();
			
			if (!finalLine.isEmpty() || text.endsWith("\n")) {
				newLines.add(text.endsWith("\n"));
				
				lines.add(finalLine);
				newLines.add(true);
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
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {
		texture.get().draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
		
		if (lineWrap && xOffset != 0.0F) {
			xOffset = 0.0F;
		}
		
		renderField(matrices, provider);
	}
	
	public void setTexture(Supplier<Texture> texture) {
		this.texture = texture;
	}
	
	public void setTexture(Texture texture) {
		setTexture(() -> texture);
	}
}
