package com.t4l.jmf;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.media.Buffer;
import javax.media.Codec;
import javax.media.Format;
import javax.media.PlugIn;
import javax.media.ResourceUnavailableException;
import javax.media.format.JPEGFormat;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;

import net.sf.fmj.utility.LoggerSingleton;

class CustomByteArrayOutputStream extends OutputStream {
	int ctr = 0;
	byte[] data;

	public CustomByteArrayOutputStream(byte[] b) {
		data = b;
	}

	public int getBytesWritten() {
		return ctr;
	}

	@Override
	public void write(byte[] b) throws IOException {
		write(b, 0, b.length);
	}

	@Override
	public void write(byte[] b, int offset, int len) throws IOException {
		System.arraycopy(data, ctr, b, offset, len);
		ctr += len;
	}

	@Override
	public void write(int b) throws IOException {
		data[ctr] = (byte) b;
		ctr++;
	}
}

/**
 * This type of MOV file just stores frames as a series of JPEG images. This
 * codec uses ImageIO classes to convert from the jpeg VideoFormat to
 * RGBFormats.
 *
 * @author Jeremy Wood
 */
public class JPEGEncoder implements Codec {
	private static final Logger logger = LoggerSingleton.logger;

	private static final VideoFormat jpegFormat = new JPEGFormat();
	private static final RGBFormat rgbFormat = new RGBFormat(null, -1, Format.intArray, -1.f, -1, -1, -1, -1);

	static Hashtable imageTable = new Hashtable();

	/**
	 *
	 * @param image
	 * @param data
	 * @return the number if bytes written in the array
	 * @throws IOException
	 */
	protected static int writeJPEG(BufferedImage image, byte[] data) throws IOException {
		ImageWriter iw = ImageIO.getImageWritersByMIMEType("image/jpeg").next();
		ImageWriteParam iwParam = iw.getDefaultWriteParam();
		float quality = .8f;
		iwParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		iwParam.setCompressionQuality(quality);
		CustomByteArrayOutputStream out = new CustomByteArrayOutputStream(data);
		iw.setOutput(out);

		IIOImage img = new IIOImage(image, null, null);

		iw.write(null, img, iwParam);
		return out.getBytesWritten();
	}

	@Override
	public void close() {
		synchronized (imageTable) {
			imageTable.clear();
		}
	}

	@Override
	public Object getControl(String controlType) {
		return null;
	}

	@Override
	public Object[] getControls() {
		return new String[] {};
	}

	@Override
	public String getName() {
		return "JPEG Encoder";
	}

	@Override
	public Format[] getSupportedInputFormats() {
		return new VideoFormat[] { rgbFormat };
	}

	@Override
	public Format[] getSupportedOutputFormats(Format input) {
		if (input == null)
			return new VideoFormat[] { jpegFormat };

		if (input.relax().matches(rgbFormat)) {
			final VideoFormat inputVideoFormat = (VideoFormat) input;
			return new VideoFormat[] { new JPEGFormat(inputVideoFormat.getSize(), -1, Format.byteArray,
					inputVideoFormat.getFrameRate(), -1, -1) };
		}
		return new Format[] {};
	}

	@Override
	public void open() throws ResourceUnavailableException {
	}

	@Override
	public int process(Buffer input, Buffer output) {
		Format inputFormat = input.getFormat();
		Format outputFormat = output.getFormat();

		if (inputFormat.relax().matches(rgbFormat) && outputFormat.relax().matches(jpegFormat)) {
			return processRGBtoJPEG(input, output);
		}
		return PlugIn.BUFFER_PROCESSED_FAILED;
	}

	protected int processRGBtoJPEG(Buffer input, Buffer output) {
		synchronized (imageTable) {
			try {
				RGBFormat inputFormat = (RGBFormat) input.getFormat();
				VideoFormat outputFormat = (VideoFormat) output.getFormat();

				if (outputFormat == null) {
					int width = inputFormat.getSize().width;
					int height = inputFormat.getSize().height;
					outputFormat = new JPEGFormat(new Dimension(width, height), width * height + 200, // surely this is
																										// the max
																										// size, right?
							Format.byteArray, inputFormat.getFrameRate(), -1, // TODO:
																				// specify
																				// quality
							-1); // TODO: specify decimation
					output.setFormat(outputFormat);
				}

				int[] data = (int[]) input.getData();

				Dimension d = inputFormat.getSize();
				BufferedImage dest = (BufferedImage) imageTable.get(d);
				if (dest == null) {
					dest = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
				}
				RGBConverter.populateImage(data, input.getOffset(), dest, inputFormat);

				byte[] bytes;

				Object obj = output.getData();
				if (obj instanceof byte[]) {
					bytes = (byte[]) obj;
				} else {
					bytes = new byte[d.width * d.height + 200];
					output.setData(bytes);
				}

				int length = writeJPEG(dest, bytes);

				imageTable.put(d, dest);

				output.setLength(length);

				output.setDiscard(input.isDiscard());
				output.setDuration(input.getDuration());
				output.setEOM(input.isEOM());
				output.setFlags(input.getFlags()); // is this correct?
				output.setHeader(null);
				output.setTimeStamp(input.getTimeStamp());
				output.setSequenceNumber(input.getSequenceNumber());
				output.setOffset(0);
				return PlugIn.BUFFER_PROCESSED_OK;
			} catch (Throwable t) {
				logger.log(Level.WARNING, "" + t, t);
				return PlugIn.BUFFER_PROCESSED_FAILED;
			}
		}
	}

	@Override
	public void reset() {
	}

	@Override
	public Format setInputFormat(Format f) {
		// we don't do anything here; just return positive feedback that we
		// can in fact handle whatever it gives us
		if (f.relax().matches(rgbFormat)) {
			return f;
		}
		return null;
	}

	@Override
	public Format setOutputFormat(Format f) {
		// we don't do anything here; just return positive feedback that we
		// can in fact handle whatever it gives us
		if (f.relax().matches(jpegFormat)) {
			return f;
		}
		return null;
	}
}