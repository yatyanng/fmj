package net.sf.fmj.media.multiplexer;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PushSourceStream;
import javax.media.protocol.SourceTransferHandler;

import net.sf.fmj.utility.LoggerSingleton;

/**
 * Adapater from {@link InputStream} to {@link PushSourceStream}.
 *
 * @author Ken Larson
 *
 */
public class InputStreamPushSourceStream implements PushSourceStream {
	private static final Logger logger = LoggerSingleton.logger;

	private final ContentDescriptor outputContentDescriptor;
	private final InputStream is;

	private boolean eos;

	private SourceTransferHandler transferHandler;

	public InputStreamPushSourceStream(ContentDescriptor outputContentDescriptor, final InputStream is) {
		super();
		this.outputContentDescriptor = outputContentDescriptor;
		this.is = is;
	}

	@Override
	public boolean endOfStream() {
		logger.finer(getClass().getSimpleName() + " endOfStream");
		return eos;
	}

	@Override
	public ContentDescriptor getContentDescriptor() {
		logger.finer(getClass().getSimpleName() + " getContentDescriptor");
		return outputContentDescriptor;
	}

	@Override
	public long getContentLength() {
		logger.finer(getClass().getSimpleName() + " getContentLength");
		return 0; // TODO
	}

	@Override
	public Object getControl(String controlType) {
		logger.finer(getClass().getSimpleName() + " getControl");
		return null;
	}

	@Override
	public Object[] getControls() {
		logger.finer(getClass().getSimpleName() + " getControls");
		return new Object[0];
	}

	@Override
	public int getMinimumTransferSize() {
		logger.finer(getClass().getSimpleName() + " getMinimumTransferSize");
		return 0;
	}

	/**
	 * Not a JMF API method, but allows us to get the transfer handler to do a
	 * similar hack to JMF: how to go back and update a header for a file you've
	 * already written.
	 */
	public SourceTransferHandler getTransferHandler() {
		return transferHandler;
	}

	public void notifyDataAvailable() {
		if (transferHandler != null) // TODO; synchronization issues on
										// transferHandler
			transferHandler.transferData(this);
	}

	@Override
	public int read(byte[] buffer, int offset, int length) throws IOException {
		// logger.finer(getClass().getSimpleName() + " read");
		int result = is.read(buffer, offset, length);
		if (result < 0)
			eos = true;
		return result;
	}

	@Override
	public void setTransferHandler(SourceTransferHandler transferHandler) {
		logger.finer(getClass().getSimpleName() + " setTransferHandler");
		this.transferHandler = transferHandler;
	}
}