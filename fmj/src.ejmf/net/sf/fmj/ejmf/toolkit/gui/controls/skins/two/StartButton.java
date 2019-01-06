package net.sf.fmj.ejmf.toolkit.gui.controls.skins.two;

import javax.swing.ImageIcon;

import net.sf.fmj.ejmf.toolkit.gui.controls.BasicIconButton;

/**
 * @author Ken Larson
 */
public class StartButton extends BasicIconButton {
	public StartButton() {
		super(new ImageIcon(StartButton.class.getResource("resources/control_play_blue.png")),
				new ImageIcon(StartButton.class.getResource("resources/control_play.png")));
	}
}
