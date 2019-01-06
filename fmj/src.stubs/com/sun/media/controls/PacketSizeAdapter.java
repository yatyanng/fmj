package com.sun.media.controls;

import java.awt.Component;

import javax.media.Codec;
import javax.media.control.PacketSizeControl;

/**
 * TODO: Stub
 *
 * @author Ken Larson
 *
 */
public class PacketSizeAdapter implements PacketSizeControl {
	protected Codec owner;
	protected boolean settable;
	protected int packetSize;

	public PacketSizeAdapter(Codec owner, int size, boolean settable) {
		super();
		this.owner = owner;
		packetSize = size;
		this.settable = settable;
	}

	@Override
	public Component getControlComponent() {
		throw new UnsupportedOperationException(); // TODO
	}

	@Override
	public int getPacketSize() {
		return packetSize;
	}

	@Override
	public int setPacketSize(int numBytes) {
		throw new UnsupportedOperationException(); // TODO
	}

}
