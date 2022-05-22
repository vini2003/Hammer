package dev.vini2003.hammer.core.api.client.texture;

import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.client.util.DrawingUtil;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.client.util.LayerUtil;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class PartitionedTexture implements Texture {
	private final Identifier textureId;
	
	private final float textureWidth;
	private final float textureHeight;
	
	private final float leftPadding;
	private final float rightPadding;
	private final float topPadding;
	private final float bottomPadding;
	
	private final Part topLeft;
	private final Part topRight;
	private final Part bottomLeft;
	private final Part bottomRight;
	private final Part middleLeft;
	private final Part middleRight;
	private final Part middleTop;
	private final Part middleBottom;
	private final Part center;
	
	public PartitionedTexture(Identifier textureId, float textureWidth, float textureHeight, float leftPadding, float rightPadding, float topPadding, float bottomPadding) {
		this.textureId = textureId;
		
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		
		this.leftPadding = leftPadding;
		this.rightPadding = rightPadding;
		this.topPadding = topPadding;
		this.bottomPadding = bottomPadding;
		
		this.topLeft = new Part(0.0F, 0.0F, leftPadding, topPadding);
		this.topRight = new Part(1.0F - rightPadding, 0.0F, 1.0F, topPadding);
		this.bottomLeft = new Part(0.0F, 1.0F - bottomPadding, leftPadding, 1.0F);
		this.bottomRight = new Part(1.0F - rightPadding, 1.0F - bottomPadding, 1.0F, 1.0F);
		this.middleLeft = new Part(0.0F, topPadding, leftPadding, 1.0F - bottomPadding);
		this.middleRight = new Part(1.0F - rightPadding, topPadding, 1.0F, 1.0F - bottomPadding);
		this.middleTop = new Part(leftPadding, 0.0F, 1.0F - rightPadding, topPadding);
		this.middleBottom = new Part(leftPadding, 1.0F - bottomPadding, 1.0F - rightPadding, 1.0F);
		this.center = new Part(leftPadding, topPadding, 1.0F - rightPadding, 1.0F - bottomPadding);
	}
	
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float width, float height) {
		var scaleWidth = width / textureWidth;
		var scaleHeight = height / textureHeight;
		
		var topLeftWidth = (width * (topLeft.uE - topLeft.uS) / scaleWidth);
		var topLeftHeight = (height * (topLeft.vE - topLeft.vS) / scaleHeight);
		
		var topRightWidth = (width * (topRight.uE - topRight.uS) / scaleWidth);
		var topRightHeight = (height * (topRight.vE - topRight.vS) / scaleHeight);
		
		
		var bottomLeftWidth = (width * (bottomLeft.uE - bottomLeft.uS) / scaleWidth);
		var bottomLeftHeight = (height * (bottomLeft.vE - bottomLeft.vS) / scaleHeight);
		;
		var bottomRightWidth = (width * (bottomRight.uE - bottomRight.uS) / scaleWidth);
		var bottomRightHeight = (height * (bottomRight.vE - bottomRight.vS) / scaleHeight);
		
		
		var middleTopWidth = (width * (middleTop.uE + middleTop.uS) - topLeftWidth - topRightWidth);
		var middleTopHeight = (height * (middleTop.vE - middleTop.vS) / scaleHeight);
		
		var middleBottomWidth = (width * (middleBottom.uE + middleBottom.uS) - bottomLeftWidth - bottomRightWidth);
		var middleBottomHeight = (height * (middleBottom.vE - middleBottom.vS) / scaleHeight);
		
		
		var middleLeftWidth = (width * (middleLeft.uE - middleLeft.uS) / scaleWidth);
		var middleLeftHeight = (height * (middleLeft.vE + middleLeft.vS) - topLeftHeight - topRightHeight);
		
		var middleRightWidth = (width * (middleRight.uE - middleRight.uS) / scaleWidth);
		var middleRightHeight = (height * (middleRight.vE + middleRight.vS) - topLeftHeight - topRightHeight);
		
		
		var centerWidth = (width * (center.uE + center.uS) - topLeftWidth - topRightWidth);
		var centerHeight = (height * (center.vE + center.vS) - topLeftHeight - topRightHeight);
		
		
		var heightMultiplier = height / (topLeftHeight + middleLeftHeight + bottomLeftHeight);
		
		middleLeftHeight *= heightMultiplier;
		middleRightHeight *= heightMultiplier;
		centerHeight *= heightMultiplier;
		
		var client = InstanceUtil.getClient();
		
		var scaledX = x - (x % (client.getWindow().getScaledWidth() / (float) client.getWindow().getWidth()));
		var scaledY = y - (y % (client.getWindow().getScaledHeight() / (float) client.getWindow().getHeight()));
		
		var layer = LayerUtil.get(textureId);
		
		DrawingUtil.drawTexturedQuad(
				matrices,
				provider,
				textureId,
				scaledX, scaledY, 0.0F,
				topLeftWidth, topLeftHeight,
				topLeft.uS, topLeft.vS,
				topLeft.uE, topLeft.vE,
				0.0F, 0.0F, 0.0F,
				DrawingUtil.DEFAULT_OVERLAY,
				DrawingUtil.DEFAULT_LIGHT,
				Color.WHITE,
				layer
		);
		
		DrawingUtil.drawTexturedQuad(
				matrices,
				provider,
				textureId,
				scaledX + topLeftWidth, scaledY, 0.0F,
				middleTopWidth, middleTopHeight,
				middleTop.uS, middleTop.vS,
				middleTop.uE, middleTop.vE,
				0.0F, 0.0F, 0.0F,
				DrawingUtil.DEFAULT_OVERLAY,
				DrawingUtil.DEFAULT_LIGHT,
				Color.WHITE,
				layer
		);
		
		DrawingUtil.drawTexturedQuad(
				matrices,
				provider,
				textureId,
				scaledX + topLeftWidth + middleTopWidth, scaledY, 0.0F,
				topRightWidth, topRightHeight,
				topRight.uS, topRight.vS,
				topRight.uE, topRight.vE,
				0.0F, 0.0F, 0.0F,
				DrawingUtil.DEFAULT_OVERLAY,
				DrawingUtil.DEFAULT_LIGHT,
				Color.WHITE,
				layer
		);
		
		DrawingUtil.drawTexturedQuad(
				matrices,
				provider,
				textureId,
				scaledX, scaledY + topRightHeight, 0.0F,
				middleLeftWidth, middleLeftHeight,
				middleLeft.uS, middleLeft.vS,
				middleLeft.uE, middleLeft.vE,
				0.0F, 0.0F, 0.0F,
				DrawingUtil.DEFAULT_OVERLAY,
				DrawingUtil.DEFAULT_LIGHT,
				Color.WHITE,
				layer
		);
		
		DrawingUtil.drawTexturedQuad(
				matrices,
				provider,
				textureId,
				scaledX + middleLeftWidth + middleTopWidth, scaledY + topRightHeight, 0.0F,
				middleRightWidth, middleRightHeight,
				middleRight.uS, middleRight.vS,
				middleRight.uE, middleRight.vE,
				0.0F, 0.0F, 0.0F,
				DrawingUtil.DEFAULT_OVERLAY,
				DrawingUtil.DEFAULT_LIGHT,
				Color.WHITE,
				layer
		);
		
		DrawingUtil.drawTexturedQuad(
				matrices,
				provider,
				textureId,
				scaledX + topLeftWidth, scaledY + topLeftHeight, 0.0F,
				centerWidth, centerHeight,
				center.uS, center.vS,
				center.uE, center.vE,
				0.0F, 0.0F, 0.0F,
				DrawingUtil.DEFAULT_OVERLAY,
				DrawingUtil.DEFAULT_LIGHT,
				Color.WHITE,
				layer
		);
		
		DrawingUtil.drawTexturedQuad(
				matrices,
				provider,
				textureId,
				scaledX, scaledY + centerHeight + topLeftHeight, 0.0F,
				bottomLeftWidth, bottomLeftHeight,
				bottomLeft.uS, bottomLeft.vS,
				bottomLeft.uE, bottomLeft.vE,
				0.0F, 0.0F, 0.0F,
				DrawingUtil.DEFAULT_OVERLAY,
				DrawingUtil.DEFAULT_LIGHT,
				Color.WHITE,
				layer
		);
		
		DrawingUtil.drawTexturedQuad(
				matrices,
				provider,
				textureId,
				scaledX + topLeftWidth, scaledY + centerHeight + topLeftHeight, 0.0F,
				middleBottomWidth, middleBottomHeight,
				middleBottom.uS, middleBottom.vS,
				middleBottom.uE, middleBottom.vE,
				0.0F, 0.0F, 0.0F,
				DrawingUtil.DEFAULT_OVERLAY,
				DrawingUtil.DEFAULT_LIGHT,
				Color.WHITE,
				layer
		);
		
		DrawingUtil.drawTexturedQuad(
				matrices,
				provider,
				textureId,
				scaledX + topLeftWidth + middleBottomWidth, scaledY + centerHeight + topLeftHeight, 0.0F,
				bottomRightWidth, bottomRightHeight,
				bottomRight.uS, bottomRight.vS,
				bottomRight.uE, bottomRight.vE,
				0.0F, 0.0F, 0.0F,
				DrawingUtil.DEFAULT_OVERLAY,
				DrawingUtil.DEFAULT_LIGHT,
				Color.WHITE,
				layer
		);
	}
	
	record Part(
			float uS, float vS,
			float uE, float vE
	) {}
}
