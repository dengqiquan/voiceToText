package com.xianx.demo;

/**
 * 类来从语音识别API中对HTTP请求的结果进行建模。
 */
public class SpeechRecognition {

    private final long offset;
    private final long duration;
    private final String text;

    public SpeechRecognition(long offset, long duration, String text) {
        this.offset = offset;
        this.duration = duration;
        this.text = text;
    }

    public long getOffset() {
        return offset;
    }

    public long getDuration() {
        return duration;
    }

    public String getText() {
        return text;
    }

}
