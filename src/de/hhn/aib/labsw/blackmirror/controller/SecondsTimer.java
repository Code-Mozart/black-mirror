package de.hhn.aib.labsw.blackmirror.controller;

import java.util.Timer;
import java.util.TimerTask;

public class SecondsTimer {
    private final Runnable onUpdateCallback;
    private final Timer t;

    private class EverySecondTimerTask extends TimerTask {
        @Override
        public void run() {
            t.schedule(new EverySecondTimerTask(), getRemainingMillis());
            if (onUpdateCallback != null)
                onUpdateCallback.run();
        }
    }

    public SecondsTimer(Runnable onUpdateCallback) {
        this.onUpdateCallback = onUpdateCallback;

        t = new Timer();
        t.schedule(new EverySecondTimerTask(), getRemainingMillis());
    }

    private long getRemainingMillis() {
        return 1000L - System.currentTimeMillis() % 1000L;
    }
}
