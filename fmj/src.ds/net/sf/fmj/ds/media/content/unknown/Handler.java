package net.sf.fmj.ds.media.content.unknown;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.IncompatibleSourceException;
import javax.media.Time;
import javax.media.protocol.DataSource;

import net.sf.fmj.ejmf.toolkit.media.AbstractPlayer;
import net.sf.fmj.gui.controlpanelfactory.ControlPanelFactorySingleton;
import net.sf.fmj.utility.LoggerSingleton;
import net.sf.fmj.utility.URLUtils;
import net.sf.jdshow.Com;
import net.sf.jdshow.ComException;
import net.sf.jdshow.IGraphBuilder;
import net.sf.jdshow.IMediaControl;
import net.sf.jdshow.IMediaSeeking;
import net.sf.jdshow.IVideoWindow;
import net.sf.jdshow.JAWTUtils;
import net.sf.jdshow.WindowedRendering;

/**
 *
 * @author Ken Larson
 *
 */
public class Handler extends AbstractPlayer {
	private class MyCanvas extends Canvas {
		// @Override
		@Override
		public Dimension getPreferredSize() {
			if (videoSize == null)
				return new Dimension(0, 0);
			return videoSize;
			// return new Dimension(400, 300); // TODO
		}

	}

	private class MyComponentListener implements ComponentListener {
		@Override
		public void componentHidden(ComponentEvent e) {
			logger.fine("componentHidden");
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			int x = ComponentEvent.COMPONENT_MOVED;
			logger.fine("componentMoved: " + e.getID());
			bindVisualComponent();

		}

		@Override
		public void componentResized(ComponentEvent e) {
			logger.fine("componentResized");
			bindVisualComponent();
		}

		@Override
		public void componentShown(ComponentEvent e) {
			logger.fine("componentShown");

		}

	}

	private static final Logger logger = LoggerSingleton.logger;

	private boolean prefetchNeeded = true;
	private static final boolean TRACE = true;
	private IGraphBuilder graphBuilder;

	private IMediaControl mediaControl;

	private IMediaSeeking mediaSeeking;

	private MyCanvas visualComponent;

	private boolean visualComponentBound = false;

	private Dimension videoSize;

	private boolean bindVisualComponent() {
		if (visualComponentBound)
			return false;
		final long hwnd = JAWTUtils.getWindowHandle(visualComponent);
		// logger.fine("HWND: " + hwnd);

		try {
			final int hr = WindowedRendering.InitWindowedRendering2(hwnd, graphBuilder);
			if (Com.FAILED(hr))
				throw new ComException(hr);
		} catch (ComException e) {
			logger.log(Level.WARNING, "" + e, e);
			return false;
		}
		visualComponentBound = true;
		return true;

	}

	// @Override
	@Override
	public void doPlayerClose() {
		// TODO
		logger.info("Handler.doPlayerClose");
	}

	// @Override
	@Override
	public boolean doPlayerDeallocate() {
		logger.info("Handler.doPlayerDeallocate");

		mediaControl.Release();
		graphBuilder.Release();
		mediaSeeking.Release();

		Com.CoUninitialize();

		return true;
	}

	// @Override
	@Override
	public boolean doPlayerPrefetch() {
		if (!prefetchNeeded)
			return true;

		prefetchNeeded = false;

		return true;
	}

	// @Override
	@Override
	public boolean doPlayerRealize() {
		return true;
	}

	// @Override
	@Override
	public void doPlayerSetMediaTime(Time t) {
		logger.info("Handler.doPlayerSetMediaTime" + t);
		try {
			long[] current = new long[1];
			long[] stop = new long[1];

			int hr = mediaSeeking.GetPositions(current, stop);
			if (Com.FAILED(hr))
				throw new ComException(hr);

			current[0] = t.getNanoseconds() / 100; // TODO: this assumes
													// REFERENCE_TIME format is
													// being used.

			hr = mediaSeeking.SetPositions(current, IMediaSeeking.AM_SEEKING_AbsolutePositioning, stop,
					IMediaSeeking.AM_SEEKING_NoPositioning);
			if (Com.FAILED(hr))
				throw new ComException(hr);

		} catch (ComException e) {
			logger.log(Level.WARNING, "" + e, e);
			// TODO: handle
		}

	}

	// @Override
	@Override
	public float doPlayerSetRate(float rate) {
		logger.fine("Handler.doPlayerSetRate " + rate);

		try {
			int hr = mediaSeeking.SetRate(rate);
			if (Com.FAILED(hr))
				throw new ComException(hr);
		} catch (ComException e) {
			logger.log(Level.WARNING, "" + e, e);
			return getRate(); // TODO: what to return?
		}

		return rate;
	}

	// @Override
	@Override
	public boolean doPlayerStop() {
		logger.info("Handler.doPlayerStop");

		mediaControl.Stop();

		return true;
	}

	// @Override
	@Override
	public boolean doPlayerSyncStart(Time t) {
		logger.info("Handler.doPlayerSyncStart" + t);

		try {
			int hr = mediaControl.Run();
			if (Com.FAILED(hr))
				throw new ComException(hr);
		} catch (ComException e) {
			logger.log(Level.WARNING, "" + e, e);
			return false;
		}
		return true;
	}

	@Override
	public Component getControlPanelComponent() {
		Component c = super.getControlPanelComponent();

		if (c == null) {
			c = ControlPanelFactorySingleton.getInstance().getControlPanelComponent(this);
			setControlPanelComponent(c);
		}

		return c;
	}

	// @Override
	@Override
	public synchronized Time getMediaTime() {
		if (mediaSeeking != null) {
			try {
				long[] current = new long[1];

				int hr = mediaSeeking.GetCurrentPosition(current);
				if (Com.FAILED(hr))
					throw new ComException(hr);

				return new Time(current[0] * 100); // TODO: this assumes
													// REFERENCE_TIME format is
													// being used.

			} catch (ComException e) {
				logger.log(Level.WARNING, "" + e, e);
				return DURATION_UNKNOWN;
			}
		}
		return super.getMediaTime();
	}

	// @Override
	@Override
	public Time getPlayerDuration() {
		if (getState() < Realized) {
			return DURATION_UNKNOWN;
		} else if (mediaSeeking != null) {
			try {
				long[] duration = new long[1];

				int hr = mediaSeeking.GetDuration(duration);
				if (Com.FAILED(hr))
					throw new ComException(hr);

				return new Time(duration[0] * 100); // TODO: this assumes
													// REFERENCE_TIME format is
													// being used.

			} catch (ComException e) {
				logger.log(Level.WARNING, "" + e, e);
				return DURATION_UNKNOWN;
			}
		} else {
			return DURATION_UNKNOWN;
		}
	}

	// @Override
	@Override
	public Time getPlayerStartLatency() {
		return new Time(0);
	}

	// @Override
	@Override
	public Component getVisualComponent() {
		return visualComponent;
	}

	@Override
	public void setSource(DataSource source) throws IncompatibleSourceException {
		if (TRACE)
			logger.fine("DataSource: " + source);

		if (!source.getLocator().getProtocol().equals("file"))
			throw new IncompatibleSourceException("Only file URLs supported: " + source);

		// TODO: only handling file URLs right now.
		String path = URLUtils.extractValidPathFromFileUrl(source.getLocator().toExternalForm());
		if (path == null)
			throw new IncompatibleSourceException(
					"Unable to extract valid file path from URL: " + source.getLocator().toExternalForm());

		try {
			// because Java thinks that /C: is a valid way to start a path on
			// Windows, we have to turn it
			// into something more normal.
			path = new File(path).getCanonicalPath();
		} catch (IOException e1) {
			final String msg = "Unable to get canonical path from " + path + ": " + e1;
			logger.log(Level.WARNING, msg, e1);
			throw new IncompatibleSourceException(msg);
		}

		logger.info("Path: " + path);

		try {
			System.loadLibrary("jdshow");
		} catch (Throwable e) {
			logger.log(Level.WARNING, "" + e, e);
			throw new IncompatibleSourceException();
		}

		// TODO: check if media has visual
		visualComponent = new MyCanvas();

		visualComponent.addComponentListener(new MyComponentListener());

		try {
			Com.CoInitialize();

			int hr;

			long[] p = new long[1];
			hr = Com.CoCreateInstance(Com.CLSID_FilterGraph, 0L, Com.CLSCTX_ALL, Com.IID_IGraphBuilder, p);
			if (Com.FAILED(hr))
				throw new ComException(hr);

			graphBuilder = new IGraphBuilder(p[0]);

			hr = graphBuilder.RenderFile(path, "");
			if (Com.FAILED(hr))
				throw new ComException(hr);

			hr = graphBuilder.QueryInterface(Com.IID_IMediaControl, p);
			if (Com.FAILED(hr))
				throw new ComException(hr);
			mediaControl = new IMediaControl(p[0]);

			hr = graphBuilder.QueryInterface(Com.IID_IMediaSeeking, p);
			if (Com.FAILED(hr))
				throw new ComException(hr);
			mediaSeeking = new IMediaSeeking(p[0]);

			hr = graphBuilder.QueryInterface(Com.IID_IVideoWindow, p);
			if (Com.FAILED(hr))
				throw new ComException(hr);

			// determine video size:
			final IVideoWindow videoWindow = new IVideoWindow(p[0]);

			{
				long[] width = new long[1];
				hr = videoWindow.get_Width(width);
				if (Com.FAILED(hr))
					throw new ComException(hr);
				// logger.fine("width: " + width[0]);

				long[] height = new long[1];
				hr = videoWindow.get_Height(height);
				if (Com.FAILED(hr))
					throw new ComException(hr);
				// logger.fine("height: " + height[0]);

				videoSize = new Dimension((int) width[0], (int) height[0]);
			}

			videoWindow.Release();

		} catch (ComException e) {
			logger.log(Level.WARNING, "" + e, e);
			throw new IncompatibleSourceException(e.getMessage());
		}

		super.setSource(source);

	}

}
