package javax.media.cdm;

import java.util.Vector;

import javax.media.CaptureDeviceInfo;
import javax.media.Format;

import net.sf.fmj.registry.Registry;

/**
 * Internal implementation of javax.media.CaptureDeviceManager. Coding complete.
 *
 * @author Ken Larson
 */
public class CaptureDeviceManager extends javax.media.CaptureDeviceManager {
	public static synchronized boolean addDevice(CaptureDeviceInfo newDevice) {
		return Registry.getInstance().addDevice(newDevice);
	}

	public static synchronized void commit() throws java.io.IOException {
		Registry.getInstance().commit();
	}

	public static synchronized CaptureDeviceInfo getDevice(String deviceName) {
		for (CaptureDeviceInfo captureDeviceInfo : getDeviceList()) {
			if (captureDeviceInfo.getName().equals(deviceName))
				return captureDeviceInfo;
		}
		return null;
	}

	public static synchronized Vector<CaptureDeviceInfo> getDeviceList() // not in javax.media.CaptureDeviceManager
	{
		return Registry.getInstance().getDeviceList();
	}

	public static synchronized Vector<CaptureDeviceInfo> getDeviceList(Format format) {
		Vector<CaptureDeviceInfo> result = new Vector<CaptureDeviceInfo>();
		for (CaptureDeviceInfo captureDeviceInfo : getDeviceList()) {
			if (format == null) {
				result.add(captureDeviceInfo);
			} else {
				for (Format aFormat : captureDeviceInfo.getFormats()) {
					if (format.matches(aFormat)) {
						result.add(captureDeviceInfo);
						break;
					}
				}
			}

		}
		return result;
	}

	public static synchronized boolean removeDevice(CaptureDeviceInfo device) {
		return Registry.getInstance().removeDevice(device);
	}

	public CaptureDeviceManager() {
		super();
	}
}
