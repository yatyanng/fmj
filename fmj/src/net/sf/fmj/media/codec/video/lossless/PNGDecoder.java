package net.sf.fmj.media.codec.video.lossless;

import javax.media.Format;

import net.sf.fmj.media.codec.video.ImageIODecoder;
import net.sf.fmj.media.format.PNGFormat;

/**
 * PNG decoder Codec.
 *
 * @author Ken Larson
 *
 */
public class PNGDecoder extends ImageIODecoder {
	private final Format[] supportedInputFormats = new Format[] { new PNGFormat(), };

	public PNGDecoder() {
		super("PNG");
	}

	@Override
	public Format[] getSupportedInputFormats() {
		return supportedInputFormats;
	}
}
