package net.sf.fmj.media.rtp;

import javax.media.rtp.TransmissionStats;

public class RTPTransStats implements TransmissionStats {
	protected int total_pdu;
	protected int total_bytes;

	/**
	 * Updated by the <tt>RTPTransmitter</tt> or the <tt>RTCPTransmitter</tt> when
	 * an RTCP packet is transmitted.
	 */
	public int total_rtcp;

	public RTPTransStats() {
		total_pdu = 0;
		total_bytes = 0;
		total_rtcp = 0;
	}

	@Override
	public int getBytesTransmitted() {
		return total_bytes;
	}

	@Override
	public int getPDUTransmitted() {
		return total_pdu;
	}

	@Override
	public int getRTCPSent() {
		return total_rtcp;
	}
}
