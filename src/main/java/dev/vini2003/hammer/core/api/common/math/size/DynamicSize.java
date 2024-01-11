package dev.vini2003.hammer.core.api.common.math.size;

import dev.vini2003.hammer.core.api.common.supplier.FloatSupplier;

public class DynamicSize extends Size {
	private final FloatSupplier widthSupplier;
	private final FloatSupplier heightSupplier;
	private final FloatSupplier lengthSupplier;
	
	/**
	 * Constructs a dynamic size.
	 *
	 * @param width  the width size component.
	 * @param height the height size component.
	 * @param length the length size component.
	 */
	public DynamicSize(FloatSupplier width, FloatSupplier height, FloatSupplier length) {
		this.widthSupplier = width;
		this.heightSupplier = height;
		this.lengthSupplier = length;
	}
	
	/**
	 * Constructs a dynamic size.
	 *
	 * @param width  the width size component
	 * @param height the height size component.
	 */
	public DynamicSize(FloatSupplier width, FloatSupplier height) {
		this.widthSupplier = width;
		this.heightSupplier = height;
		this.lengthSupplier = () -> 0.0F;
	}
	
	/**
	 * Constructs a dynamic size.
	 *
	 * @param anchor the anchor,
	 * @param relativeSize the relative size.
	 */
	public DynamicSize(SizeHolder anchor, SizeHolder relativeSize) {
		this(() -> anchor.getWidth() + relativeSize.getWidth(), () -> anchor.getHeight() + relativeSize.getHeight(), () -> anchor.getLength() + relativeSize.getLength());
	}
	
	/**
	 * Constructs an anchored dynamic size.
	 *
	 * @param anchor the anchor.
	 * @param width  the relative with size component.
	 * @param height the relative height size component.
	 * @param length the relative length size component.
	 */
	public DynamicSize(SizeHolder anchor, FloatSupplier width, FloatSupplier height, FloatSupplier length) {
		this(() -> anchor.getWidth() + width.get(), () -> anchor.getHeight() + height.get(), () -> anchor.getLength() + length.get());
	}
	
	/**
	 * Constructs an anchored dynamic size.
	 *
	 * @param anchor the anchor.
	 * @param width  the relative with size component.
	 * @param height the relative height size component.
	 */
	public DynamicSize(SizeHolder anchor, FloatSupplier width, FloatSupplier height) {
		this(() -> anchor.getWidth() + width.get(), () -> anchor.getHeight() + height.get(), anchor::getLength);
	}
	
	/**
	 * Constructs an anchor's size.
	 *
	 * @param anchor the anchor.
	 */
	public DynamicSize(SizeHolder anchor) {
		this(anchor::getWidth, anchor::getHeight, anchor::getLength);
	}
	
	
	/**
	 * Returns this size, adding another size to it.
	 *
	 * @param size the size.
	 *
	 * @return the resulting size.
	 */
	public DynamicSize plus(SizeHolder size) {
		return new DynamicSize(() -> getWidth() + size.getWidth(), () -> getHeight() + size.getHeight(), () -> getLength() + size.getLength());
	}
	
	/**
	 * Returns this size, subtracting another size from it.
	 *
	 * @param size the size.
	 *
	 * @return the resulting size.
	 */
	public DynamicSize minus(SizeHolder size) {
		return new DynamicSize(() -> getWidth() + size.getWidth(), () -> getHeight() + size.getHeight(), () -> getLength() + size.getLength());
	}
	
	/**
	 * Returns this size, multiplying its components by a given number.
	 *
	 * @param number the number.
	 *
	 * @return the resulting size.
	 */
	public DynamicSize times(FloatSupplier number) {
		return new DynamicSize(() -> getWidth() * number.get(), () -> getHeight() * number.get(), () -> getLength() * number.get());
	}
	
	/**
	 * Returns this size, dividing its components by a given number.
	 *
	 * @param number the number.
	 *
	 * @return the resulting size.
	 */
	public DynamicSize div(FloatSupplier number) {
		return new DynamicSize(() -> getWidth() / number.get(), () -> getHeight() / number.get(), () -> getLength() / number.get());
	}
	
	@Override
	public float getWidth() {
		return widthSupplier.get();
	}
	
	@Override
	public float getHeight() {
		return heightSupplier.get();
	}
	
	@Override
	public float getLength() {
		return lengthSupplier.get();
	}
}
