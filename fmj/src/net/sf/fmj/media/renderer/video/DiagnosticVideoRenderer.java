/*
 * Created on 12/10/2004
 */
package net.sf.fmj.media.renderer.video;

import java.util.logging.Logger;

import javax.media.Buffer;
import javax.media.Control;
import javax.media.Format;
import javax.media.Renderer;
import javax.media.ResourceUnavailableException;
import javax.media.format.RGBFormat;
import javax.media.format.YUVFormat;

import net.sf.fmj.utility.LoggerSingleton;

/**
 * This Renderer is used to log process() calls. Useful for diagnosing whether
 * the Renderer is actually being told to process buffers.
 *
 * @author Warren Bloomer
 *
 */
public class DiagnosticVideoRenderer implements Renderer {
	private static final Logger logger = LoggerSingleton.logger;

	String name = "Disgnostic Video Renderer";

	boolean started = false;

	Format[] supportedFormats = new Format[] { new RGBFormat(), new YUVFormat(), };

	int noFrames = 0;

	@Override
	public synchronized void close() {
		// Nothing to do
	}

	/**
	 * Return the control based on a control type for the PlugIn.
	 */
	@Override
	public Object getControl(String controlType) {
		try {
			Class<?> cls = Class.forName(controlType);
			Object cs[] = getControls();
			for (int i = 0; i < cs.length; i++) {
				if (cls.isInstance(cs[i])) {
					return cs[i];
				}
			}
			return null;
		} catch (Exception e) { // no such controlType or such control
			return null;
		}
	}

	/**
	 * Returns an array of supported controls
	 */
	@Override
	public Object[] getControls() {
		// No controls
		return new Control[0];
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Lists the possible input formats supported by this plug-in.
	 */
	@Override
	public Format[] getSupportedInputFormats() {
		return supportedFormats;
	}

	/*--------------- PlugIn implementation ---------------- */

	/**
	 * Opens the plugin
	 */
	@Override
	public void open() throws ResourceUnavailableException {
	}

	/**
	 * Processes the data and renders it to a component
	 */
	@Override
	public int process(Buffer buffer) {
		if ((noFrames % 10) == 0) {
			logger.fine("Received frame " + noFrames);
		}
		noFrames++;

		return BUFFER_PROCESSED_OK;
	}

	/**
	 * Resets the state of the plug-in. Typically at end of media or when media is
	 * repositioned.
	 */
	@Override
	public void reset() {
		// Nothing to do
	}

	/**
	 * Set the data input format.
	 */
	@Override
	public Format setInputFormat(Format format) {
		return format;
	}

	/*--------------- Controls implementation ------------- */

	/*************************************************************************
	 * Renderer implementation
	 *************************************************************************/
	@Override
	public void start() {
		started = true;
	}

	@Override
	public void stop() {
		started = false;
	}

}
