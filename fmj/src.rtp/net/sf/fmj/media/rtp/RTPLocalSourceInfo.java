package net.sf.fmj.media.rtp;

import javax.media.rtp.LocalParticipant;
import javax.media.rtp.rtcp.SourceDescription;

public class RTPLocalSourceInfo extends RTPSourceInfo implements LocalParticipant {
	public RTPLocalSourceInfo(String cname, RTPSourceInfoCache sic) {
		super(cname, sic);
	}

	@Override
	public void setSourceDescription(SourceDescription sdeslist[]) {
		super.sic.ssrccache.ourssrc.setSourceDescription(sdeslist);
	}
}
