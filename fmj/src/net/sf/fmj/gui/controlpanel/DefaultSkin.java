package net.sf.fmj.gui.controlpanel;

import javax.swing.ImageIcon;

import net.sf.fmj.gui.controlpanel.images.Images;

/**
 * Default skin.
 *
 * @author Ken Larson
 *
 */
public class DefaultSkin implements Skin {
	@Override
	public ImageIcon getFastForwardIcon() {
		return Images.get(Images.MEDIA_FASTFORWARD);
	}

	@Override
	public ImageIcon getMuteOffIcon() {
		return Images.get(Images.MUTE_OFF);
	}

	@Override
	public ImageIcon getMuteOnIcon() {
		return Images.get(Images.MUTE_ON);
	}

	@Override
	public ImageIcon getPauseIcon() {
		return Images.get(Images.MEDIA_PAUSE);
	}

	@Override
	public ImageIcon getPlayIcon() {
		return Images.get(Images.MEDIA_PLAY);
	}

	@Override
	public ImageIcon getRewindIcon() {
		return Images.get(Images.MEDIA_REWIND);
	}

	@Override
	public ImageIcon getStepBackwardIcon() {
		return Images.get(Images.MEDIA_STEPBACK);
	}

	@Override
	public ImageIcon getStepForwardIcon() {
		return Images.get(Images.MEDIA_STEPFORWARD);
	}

	@Override
	public ImageIcon getStopIcon() {
		return Images.get(Images.MEDIA_STOP);
	}

}
