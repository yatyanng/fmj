package net.sf.fmj.ejmf.toolkit.gui.controls;

import java.awt.Component;

import javax.swing.AbstractButton;

/**
 *
 * @author Ken Larson
 *
 */
public interface Skin {
	public Component createFastForwardButton();

	public Component createGainMeterButton();

	public Component createPauseButton();

	// public Component createTimeDisplayControl();
	public Component createProgressSlider();

	public Component createReverseButton();

	public Component createStartButton();

	public Component createStopButton();

	public AbstractButton createVolumeControlButton_Decrease();

	public AbstractButton createVolumeControlButton_Increase();
}
