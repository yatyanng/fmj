package javax.media.rtp.event;

import javax.media.rtp.Participant;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.SessionManager;

/**
 * Standard JMF class -- see <a href=
 * "http://java.sun.com/products/java-media/jmf/2.1.1/apidocs/javax/media/rtp/event/InactiveReceiveStreamEvent.html"
 * target="_blank">this class in the JMF Javadoc</a>. Complete.
 *
 * @author Ken Larson
 *
 */
public class InactiveReceiveStreamEvent extends ReceiveStreamEvent {
	private boolean laststream;

	public InactiveReceiveStreamEvent(SessionManager from, Participant participant, ReceiveStream recvStream,
			boolean laststream) {
		super(from, recvStream, participant);
		this.laststream = laststream;
	}

	public boolean isLastStream() {
		return laststream;
	}
}
