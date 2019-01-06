package net.sf.fmj.media;

import javax.media.ConfigureCompleteEvent;
import javax.media.NotConfiguredError;
import javax.media.Processor;
import javax.media.control.TrackControl;
import javax.media.protocol.ContentDescriptor;

import net.sf.fmj.ejmf.toolkit.media.AbstractPlayer;

/**
 * Abstract base class to implement Processor.
 *
 * @author Ken Larson
 *
 */
public abstract class AbstractProcessor extends AbstractPlayer implements Processor {
	// configure, synchronousConfigure, doConfigure modeled after realize, etc,
	// in AbstractController.

	protected ContentDescriptor outputContentDescriptor;

	@Override
	public void configure() {
		// Has this state already been reached?
		if (getState() >= Configured) {
			postConfigureCompleteEvent();
			return;
		}

		// Set the target state
		if (getTargetState() < Configured) {
			setTargetState(Configured);
		}

		// Realize on a separate thread
		Thread thread = new Thread("Processor Configure Thread") {
			@Override
			public void run() {
				if (AbstractProcessor.this.getState() < Configured) {
					synchronousConfigure();
				}
			}
		};

		getThreadQueue().addThread(thread);
	}

	public abstract boolean doConfigure();

	@Override
	public ContentDescriptor getContentDescriptor() throws NotConfiguredError {
		if (getState() < Configured)
			throw new NotConfiguredError("Cannot call getContentDescriptor on an unconfigured Processor.");

		return outputContentDescriptor;
	}

	@Override
	public ContentDescriptor[] getSupportedContentDescriptors() throws NotConfiguredError {
		if (getState() < Configured)
			throw new NotConfiguredError("Cannot call getSupportedContentDescriptors on an unconfigured Processor.");

		return null;
	}

	// public DataSource getDataOutput() throws NotRealizedError
	// {
	// if (getState() < Realized)
	// throw new
	// NotRealizedError("Cannot call getDataOutput on an unrealized Processor.");
	//
	// return null;
	// }
	@Override
	public TrackControl[] getTrackControls() throws NotConfiguredError {
		if (getState() < Configured)
			throw new NotConfiguredError("Cannot call getTrackControls on an unconfigured Processor.");

		return null;
	}

	protected void postConfigureCompleteEvent() {
		postEvent(new ConfigureCompleteEvent(this, getPreviousState(), getState(), getTargetState()));
	}

	@Override
	public ContentDescriptor setContentDescriptor(ContentDescriptor outputContentDescriptor) throws NotConfiguredError {
		// TODO: check that it matches a supported content descriptor
		this.outputContentDescriptor = outputContentDescriptor;
		return outputContentDescriptor;
	}

	protected void synchronousConfigure() {
		// Set the current state and post event
		setState(Configuring);
		postTransitionEvent();

		// Do the actual realizing
		if (doConfigure()) {
			// The realize was successful

			// Set the current state and post event
			setState(Configured);
			postConfigureCompleteEvent();

		} else {
			// The Configure was unsuccessful
			// Rely on the Controller to post the
			// ControllerErrorEvent

			// Reset the current and target states
			setState(Unrealized);
			setTargetState(Unrealized);
		}
	}

}
