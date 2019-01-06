package net.sf.fmj.media;

import javax.media.PlugIn;
import javax.media.ResourceUnavailableException;

/**
 * Abstract implementation of PlugIn, useful for subclassing.
 *
 * @author Ken Larson
 *
 */
public abstract class AbstractPlugIn extends AbstractControls implements PlugIn {
	@Override
	public void close() {
	}

	@Override
	public String getName() {
		return getClass().getSimpleName(); // override to provide a better name
	}

	@Override
	public void open() throws ResourceUnavailableException {
	}

	@Override
	public void reset() { // TODO
	}
}
