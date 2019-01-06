package javax.media.bean.playerbean;

import java.awt.Component;
import java.awt.Panel;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.ClockStoppedException;
import javax.media.Control;
import javax.media.Controller;
import javax.media.ControllerListener;
import javax.media.GainControl;
import javax.media.IncompatibleSourceException;
import javax.media.IncompatibleTimeBaseException;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.Time;
import javax.media.TimeBase;
import javax.media.protocol.DataSource;

import net.sf.fmj.utility.LoggerSingleton;

public class MediaPlayer extends java.awt.Container implements Player, java.io.Externalizable {
	private static final Logger logger = LoggerSingleton.logger;

	// created the same vars that Sun's has, we aren't using many of them yet
	// (or may might not ever)
//    private PropertyChangeSupport changes;

//    private String urlString;

//    private MediaLocator mrl;

//    private URL url;

//    private AppletContext mpAppletContext;

	private boolean panelVisible;

	private boolean cachingVisible;

//    private boolean fixedAspectRatio;

//    private boolean fitVideo;

	private boolean looping;

	transient Player player;

	transient Panel panel;

	transient Panel vPanel;

	transient Panel newPanel;

	transient Component visualComponent;

	transient Component controlComponent;

	transient Component cachingComponent;
	private transient int controlPanelHeight;

	private transient int urlFieldHeight;

//    private int preferredHeight;

//    private int preferredWidth;

//    private int state;

//    private Vector controlListeners;

//    private PopupMenu zoomMenu;

//    private URL mpCodeBase;

	protected transient GainControl gainControl;

	protected transient String curVolumeLevel;

	protected transient float curVolumeValue;

	protected transient String curZoomLevel;

	protected transient float curZoomValue;

	protected transient Time mediaTime;

	// selfListener

//    private long contentLength;

	// private boolean fixtedFirstTime;

	private boolean displayURL;

//    private boolean isPopupActive;

	// urlName

	// mouseListen;

	// errMeth;

	public MediaPlayer() {
	}

	@Override
	public void addController(Controller newController) throws IncompatibleTimeBaseException {
		if (player == null)
			return;

		player.addController(newController);
	}

	@Override
	public void addControllerListener(ControllerListener listener) {
		if (player == null)
			return;

		player.addControllerListener(listener);
	}

	@Override
	public void addPropertyChangeListener(java.beans.PropertyChangeListener c) {
		throw new UnsupportedOperationException(); // TODO
	}

	@Override
	public void close() {
		if (player == null)
			return;
		player.close();
	}

	@Override
	public void deallocate() {
		if (player == null)
			return;
		player.deallocate();
	}

	@Override
	public Control getControl(String forName) {
		if (player == null)
			return null;
		return player.getControl(forName);
	}

	@Override
	public Component getControlPanelComponent() {
		if (player == null)
			return null;
		return player.getControlPanelComponent();
	}

	public int getControlPanelHeight() {
		return controlPanelHeight;
	}

	@Override
	public Control[] getControls() {
		if (player == null)
			return new Control[0];
		return player.getControls(); // TODO: do we add extra controls?
	}

	@Override
	public Time getDuration() {
		if (player == null)
			return DURATION_UNKNOWN;
		return player.getDuration();
	}

	@Override
	public GainControl getGainControl() {
		if (player == null)
			return null;
		return player.getGainControl();
	}

	public String getMediaLocation() {
		if (player == null)
			return " ";
		throw new UnsupportedOperationException(); // TODO
	}

	public int getMediaLocationHeight() {
		return urlFieldHeight;
	}

	protected MediaLocator getMediaLocator(String filename) {
		return new MediaLocator("file://" + filename);
	}

	@Override
	public long getMediaNanoseconds() {
		if (player == null)
			return Long.MAX_VALUE;
		return player.getMediaNanoseconds();
	}

	@Override
	public Time getMediaTime() {
		if (player == null)
			return new Time(Long.MAX_VALUE);

		return player.getMediaTime();
	}

	public boolean getPlaybackLoop() {
		return looping;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public java.awt.Dimension getPreferredSize() {
		throw new UnsupportedOperationException(); // TODO
	}

	@Override
	public float getRate() {
		if (player == null)
			return 0.f;
		return player.getRate();
	}

	@Override
	public Time getStartLatency() {
		if (player == null)
			return new Time(Long.MAX_VALUE);

		return player.getStartLatency();
	}

	@Override
	public int getState() {
		if (player == null)
			return Unrealized;
		return player.getState();
	}

	@Override
	public Time getStopTime() {
		if (player == null)
			return null;
		return player.getStopTime();
	}

	@Override
	public Time getSyncTime() {
		if (player == null)
			return new Time(Long.MAX_VALUE);

		return player.getSyncTime();
	}

	@Override
	public int getTargetState() {
		if (player == null)
			return Unrealized;
		return player.getTargetState();
	}

	@Override
	public TimeBase getTimeBase() {
		if (player == null)
			return null;
		return player.getTimeBase();
	}

	@Override
	public Component getVisualComponent() {
		if (player == null)
			return null;

		return player.getVisualComponent();
	}

	public String getVolumeLevel() {
		return curVolumeLevel;
	}

	public String getZoomTo() {
		return curZoomLevel;
	}

	public boolean isCachingControlVisible() {
		return cachingVisible;
	}

	public boolean isControlPanelVisible() {
		return panelVisible;
	}

	public boolean isFixedAspectRatio() {
		throw new UnsupportedOperationException(); // TODO
	}

	public boolean isMediaLocationVisible() {
		return displayURL;
	}

	public boolean isPlayBackLoop() {
		return looping;
	}

	@Override
	public Time mapToTimeBase(Time t) throws ClockStoppedException {
		if (player == null)
			return new Time(Long.MAX_VALUE);

		return player.mapToTimeBase(t);
	}

	@Override
	public void prefetch() {
		if (player == null)
			return;

		player.prefetch();
	}

	@Override
	public void readExternal(ObjectInput in) throws java.io.IOException, ClassNotFoundException {
		throw new UnsupportedOperationException(); // TODO
	}

	@Override
	public void realize() {
		if (player == null)
			return;
		player.realize();
	}

	@Override
	public void removeController(Controller oldController) {
		if (player == null)
			return;
		player.removeController(oldController);
	}

	@Override
	public void removeControllerListener(ControllerListener listener) {
		if (player == null)
			return;
		player.removeControllerListener(listener);
	}

	@Override
	public void removePropertyChangeListener(java.beans.PropertyChangeListener c) {
		throw new UnsupportedOperationException(); // TODO
	}

	public void restoreMediaTime() {
		throw new UnsupportedOperationException(); // TODO
	}

	public void saveMediaTime() {
		throw new UnsupportedOperationException(); // TODO
	}

	@Override
	public void setBounds(int x, int y, int w, int h) {
		throw new UnsupportedOperationException(); // TODO
	}

	public void setCachingControlVisible(boolean isVisible) {
		cachingVisible = isVisible;
	}

	public void setCodeBase(java.net.URL cb) {
//        mpCodeBase = cb;
	}

	public void setControlPanelVisible(boolean isVisible) {
		panelVisible = isVisible;
	}

	public void setDataSource(DataSource ds) {
		try {
			player = Manager.createPlayer(ds);
		} catch (NoPlayerException e) {
			logger.log(Level.WARNING, "" + e, e);
		} catch (IOException e) {
			logger.log(Level.WARNING, "" + e, e);
		}
	}

	public void setFixedAspectRatio(boolean isFixed) {
		throw new UnsupportedOperationException(); // TODO
	}

	public void setMediaLocation(String location) {
		throw new UnsupportedOperationException(); // TODO
	}

	public void setMediaLocationVisible(boolean val) {
		displayURL = val;
	}

	public void setMediaLocator(MediaLocator locator) {
		try {
			player = Manager.createPlayer(locator);
		} catch (NoPlayerException e) {
			logger.log(Level.WARNING, "" + e, e);
		} catch (IOException e) {
			logger.log(Level.WARNING, "" + e, e);
		}

	}

	@Override
	public void setMediaTime(Time now) {
		if (player == null)
			return;
		player.setMediaTime(now);
	}

	public void setPlaybackLoop(boolean val) {
		looping = val;
	}

	public void setPlayer(Player newPlayer) {
		this.player = newPlayer;
	}

	public void setPopupActive(boolean isActive) {
//        isPopupActive = isActive;
	}

	@Override
	public float setRate(float factor) {
		if (player == null)
			return 0.f;
		return player.setRate(factor);
	}

	@Override
	public void setSource(DataSource source) throws IOException, IncompatibleSourceException {
		if (player == null)
			return;
		player.setSource(source);
	}

	@Override
	public void setStopTime(Time stopTime) {
		if (player == null)
			return;
		player.setStopTime(stopTime);
	}

	@Override
	public void setTimeBase(TimeBase master) throws IncompatibleTimeBaseException {
		if (player == null)
			return;
		player.setTimeBase(master);
	}

	public void setVolumeLevel(String volumeString) {
		curVolumeLevel = volumeString; // TODO: float conversion
	}

	public void setZoomTo(String scale) {
		this.curZoomLevel = scale; // TODO: convert to float
	}

	@Override
	public void start() {
		if (player == null)
			return;
		player.start();
	}

	@Override
	public void stop() {
		if (player == null)
			return;
		player.stop();
	}

	public void stopAndDeallocate() {
		stop();
		deallocate();
		// TODO: is it more complicated than this?
	}

	@Override
	public void syncStart(Time at) {
		if (player == null)
			return;
		player.syncStart(at);
	}

	// synchronized in reference impl
	public synchronized void waitForState(int s) {
		throw new UnsupportedOperationException(); // TODO
	}

	@Override
	public void writeExternal(ObjectOutput out) throws java.io.IOException {
		throw new UnsupportedOperationException(); // TODO
	}
}
