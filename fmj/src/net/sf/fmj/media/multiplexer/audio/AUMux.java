package net.sf.fmj.media.multiplexer.audio;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.Buffer;
import javax.media.Format;
import javax.media.ResourceUnavailableException;
import javax.media.format.AudioFormat;
import javax.media.protocol.DataSource;
import javax.media.protocol.FileTypeDescriptor;
import javax.media.protocol.PushSourceStream;
import javax.media.protocol.Seekable;

import net.sf.fmj.media.codec.JavaSoundCodec;
import net.sf.fmj.media.multiplexer.AbstractInputStreamMux;
import net.sf.fmj.media.multiplexer.InputStreamPushDataSource;
import net.sf.fmj.media.multiplexer.InputStreamPushSourceStream;
import net.sf.fmj.media.renderer.audio.JavaSoundUtils;
import net.sf.fmj.utility.LoggerSingleton;

/**
 * AU Mux that doesn't use javasound.
 *
 * @author Ken Larson
 *
 */
public class AUMux extends AbstractInputStreamMux {
	private static final Logger logger = LoggerSingleton.logger;

	private boolean headerWritten = false;

	private boolean trailerWritten = false;

	// when stream is closed, data length is written to the header ( otherwise
	// some programs will not play the au file )
	private long bytesWritten;

	public AUMux() {
		super(new FileTypeDescriptor(FileTypeDescriptor.BASIC_AUDIO));
	}

	@Override
	public void close() {
		if (!trailerWritten) {
			try {
				outputTrailer(getOutputStream());
			} catch (IOException e) {
				logger.log(Level.WARNING, "" + e, e);
				throw new RuntimeException(e);
			}
			trailerWritten = true;
		}

		super.close();
	}

	@Override
	protected void doProcess(Buffer buffer, int trackID, OutputStream os) throws IOException {
		if (!headerWritten) {
			outputHeader(os);
			headerWritten = true;
		}
		if (buffer.isEOM()) {
			if (!trailerWritten) {
				outputTrailer(os);
				trailerWritten = true;
			}
			os.close();
			return; // TODO: what if there is data in buffer?
		}

		if (buffer.isDiscard())
			return;

		os.write((byte[]) buffer.getData(), buffer.getOffset(), buffer.getLength());
		bytesWritten += buffer.getLength();
	}

	@Override
	public Format[] getSupportedInputFormats() {
		return new Format[] { new AudioFormat(AudioFormat.LINEAR, -1, 8, -1, -1, AudioFormat.SIGNED),
				new AudioFormat(AudioFormat.LINEAR, -1, 16, -1, AudioFormat.BIG_ENDIAN, AudioFormat.SIGNED),
				new AudioFormat(AudioFormat.LINEAR, -1, 24, -1, AudioFormat.BIG_ENDIAN, AudioFormat.SIGNED),
				new AudioFormat(AudioFormat.LINEAR, -1, 32, -1, AudioFormat.BIG_ENDIAN, AudioFormat.SIGNED),
				new AudioFormat(AudioFormat.ULAW), // TODO: narrow down
				new AudioFormat(AudioFormat.ALAW) // TODO: narrow down
		};
	}

	@Override
	public void open() throws ResourceUnavailableException {
		super.open();

		if (!headerWritten) {
			try {
				outputHeader(getOutputStream());
			} catch (IOException e) {
				logger.log(Level.WARNING, "" + e, e);
				throw new ResourceUnavailableException("" + e);
			}
			headerWritten = true;
		}
	}

	private void outputHeader(OutputStream os) throws IOException {
		byte[] header = JavaSoundCodec.createAuHeader(JavaSoundUtils.convertFormat((AudioFormat) inputFormats[0])); // TODO:
																													// no
																													// length
		if (header == null)
			throw new IOException("Unable to create AU header");

		os.write(header);
	}

	private void outputTrailer(OutputStream os) throws IOException {
		// mgodehardt: updating data length field, some java versions cannot
		// handle length of -1 in au file
		// ( microsoft mediaplayer is not playing au files with length -1 )

		// should we throw a IOException if length could not be written ?
		DataSource ds = getDataOutput();
		if (ds instanceof InputStreamPushDataSource) {
			InputStreamPushDataSource pds = (InputStreamPushDataSource) ds;
			PushSourceStream pss = pds.getStreams()[0];

			if (pss instanceof InputStreamPushSourceStream) {
				InputStreamPushSourceStream ispss = (InputStreamPushSourceStream) pss;
				if (((Seekable) ispss.getTransferHandler()) instanceof Seekable) {
					((Seekable) ispss.getTransferHandler()).seek(8);

					writeInt(os, bytesWritten);

					if (getDataOutputNoInit() != null) {
						getDataOutputNoInit().notifyDataAvailable(0); // only 1
																		// track
					}
				}
			}
		}
	}

	@Override
	public Format setInputFormat(Format format, int trackID) {
		logger.finer("setInputFormat " + format + " " + trackID);

		boolean match = false;
		for (Format supported : getSupportedInputFormats()) {
			if (format.matches(supported)) {
				match = true;
				break;
			}
		}
		if (!match) {
			logger.warning("Input format does not match any supported input format: " + format);
			return null;
		}
		if (inputFormats != null) // TODO: should we save this somewhere and
									// apply once inputFormats is not null?
			inputFormats[trackID] = format;

		return format;
	}
}
