package net.sf.fmj.media.control;

import java.awt.Component;

import javax.media.Control;

public class SliderRegionControlAdapter extends AtomicControlAdapter implements SliderRegionControl {
	long min, max;
	boolean enable;

	public SliderRegionControlAdapter() {
		super(null, true, null);
		enable = true;
	}

	public SliderRegionControlAdapter(Component c, boolean def, Control parent) {
		super(c, def, parent);
	}

	@Override
	public long getMaxValue() {
		return max;
	}

	@Override
	public long getMinValue() {
		return min;
	}

	@Override
	public boolean isEnable() {
		return enable;
	}

	@Override
	public void setEnable(boolean f) {
		enable = f;
	}

	@Override
	public long setMaxValue(long value) {
		// this.max = value / 1000000L;
		this.max = value;
		informListeners();
		return max;
	}

	@Override
	public long setMinValue(long value) {
		// this.min = value / 1000000L;
		this.min = value;
		informListeners();
		return min;
	}
}
