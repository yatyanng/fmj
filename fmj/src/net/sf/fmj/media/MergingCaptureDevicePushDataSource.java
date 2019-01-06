package net.sf.fmj.media;

import java.util.ArrayList;
import java.util.List;

import javax.media.CaptureDeviceInfo;
import javax.media.control.FormatControl;
import javax.media.protocol.CaptureDevice;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushDataSource;

/**
 * Merges multiple {@link PushDataSource} that implement {@link CaptureDevice}.
 *
 * @author Ken Larson
 *
 */
public class MergingCaptureDevicePushDataSource extends MergingPushDataSource implements CaptureDevice {
	public MergingCaptureDevicePushDataSource(List<PushDataSource> sources) {
		super(sources);
		for (DataSource source : sources) {
			if (!(source instanceof CaptureDevice))
				throw new IllegalArgumentException();
		}
	}

	@Override
	public CaptureDeviceInfo getCaptureDeviceInfo() {
		throw new UnsupportedOperationException(); // TODO
	}

	@Override
	public FormatControl[] getFormatControls() {
		final List<FormatControl> formatControls = new ArrayList<FormatControl>();
		for (DataSource source : sources) {
			for (FormatControl formatControl : ((CaptureDevice) source).getFormatControls())
				formatControls.add(formatControl);
		}
		return formatControls.toArray(new FormatControl[0]);
	}
}
