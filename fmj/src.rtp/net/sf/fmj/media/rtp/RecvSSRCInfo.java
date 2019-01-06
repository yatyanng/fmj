package net.sf.fmj.media.rtp;

import java.util.Vector;

import javax.media.protocol.DataSource;
import javax.media.rtp.LocalParticipant;
import javax.media.rtp.Participant;
import javax.media.rtp.RTPStream;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.ReceptionStats;
import javax.media.rtp.rtcp.Feedback;
import javax.media.rtp.rtcp.ReceiverReport;
import javax.media.rtp.rtcp.Report;
import javax.media.rtp.rtcp.SenderReport;

public class RecvSSRCInfo extends SSRCInfo implements ReceiveStream, SenderReport, ReceiverReport {
	RecvSSRCInfo(SSRCCache cache, int ssrc) {
		super(cache, ssrc);
	}

	RecvSSRCInfo(SSRCInfo info) {
		super(info);
	}

	@Override
	public DataSource getDataSource() {
		return super.dsource;
	}

	@Override
	public long getNTPTimeStampLSW() {
		return super.lastSRntptimestamp & 0xffffffffL;
	}

	@Override
	public long getNTPTimeStampMSW() {
		return super.lastSRntptimestamp >> 32 & 0xffffffffL;
	}

	@Override
	public Participant getParticipant() {
		SSRCCache cache = getSSRCCache();
		if ((super.sourceInfo instanceof LocalParticipant) && cache.sm.IsNonParticipating())
			return null;
		else
			return super.sourceInfo;
	}

	@Override
	public long getRTPTimeStamp() {
		return super.lastSRrtptimestamp;
	}

	@Override
	public long getSenderByteCount() {
		return super.lastSRoctetcount;
	}

	@Override
	public Feedback getSenderFeedback() {
		SSRCCache cache = getSSRCCache();
		Report report = null;
		Vector reports = null;
		Vector feedback = null;
		Feedback reportblk = null;
		try {
			LocalParticipant localpartc = cache.sm.getLocalParticipant();
			reports = localpartc.getReports();
			for (int i = 0; i < reports.size(); i++) {
				report = (Report) reports.elementAt(i);
				feedback = report.getFeedbackReports();
				for (int j = 0; j < feedback.size(); j++) {
					reportblk = (Feedback) feedback.elementAt(j);
					long ssrc = reportblk.getSSRC();
					if (ssrc == getSSRC())
						return reportblk;
				}

			}

			return null;
		} catch (NullPointerException e) {
			return null;
		}
	}

	@Override
	public long getSenderPacketCount() {
		return super.lastSRpacketcount;
	}

	@Override
	public SenderReport getSenderReport() {
		return this;
	}

	@Override
	public ReceptionStats getSourceReceptionStats() {
		return super.stats;
	}

	@Override
	public long getSSRC() {
		return super.ssrc;
	}

	@Override
	public RTPStream getStream() {
		return this;
	}
}
