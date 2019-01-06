package net.sf.fmj.media.datasink;

import java.util.Vector;

import javax.media.datasink.DataSinkErrorEvent;
import javax.media.datasink.DataSinkEvent;
import javax.media.datasink.DataSinkListener;
import javax.media.datasink.EndOfStreamEvent;

public abstract class BasicDataSink implements javax.media.DataSink {
	protected final Vector<DataSinkListener> listeners = new Vector<DataSinkListener>(1);

	@Override
	public void addDataSinkListener(DataSinkListener dsl) {
		if ((dsl != null) && !listeners.contains(dsl))
			listeners.addElement(dsl);
	}

	protected void removeAllListeners() {
		listeners.removeAllElements();
	}

	@Override
	public void removeDataSinkListener(DataSinkListener dsl) {
		if (dsl != null)
			listeners.removeElement(dsl);
	}

	protected final void sendDataSinkErrorEvent(String reason) {
		sendEvent(new DataSinkErrorEvent(this, reason));
	}

	protected final void sendEndofStreamEvent() {
		sendEvent(new EndOfStreamEvent(this));
	}

	protected void sendEvent(DataSinkEvent event) {
		if (!listeners.isEmpty()) {
			synchronized (listeners) {
				for (DataSinkListener listener : listeners)
					listener.dataSinkUpdate(event);
			}
		}
	}
}
