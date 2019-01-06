package net.sf.fmj.media;

import javax.media.Buffer;
import javax.media.Codec;
import javax.media.Format;

import net.sf.fmj.utility.LoggingStringUtils;

/**
 * Abstract base class to implement Codec.
 *
 * @author Ken Larson
 *
 */
public abstract class AbstractCodec extends AbstractPlugIn implements Codec {
	protected Format inputFormat = null;
	protected Format outputFormat = null;
	protected boolean opened = false;
	protected Format[] inputFormats = new Format[0];

	protected boolean checkInputBuffer(Buffer b) {
		return true; // TODO
	}

	protected final void dump(String label, Buffer buffer) {
		System.out.println(label + ": " + LoggingStringUtils.bufferToStr(buffer));

	}

	protected Format getInputFormat() {
		return inputFormat;
	}

	protected Format getOutputFormat() {
		return outputFormat;
	}

	@Override
	public Format[] getSupportedInputFormats() {
		return inputFormats;
	}

	@Override
	public abstract Format[] getSupportedOutputFormats(Format input);

	protected boolean isEOM(Buffer b) {
		return b.isEOM();
	}

	@Override
	public abstract int process(Buffer input, Buffer output);

	protected void propagateEOM(Buffer b) {
		b.setEOM(true);
	}

	@Override
	public Format setInputFormat(Format format) {
		this.inputFormat = format;
		return inputFormat;
	}

	@Override
	public Format setOutputFormat(Format format) {
		this.outputFormat = format;
		return outputFormat;
	}

}
