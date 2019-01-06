package net.sf.fmj.media.datasink.render;

import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.CannotRealizeException;
import javax.media.IncompatibleSourceException;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.protocol.DataSource;
import javax.swing.JFrame;

import net.sf.fmj.ejmf.toolkit.util.PlayerPanel;
import net.sf.fmj.media.AbstractDataSink;
import net.sf.fmj.utility.LoggerSingleton;

/**
 * DataSink that creates a player and renders. Really only for testing.
 *
 * @author Ken Larson
 *
 */
public class Handler extends AbstractDataSink {
	private static final Logger logger = LoggerSingleton.logger;

	private DataSource source;

	// TODO: additional listener notifications?

	private Player player;

	@Override
	public void close() {
		try {
			stop();
		} catch (IOException e) {
			logger.log(Level.WARNING, "" + e, e);
		}

		// TODO: disconnect source?
	}

	@Override
	public String getContentType() {
		// TODO: do we get this from the source, or the outputLocator?
		if (source != null)
			return source.getContentType();
		else
			return null;
	}

	@Override
	public Object getControl(String controlType) {
		logger.warning("TODO: getControl " + controlType);
		return null;
	}

	@Override
	public Object[] getControls() {
		logger.warning("TODO: getControls");
		return new Object[0];
	}

	@Override
	public void open() throws IOException, SecurityException {
		// source.connect(); // Manager/player will take care of this.

		try {
			player = Manager.createRealizedPlayer(source);
		} catch (NoPlayerException e) {
			logger.log(Level.WARNING, "" + e, e);
			throw new IOException("" + e);
		} catch (CannotRealizeException e) {
			logger.log(Level.WARNING, "" + e, e);
			throw new IOException("" + e);
		}

		// TODO: GUI

	}

	@Override
	public void setSource(DataSource source) throws IOException, IncompatibleSourceException {
		this.source = source;
	}

	@Override
	public void start() throws IOException {
		// no need to open GUI for just audio.

		// create GUI frame, add player's GUI components to it:
		if (player.getVisualComponent() != null) {
			final PlayerPanel playerpanel;
			try {
				playerpanel = new PlayerPanel(player);
			} catch (NoPlayerException e) {
				logger.log(Level.WARNING, "" + e, e);
				throw new IOException("" + e);
			}

			// already realized so this will work:
			// playerpanel.addControlComponent(); // no need for control
			// component
			playerpanel.addVisualComponent();

			final JFrame frame = new JFrame("Renderer");

			// exit on close:
			// Allow window to close
			frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					// TODO: close player?
					// System.exit(0);
				}
			});

			// Resize frame whenever new Component is added
			playerpanel.getMediaPanel().addContainerListener(new ContainerListener() {
				@Override
				public void componentAdded(ContainerEvent e) {
					frame.pack();
				}

				@Override
				public void componentRemoved(ContainerEvent e) {
					frame.pack();
				}
			});

			// finish constructing window, and open it
			frame.getContentPane().add(playerpanel);

			frame.pack();
			frame.setVisible(true);
		}

		player.start();
	}

	@Override
	public void stop() throws IOException {
		if (player != null)
			player.stop();

	}

}
