package com.bbpay.server.service;
import java.util.Calendar;

public class SeqNumber {
    public static void main(String args[]) {
        SeqNumber s = new SeqNumber(5, 4);
        for (int i = 0; i < 12; i++) {
            System.out.println(s.getSeq());
        }
    }
    private int repidCount;
    private int seqLength;
    private long current;
    private long actualRepid = 0;
    private int date = 0;
    public SeqNumber(int repidCount, int seqLength) {
        this.repidCount = repidCount / 2;
        this.seqLength = seqLength;
        init();
    }
    public String getSeq() {
        if (java.util.Calendar.getInstance().get(Calendar.DATE) != date) {
            init();
        }
        if (actualRepid % repidCount == 0) {
            current++;
        }
        actualRepid++;
        String s = String.valueOf(current);
        return s.substring(1);
    }
    private void init() {
        StringBuilder sb = new StringBuilder();
        sb.append(1);
        for (int i = 0; i < seqLength; i++) {
            sb.append("0");
        }
        current = Long.valueOf(sb.toString());
        actualRepid = 0;
        date = java.util.Calendar.getInstance().get(Calendar.DATE);
    }
}
