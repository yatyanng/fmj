package net.sf.fmj.gui.controlpanelfactory;

import java.awt.Component;

import javax.media.Player;

import net.sf.fmj.gui.controlpanel.SwingLookControlPanel;

/**
 *
 * @author Ken Larson
 *
 */
public class SwingLookControlPanelFactory implements ControlPanelFactory {
	@Override
	public Component getControlPanelComponent(Player p) {
		return new SwingLookControlPanel(p);
	}

}
