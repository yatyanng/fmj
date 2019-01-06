package com.sun.media.controls;

import java.awt.Component;

import javax.media.Codec;
import javax.media.control.H263Control;

/**
 * TODO: Stub
 *
 * @author Ken Larson
 *
 */
public class H263Adapter implements H263Control {
	private boolean advancedPrediction;
	private boolean arithmeticCoding;
	private boolean errorCompensation;
	private boolean pbFrames;
	private boolean unrestrictedVector;
	private int hrd_B;
	private int bppMaxKb;

	public H263Adapter(Codec owner, boolean prediction, boolean coding, boolean compensation, boolean frames,
			boolean vector, int hrd_b, int kb, boolean settable) {
		advancedPrediction = prediction;
		arithmeticCoding = coding;
		errorCompensation = compensation;
		pbFrames = frames;
		unrestrictedVector = vector;
		hrd_B = hrd_b;
		bppMaxKb = kb;
	}

	@Override
	public boolean getAdvancedPrediction() {
		return advancedPrediction;
	}

	@Override
	public boolean getArithmeticCoding() {
		return arithmeticCoding;
	}

	@Override
	public int getBppMaxKb() {
		return bppMaxKb;
	}

	@Override
	public Component getControlComponent() {
		throw new UnsupportedOperationException(); // TODO
	}

	@Override
	public boolean getErrorCompensation() {
		return errorCompensation;
	}

	@Override
	public int getHRD_B() {
		return hrd_B;
	}

	@Override
	public boolean getPBFrames() {
		return pbFrames;
	}

	@Override
	public boolean getUnrestrictedVector() {
		return unrestrictedVector;
	}

	@Override
	public boolean isAdvancedPredictionSupported() {
		throw new UnsupportedOperationException(); // TODO
	}

	@Override
	public boolean isArithmeticCodingSupported() {
		throw new UnsupportedOperationException(); // TODO
	}

	@Override
	public boolean isErrorCompensationSupported() {
		throw new UnsupportedOperationException(); // TODO
	}

	@Override
	public boolean isPBFramesSupported() {
		throw new UnsupportedOperationException(); // TODO
	}

	@Override
	public boolean isUnrestrictedVectorSupported() {
		throw new UnsupportedOperationException(); // TODO
	}

	@Override
	public boolean setAdvancedPrediction(boolean newAdvancedPredictionMode) {
		throw new UnsupportedOperationException(); // TODO
	}

	@Override
	public boolean setArithmeticCoding(boolean newArithmeticCodingMode) {
		throw new UnsupportedOperationException(); // TODO
	}

	@Override
	public boolean setErrorCompensation(boolean newtErrorCompensationMode) {
		throw new UnsupportedOperationException(); // TODO
	}

	@Override
	public boolean setPBFrames(boolean newPBFramesMode) {
		throw new UnsupportedOperationException(); // TODO
	}

	@Override
	public boolean setUnrestrictedVector(boolean newUnrestrictedVectorMode) {
		throw new UnsupportedOperationException(); // TODO
	}

}
