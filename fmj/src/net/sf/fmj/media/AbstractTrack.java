package net.sf.fmj.media;

import javax.media.Buffer;
import javax.media.Duration;
import javax.media.Format;
import javax.media.Time;
import javax.media.Track;
import javax.media.TrackListener;

/**
 * Abstract base class to implement Track.
 *
 * @author Ken Larson
 *
 */
public abstract class AbstractTrack implements Track {
	private boolean enabled = true; // default to enabled. JMF won't play the
									// track if it is not enabled. TODO: FMJ
									// should do the same.

	@Override
	public Time getDuration() {
		return Duration.DURATION_UNKNOWN;
	}

	@Override
	public abstract Format getFormat();

	@Override
	public Time getStartTime() {
		return TIME_UNKNOWN;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public Time mapFrameToTime(int frameNumber) {
		return TIME_UNKNOWN;
	}

	@Override
	public int mapTimeToFrame(Time t) {
		return FRAME_UNKNOWN;
	}

	@Override
	public abstract void readFrame(Buffer buffer);

	@Override
	public void setEnabled(boolean t) {
		this.enabled = t;
	}

	@Override
	public void setTrackListener(TrackListener listener) {
		// TODO Auto-generated method stub
	}

}
