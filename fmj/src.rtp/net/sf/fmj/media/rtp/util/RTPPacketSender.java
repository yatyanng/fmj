package net.sf.fmj.media.rtp.util;

import java.io.IOException;

import javax.media.rtp.OutputDataStream;
import javax.media.rtp.RTPConnector;
import javax.media.rtp.RTPPushDataSource;

public class RTPPacketSender implements PacketConsumer {
	RTPPushDataSource dest;
	RTPConnector connector;
	OutputDataStream outstream;

	public RTPPacketSender(OutputDataStream os) {
		dest = null;
		connector = null;
		outstream = null;
		outstream = os;
	}

	public RTPPacketSender(RTPConnector connector) throws IOException {
		dest = null;
		this.connector = null;
		outstream = null;
		this.connector = connector;
		outstream = connector.getDataOutputStream();
	}

	public RTPPacketSender(RTPPushDataSource dest) {
		this.dest = null;
		connector = null;
		outstream = null;
		this.dest = dest;
		outstream = dest.getInputStream();
	}

	@Override
	public void closeConsumer() {
	}

	@Override
	public String consumerString() {
		String s = "RTPPacketSender for " + dest;
		return s;
	}

	public RTPConnector getConnector() {
		return connector;
	}

	@Override
	public void sendTo(Packet p) throws IOException {
		if (outstream == null) {
			throw new IOException();
		} else {
			int byteWritten = outstream.write(p.data, 0, p.length);
			if (byteWritten == -1) {
				throw new IOException();
			}
			return;
		}
	}
}
