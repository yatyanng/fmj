package net.sf.fmj.media.parser;

import javax.media.Demultiplexer;
import javax.media.Duration;
import javax.media.Time;
import javax.media.Track;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.Positionable;

import net.sf.fmj.media.BasicPlugIn;

public abstract class RawParser extends BasicPlugIn implements Demultiplexer {
	static final String NAME = "Raw parser";

	protected DataSource source;
	ContentDescriptor supported[];

	public RawParser() {
		supported = new ContentDescriptor[] { new ContentDescriptor(ContentDescriptor.RAW) };
	}

	@Override
	public Object[] getControls() {
		return source.getControls();
	}

	@Override
	public Time getDuration() {
		return (source == null ? Duration.DURATION_UNKNOWN : source.getDuration());
	}

	@Override
	public Time getMediaTime() {
		return Time.TIME_UNKNOWN;
	}

	@Override
	public String getName() {
		return NAME;
	}

	/**
	 * Lists the possible input formats supported by this plug-in.
	 */
	@Override
	public ContentDescriptor[] getSupportedInputContentDescriptors() {
		return supported;
	}

	@Override
	public Track[] getTracks() {
		return null;
	}

	@Override
	public boolean isPositionable() {
		return source instanceof Positionable;
	}

	@Override
	public boolean isRandomAccess() {
		return source instanceof Positionable && ((Positionable) source).isRandomAccess();
	}

	/**
	 * Resets the state of the plug-in. Typically at end of media or when media is
	 * repositioned.
	 */
	@Override
	public void reset() {
	}

	@Override
	public Time setPosition(Time when, int round) {
		if (source instanceof Positionable)
			return ((Positionable) source).setPosition(when, round);
		return when;
	}

}
