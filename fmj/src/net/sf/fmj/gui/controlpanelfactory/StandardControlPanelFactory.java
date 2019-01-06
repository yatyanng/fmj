package net.sf.fmj.gui.controlpanelfactory;

import java.awt.Component;

import javax.media.Player;

import net.sf.fmj.ejmf.toolkit.gui.controlpanel.AbstractControlPanel;
import net.sf.fmj.ejmf.toolkit.gui.controlpanel.StandardControlPanel;

/**
 * {@link ControlPanelFactory} for {@link StandardControlPanel}, which is based
 * on EJMF.
 *
 * @author Ken Larson
 *
 */
public class StandardControlPanelFactory implements ControlPanelFactory {
	@Override
	public Component getControlPanelComponent(Player p) {
		return new StandardControlPanel(p, AbstractControlPanel.USE_START_CONTROL
				| AbstractControlPanel.USE_STOP_CONTROL | AbstractControlPanel.USE_PROGRESS_CONTROL);

	}

}
