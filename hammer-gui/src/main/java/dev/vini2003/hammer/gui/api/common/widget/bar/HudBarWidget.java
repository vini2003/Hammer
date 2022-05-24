package dev.vini2003.hammer.gui.api.common.widget.bar;

import java.util.function.BooleanSupplier;

public class HudBarWidget extends ImageBarWidget {
	protected BooleanSupplier showSupplier = () -> false;
	
	protected Side side = Side.RIGHT;
	protected Type type = Type.CONTINUOS;
	
	public void setShow(BooleanSupplier showSupplier) {
		this.showSupplier = showSupplier;
	}
	
	public void setShow(boolean show) {
		setShow(() -> show);
	}
	
	public boolean shouldShow() {
		return showSupplier.getAsBoolean();
	}
	
	public Side getSide() {
		return side;
	}
	
	public void setSide(Side side) {
		this.side = side;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public enum Side {
		LEFT,
		RIGHT
	}
	
	public enum Type {
		CONTINUOS,
		TILED
	}
}
