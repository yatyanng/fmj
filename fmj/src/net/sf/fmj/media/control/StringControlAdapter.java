package net.sf.fmj.media.control;

import java.awt.Component;

import javax.media.Control;

public class StringControlAdapter extends AtomicControlAdapter implements StringControl {
	String value;
	String title;

	public StringControlAdapter() {
		super(null, true, null);
	}

	public StringControlAdapter(Component c, boolean def, Control parent) {
		super(c, def, parent);
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String setTitle(String title) {
		this.title = title;
		informListeners();
		return title;
	}

	@Override
	public String setValue(String value) {
		this.value = value;
		informListeners();
		return value;
	}
}
