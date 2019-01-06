package net.sf.fmj.ejmf.toolkit.gui.controls.skins.two;

import java.awt.Component;

import javax.swing.AbstractButton;

import net.sf.fmj.ejmf.toolkit.gui.controls.Skin;
import net.sf.fmj.ejmf.toolkit.gui.controls.skins.ejmf.GainMeterButton;
import net.sf.fmj.ejmf.toolkit.gui.controls.skins.ejmf.ProgressSlider;
import net.sf.fmj.ejmf.toolkit.gui.controls.skins.ejmf.ReverseButton;
import net.sf.fmj.ejmf.toolkit.gui.controls.skins.ejmf.VolumeControlButton;

/**
 *
 * @author Ken Larson
 *
 */
public class SkinTwo implements Skin {
	@Override
	public Component createFastForwardButton() {
		return new FastForwardButton();
	}

	@Override
	public Component createGainMeterButton() {
		return new GainMeterButton();
	}

	@Override
	public Component createPauseButton() {
		return new PauseButton();
	}

	@Override
	public Component createProgressSlider() {
		return new ProgressSlider();
	}

	@Override
	public Component createReverseButton() {
		return new ReverseButton();
	}

	@Override
	public Component createStartButton() {
		return new StartButton();
	}

	@Override
	public Component createStopButton() {
		return new StopButton();
	}

	@Override
	public AbstractButton createVolumeControlButton_Decrease() {
		return new VolumeControlButton(VolumeControlButton.DECREASE);
	}

	@Override
	public AbstractButton createVolumeControlButton_Increase() {
		return new VolumeControlButton(VolumeControlButton.INCREASE);
	}
}
