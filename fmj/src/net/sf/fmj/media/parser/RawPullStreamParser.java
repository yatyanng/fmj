package net.sf.fmj.media.parser;

import java.io.IOException;

import javax.media.Buffer;
import javax.media.Demultiplexer;
import javax.media.Format;
import javax.media.IncompatibleSourceException;
import javax.media.Time;
import javax.media.Track;
import javax.media.TrackListener;
import javax.media.protocol.DataSource;
import javax.media.protocol.PullDataSource;
import javax.media.protocol.PullSourceStream;
import javax.media.protocol.SourceStream;

public class RawPullStreamParser extends RawParser {
	class FrameTrack implements Track {
		Demultiplexer parser;
		PullSourceStream pss;
		boolean enabled = true;
		Format format = null;
		TrackListener listener;
		Integer stateReq = new Integer(0);

		public FrameTrack(Demultiplexer parser, PullSourceStream pss) {
			this.pss = pss;
		}

		@Override
		public Time getDuration() {
			return parser.getDuration();
		}

		@Override
		public Format getFormat() {
			return format;
		}

		@Override
		public Time getStartTime() {
			return new Time(0);
		}

		@Override
		public boolean isEnabled() {
			return enabled;
		}

		@Override
		public Time mapFrameToTime(int frameNumber) {
			return new Time(0);
		}

		@Override
		public int mapTimeToFrame(Time t) {
			return -1;
		}

		@Override
		public void readFrame(Buffer buffer) {
			byte data[] = (byte[]) buffer.getData();

			// If the buffer is empty, just allocate some random number.
			if (data == null) {
				data = new byte[500];
				buffer.setData(data);
			}

			try {
				int len = pss.read(data, 0, data.length);
				buffer.setLength(len);
			} catch (IOException e) {
				buffer.setDiscard(true);
			}
		}

		@Override
		public void setEnabled(boolean t) {
			enabled = t;
		}

		@Override
		public void setTrackListener(TrackListener l) {
			listener = l;
		}

	}

	protected SourceStream[] streams;

	protected Track[] tracks = null;

	static final String NAME = "Raw pull stream parser";

	public RawPullStreamParser() {
	}

	/**
	 * Closes the plug-in component and releases resources. No more data will be
	 * accepted by the plug-in after a call to this method. The plug-in can be
	 * reinstated after being closed by calling <tt>open</tt>.
	 */
	@Override
	public void close() {
		if (source != null) {
			try {
				source.stop();
				source.disconnect();
			} catch (IOException e) {
				// Internal error?
			}
			source = null;
		}
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Track[] getTracks() {
		return tracks;
	}

	/**
	 * Opens the plug-in software or hardware component and acquires necessary
	 * resources. If all the needed resources could not be acquired, it throws a
	 * ResourceUnavailableException. Data should not be passed into the plug-in
	 * without first calling this method.
	 */
	@Override
	public void open() {
		if (tracks != null)
			return;
		tracks = new Track[streams.length];
		for (int i = 0; i < streams.length; i++) {
			tracks[i] = new FrameTrack(this, (PullSourceStream) streams[i]);
		}
	}

	@Override
	public void setSource(DataSource source) throws IOException, IncompatibleSourceException {
		SourceStream[] streams;

		if (!(source instanceof PullDataSource)) {
			throw new IncompatibleSourceException("DataSource not supported: " + source);
		} else {
			streams = ((PullDataSource) source).getStreams();
		}

		if (streams == null) {
			throw new IOException("Got a null stream from the DataSource");
		}

		if (streams.length == 0) {
			throw new IOException("Got a empty stream array from the DataSource");
		}

		if (!supports(streams)) {
			throw new IncompatibleSourceException("DataSource not supported: " + source);
		}

		this.source = source;
		this.streams = streams;
	}

	/**
	 * Start the parser.
	 */
	@Override
	public void start() throws IOException {
		source.start();
	}

	/**
	 * Stop the parser.
	 */
	@Override
	public void stop() {
		try {
			source.stop();
		} catch (IOException e) {
			// Internal errors?
		}
	}

	// //////////////////////
	//
	// Inner class
	// //////////////////////

	/**
	 * Override this if the Parser has additional requirements from the
	 * PullSourceStream
	 */
	protected boolean supports(SourceStream[] streams) {
		return ((streams[0] != null) && (streams[0] instanceof PullSourceStream));
	}
}
