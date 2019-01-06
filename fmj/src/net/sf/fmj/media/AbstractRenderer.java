package net.sf.fmj.media;

import javax.media.Buffer;
import javax.media.Format;
import javax.media.Renderer;

/**
 * Abstract implementation of Renderer, useful for subclassing.
 *
 * @author Ken Larson
 *
 */
public abstract class AbstractRenderer extends AbstractPlugIn implements Renderer {
	protected Format inputFormat;

	@Override
	public abstract Format[] getSupportedInputFormats();

	@Override
	public abstract int process(Buffer buffer);

	@Override
	public Format setInputFormat(Format format) {
		this.inputFormat = format;
		return inputFormat;
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

}
