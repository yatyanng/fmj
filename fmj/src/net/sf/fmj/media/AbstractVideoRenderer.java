package net.sf.fmj.media;

import java.awt.Component;
import java.awt.Rectangle;

import javax.media.Buffer;
import javax.media.control.FrameGrabbingControl;
import javax.media.renderer.VideoRenderer;

/**
 * Abstract implementation of VideoRenderer, useful for subclassing.
 *
 * @author Ken Larson
 *
 */
public abstract class AbstractVideoRenderer extends AbstractRenderer implements VideoRenderer, FrameGrabbingControl {
	private Rectangle bounds = null;

	private Buffer lastBuffer;

	protected abstract int doProcess(Buffer buffer);

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public abstract Component getComponent();

	@Override
	public Component getControlComponent() {
		return null;
	}

	@Override
	public Buffer grabFrame() {
		return lastBuffer;
	}

	@Override
	public final int process(Buffer buffer) {
		lastBuffer = buffer; // TODO: clone?
		return doProcess(buffer);
	}

	@Override
	public void setBounds(Rectangle rect) {
		this.bounds = rect;
	}

	@Override
	public boolean setComponent(Component comp) {
		// default implementation does not allow changing of component.
		return false;
	}

}
