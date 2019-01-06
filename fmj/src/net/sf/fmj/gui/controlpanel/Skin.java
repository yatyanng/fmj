package net.sf.fmj.gui.controlpanel;

import javax.swing.ImageIcon;

/**
 * Provides icons to {@link SwingLookControlPanel}.
 *
 * @author Ken Larson
 *
 */
public interface Skin {
	public ImageIcon getFastForwardIcon();

	public ImageIcon getMuteOffIcon();

	public ImageIcon getMuteOnIcon();

	public ImageIcon getPauseIcon();

	public ImageIcon getPlayIcon();

	public ImageIcon getRewindIcon();

	public ImageIcon getStepBackwardIcon();

	public ImageIcon getStepForwardIcon();

	public ImageIcon getStopIcon();
}
