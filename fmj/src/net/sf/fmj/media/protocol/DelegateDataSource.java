package net.sf.fmj.media.protocol;

import java.io.IOException;

import javax.media.Buffer;
import javax.media.Duration;
import javax.media.Format;
import javax.media.MediaLocator;
import javax.media.Time;
import javax.media.protocol.BufferTransferHandler;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PushBufferDataSource;
import javax.media.protocol.PushBufferStream;

import net.sf.fmj.media.Log;

/**
 * This special DataSource is used to prebuild a streaming player before the
 * actual streaming DataSource is not available e.g. RTP.
 */
public class DelegateDataSource extends PushBufferDataSource implements Streamable {
	class DelegateStream implements PushBufferStream, BufferTransferHandler {
		Format format;
		PushBufferStream master;
		BufferTransferHandler th;

		public DelegateStream(Format format) {
			this.format = format;
		}

		@Override
		public boolean endOfStream() {
			if (master != null)
				return master.endOfStream();
			return false;
		}

		@Override
		public ContentDescriptor getContentDescriptor() {
			if (master != null)
				return master.getContentDescriptor();
			return new ContentDescriptor(ContentDescriptor.RAW);
		}

		@Override
		public long getContentLength() {
			if (master != null)
				return master.getContentLength();
			return LENGTH_UNKNOWN;
		}

		@Override
		public Object getControl(String controlType) {
			if (master != null)
				return master.getControl(controlType);
			return null;
		}

		@Override
		public Object[] getControls() {
			if (master != null)
				return master.getControls();
			return new Object[0];
		}

		@Override
		public Format getFormat() {
			if (master != null)
				return master.getFormat();
			return format;
		}

		public PushBufferStream getMaster() {
			return master;
		}

		@Override
		public void read(Buffer buffer) throws IOException {
			if (master != null)
				master.read(buffer);
			throw new IOException("No data available");
		}

		public void setMaster(PushBufferStream master) {
			this.master = master;
			master.setTransferHandler(this);
		}

		@Override
		public void setTransferHandler(BufferTransferHandler transferHandler) {
			th = transferHandler;
		}

		@Override
		public void transferData(PushBufferStream stream) {
			if (th != null)
				th.transferData(stream);
		}
	}

	protected String contentType = ContentDescriptor.RAW;
	protected PushBufferDataSource master;

	protected DelegateStream streams[];
	protected boolean started = false;

	protected boolean connected = false;

	public DelegateDataSource(Format format[]) {
		streams = new DelegateStream[format.length];
		for (int i = 0; i < format.length; i++) {
			streams[i] = new DelegateStream(format[i]);
		}
		try {
			connect();
		} catch (IOException e) {
		}
	}

	@Override
	public void connect() throws IOException {
		if (connected)
			return;
		if (master != null)
			master.connect();
		connected = true;
	}

	@Override
	public void disconnect() {
		try {
			if (started)
				stop();
		} catch (IOException e) {
		}
		if (master != null)
			master.disconnect();
		connected = false;
	}

	@Override
	public String getContentType() {
		if (!connected) {
			System.err.println("Error: DataSource not connected");
			return null;
		}
		return contentType;
	}

	@Override
	public Object getControl(String controlType) {
		if (master != null)
			return master.getControl(controlType);
		return null;
	}

	@Override
	public Object[] getControls() {
		if (master != null)
			return master.getControls();
		return new Object[0];
	}

	@Override
	public Time getDuration() {
		if (master != null)
			return master.getDuration();
		return Duration.DURATION_UNKNOWN;
	}

	@Override
	public MediaLocator getLocator() {
		if (master != null)
			return master.getLocator();
		return null;
	}

	public javax.media.protocol.DataSource getMaster() {
		return master;
	}

	@Override
	public PushBufferStream[] getStreams() {
		return streams;
	}

	@Override
	public boolean isPrefetchable() {
		return false;
	}

	public void setMaster(PushBufferDataSource ds) throws IOException {
		master = ds;

		PushBufferStream mstrms[] = ds.getStreams();
		for (int i = 0; i < mstrms.length; i++) {
			for (int j = 0; j < streams.length; j++) {
				if (streams[j].getFormat().matches(mstrms[i].getFormat()))
					streams[j].setMaster(mstrms[i]);
			}
		}

		for (int i = 0; i < mstrms.length; i++) {
			if (streams[i].getMaster() == null) {
				Log.error("DelegateDataSource: cannot not find a matching track from the master with this format: "
						+ streams[i].getFormat());
			}
		}

		if (connected)
			master.connect();
		if (started)
			master.start();
	}

	@Override
	public void start() throws IOException {
		// we need to throw error if connect() has not been called
		if (!connected)
			throw new java.lang.Error("DataSource must be connected before it can be started");
		if (started)
			return;
		if (master != null)
			master.start();
		started = true;
	}

	// ///////////////////
	//
	// INNER CLASSES
	// ///////////////////
	@Override
	public void stop() throws IOException {
		if ((!connected) || (!started))
			return;
		if (master != null)
			master.stop();
		started = false;
	}
}
