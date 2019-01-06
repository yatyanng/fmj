package net.sf.fmj.media.multiplexer;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.Buffer;
import javax.media.Format;
import javax.media.ResourceUnavailableException;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;

import net.sf.fmj.media.AbstractMultiplexer;
import net.sf.fmj.media.BufferQueueInputStream;
import net.sf.fmj.utility.LoggerSingleton;

/**
 * Mux that can be implemented simply by copying streams. Override
 * createInputStreamPushDataSource and create an overridden version of
 * StreamCopyPushDataSource overriding write.
 *
 * @author Ken Larson
 *
 */
public abstract class AbstractStreamCopyMux extends AbstractMultiplexer {
	private static final Logger logger = LoggerSingleton.logger;

	private BufferQueueInputStream[] bufferQueueInputStreams;
	private StreamCopyPushDataSource dataOutput;

	private final ContentDescriptor contentDescriptor;

	// TODO: deal with n tracks properly

	public AbstractStreamCopyMux(final ContentDescriptor contentDescriptor) {
		super();
		this.contentDescriptor = contentDescriptor;

	}

	@Override
	public void close() {
		logger.finer(getClass().getSimpleName() + " close");
		super.close();

		if (dataOutput != null) {
			try {
				dataOutput.stop();
			} catch (IOException e) {
				logger.log(Level.WARNING, "" + e, e);
			}
			dataOutput.disconnect();
		}
	}

	protected StreamCopyPushDataSource createInputStreamPushDataSource(ContentDescriptor outputContentDescriptor,
			int numTracks, InputStream[] inputStreams, Format[] inputFormats) {
		return new StreamCopyPushDataSource(outputContentDescriptor, numTracks, inputStreams, inputFormats);
	}

	@Override
	public DataSource getDataOutput() {
		if (dataOutput == null)
			dataOutput = createInputStreamPushDataSource(outputContentDescriptor, numTracks, bufferQueueInputStreams,
					inputFormats);
		logger.finer(getClass().getSimpleName() + " getDataOutput");
		return dataOutput;
	}

	@Override
	public abstract Format[] getSupportedInputFormats();

	@Override
	public ContentDescriptor[] getSupportedOutputContentDescriptors(Format[] inputs) {
		// TODO: should this match the # of entries in inputs?
		return new ContentDescriptor[] { contentDescriptor };
	}

	@Override
	public void open() throws ResourceUnavailableException {
		logger.finer(getClass().getSimpleName() + " open");
		super.open();
	}

	@Override
	public int process(Buffer buffer, int trackID) {
		logger.finer(
				getClass().getSimpleName() + " process " + buffer + " " + trackID + " length " + buffer.getLength());
		// need a PushDataSource, with a PushSourceStream that reads from out

		if (buffer.isEOM())
			logger.finer("processing EOM buffer for track: " + trackID);

		if (!bufferQueueInputStreams[trackID].put(buffer))
			return INPUT_BUFFER_NOT_CONSUMED;

		try {
			if (buffer.isEOM()) {
				logger.fine("EOM, waitUntilFinished...");
				if (dataOutput != null)
					dataOutput.waitUntilFinished();
				// wait until done processing
				logger.fine("EOM, finished.");
			}

			if (dataOutput != null)
				dataOutput.notifyDataAvailable(trackID);

			return BUFFER_PROCESSED_OK;

		} catch (InterruptedException e) {
			logger.log(Level.WARNING, "" + e, e);
			return BUFFER_PROCESSED_FAILED;
		}
	}

	@Override
	public int setNumTracks(int numTracks) {
		numTracks = super.setNumTracks(numTracks);

		bufferQueueInputStreams = new BufferQueueInputStream[numTracks];
		for (int track = 0; track < numTracks; ++track) {
			bufferQueueInputStreams[track] = new BufferQueueInputStream();
		}
		return numTracks;
	}

}
