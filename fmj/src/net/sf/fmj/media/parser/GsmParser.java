package net.sf.fmj.media.parser;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.BadHeaderException;
import javax.media.Buffer;
import javax.media.Duration;
import javax.media.Format;
import javax.media.IncompatibleSourceException;
import javax.media.Time;
import javax.media.Track;
import javax.media.format.AudioFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.FileTypeDescriptor;
import javax.media.protocol.PullDataSource;
import javax.media.protocol.PullSourceStream;

import net.sf.fmj.media.AbstractDemultiplexer;
import net.sf.fmj.media.AbstractTrack;
import net.sf.fmj.utility.LoggerSingleton;

/**
 * Parser for GSM media files.
 *
 * @author Martin Harvan
 */
public class GsmParser extends AbstractDemultiplexer {
	private class PullSourceStreamTrack extends AbstractTrack {
		private PullSourceStream stream;
		private long frameLength;
		private static final int GSM_FRAME_SIZE = 33;

		public PullSourceStreamTrack(PullSourceStream stream) {
			super();
			this.stream = stream;
			frameLength = stream.getContentLength() / GSM_FRAME_SIZE;
		}

		@Override
		public Time getDuration() {
			final long lengthInFrames = frameLength;
			if (lengthInFrames < 0) {
				logger.fine("PullSourceStreamTrack: returning Duration.DURATION_UNKNOWN (1)");
				return Duration.DURATION_UNKNOWN;
			}
			final double lengthInSeconds = lengthInFrames / GSM_FRAME_RATE;
			if (lengthInSeconds < 0.0) {
				logger.fine("PullSourceStreamTrack: returning Duration.DURATION_UNKNOWN (2)");
				return Duration.DURATION_UNKNOWN;
			}
			final double lengthInNanos = secondsToNanos(lengthInSeconds);
			logger.fine("PullSourceStreamTrack: returning " + ((long) lengthInNanos));

			return new Time((long) lengthInNanos);
		}

		@Override
		public Format getFormat() {
			return new AudioFormat(AudioFormat.GSM, 8000, 8, 1, -1, AudioFormat.SIGNED, 264, -1, Format.byteArray);
		}

		@Override
		public void readFrame(Buffer buffer) {
			final int BUFFER_SIZE = 16500; // it's best to read in multiples of
											// 33's
			if (buffer.getData() == null)
				buffer.setData(new byte[BUFFER_SIZE]);
			byte[] bytes = (byte[]) buffer.getData();

			try {
				int result = stream.read(bytes, 0, bytes.length);
				if (result < 0) {
					buffer.setEOM(true);
					buffer.setLength(0);
					return;
				}
				buffer.setLength(result);
				buffer.setOffset(0);
			} catch (IOException e) {
				buffer.setEOM(true);
				buffer.setDiscard(true);
				buffer.setLength(0);
				logger.log(Level.WARNING, "" + e, e);
			}
		}
	}

	private static double GSM_FRAME_RATE = 50; // Frame rate of Gsm media
												// according to GSM specs is kbps
												// stream (260 bits every 20 ms)
												// that makes frame rate 50

	private static final Logger logger = LoggerSingleton.logger;

	private static final double secondsToNanos(double secs) {
		return secs * 1000000000.0;
	}

	private ContentDescriptor[] supportedInputContentDescriptors = new ContentDescriptor[] {
			new ContentDescriptor(FileTypeDescriptor.GSM) };

	private PullDataSource source;

	private PullSourceStreamTrack[] tracks;

	@Override
	public ContentDescriptor[] getSupportedInputContentDescriptors() {
		return supportedInputContentDescriptors;
	}

	@Override
	public Track[] getTracks() throws IOException, BadHeaderException {
		return tracks;
	}

	@Override
	public boolean isPositionable() {
		return true;
	}

	// @Override
	@Override
	public boolean isRandomAccess() {
		return super.isRandomAccess();
	}

	@Override
	public Time setPosition(Time where, int rounding) {
		// TODO Make work
		return null;
	}

	@Override
	public void setSource(DataSource source) throws IOException, IncompatibleSourceException {
		if (!(source instanceof PullDataSource))
			throw new IncompatibleSourceException();

		this.source = (PullDataSource) source;

	}

	@Override
	public void start() throws IOException {
		source.start();
		final PullSourceStream[] streamsForFormat = source.getStreams();

		tracks = new PullSourceStreamTrack[streamsForFormat.length];
		for (int i = 0; i < streamsForFormat.length; ++i) {
			tracks[i] = new PullSourceStreamTrack(streamsForFormat[i]);
		}
	}

	@Override
	public void stop() {
		try {
			source.stop();
		} catch (IOException e) {
			logger.log(Level.WARNING, "" + e, e);
		}
	}

}