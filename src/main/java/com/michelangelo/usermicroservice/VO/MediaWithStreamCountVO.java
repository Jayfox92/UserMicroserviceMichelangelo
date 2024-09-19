package com.michelangelo.usermicroservice.VO;

public class MediaWithStreamCountVO {
    private MediaVO mediaVO;
    private int streamHistoryCount;

    public MediaWithStreamCountVO(MediaVO mediaVO, int streamHistoryCount) {
        this.mediaVO = mediaVO;
        this.streamHistoryCount = streamHistoryCount;
    }

    public MediaVO getMediaVO() {
        return mediaVO;
    }

    public void setMediaVO(MediaVO mediaVO) {
        this.mediaVO = mediaVO;
    }

    public int getStreamHistoryCount() {
        return streamHistoryCount;
    }

    public void setStreamHistoryCount(int streamHistoryCount) {
        this.streamHistoryCount = streamHistoryCount;
    }
}
