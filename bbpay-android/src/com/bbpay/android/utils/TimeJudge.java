package com.bbpay.android.utils;
import com.bbpay.android.listener.ActionListener;

public class TimeJudge extends Thread {
    private static final int m_rate = 300;
    private int actionCode;
    private boolean isRun;
    private ActionListener onActionListener;
    private long startTime;
    private int timeoutMilli;
    public TimeJudge(int timeoutMilli, ActionListener onactionlistener, int actionCode) {
        this.timeoutMilli = timeoutMilli;
        onActionListener = onactionlistener;
        this.actionCode = actionCode;
        isRun = true;
        startTime = System.currentTimeMillis();
    }
    public void close() {
        isRun = false;
    }
    @Override
    public void run() {
        while (isRun) {
            CommonUtil.sleep(m_rate);
            synchronized (this) {
                if (System.currentTimeMillis() - startTime > timeoutMilli) {
                    if (isRun) {
                        isRun = false;
                        if (onActionListener != null) {
                            onActionListener.onActionFinished(actionCode, 1, null);
                            onActionListener = null;
                        }
                    }
                    break;
                }
            }
        }
    }
}
