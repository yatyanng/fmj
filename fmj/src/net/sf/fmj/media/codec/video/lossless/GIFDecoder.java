package net.sf.fmj.media.codec.video.lossless;

import javax.media.Format;

import net.sf.fmj.media.codec.video.ImageIODecoder;
import net.sf.fmj.media.format.GIFFormat;

/**
 * GIF decoder Codec.
 *
 * @author Ken Larson
 *
 */
public class GIFDecoder extends ImageIODecoder {
	private final Format[] supportedInputFormats = new Format[] { new GIFFormat(), };

	public GIFDecoder() {
		super("GIF");
	}

	@Override
	public Format[] getSupportedInputFormats() {
		return supportedInputFormats;
	}
}
