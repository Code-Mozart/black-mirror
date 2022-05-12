package de.hhn.aib.labsw.blackmirror.controller;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class implements a timer that calls back every second.
 *
 * @author Markus Marewitz
 * @version 2022-04-01
 */
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

    /**
     * Creates a new timer. Every second the callback is triggered.
     * @param onUpdateCallback The callback that is triggered every second.
     */
    public SecondsTimer(Runnable onUpdateCallback) {
        this.onUpdateCallback = onUpdateCallback;

        t = new Timer();
        t.schedule(new EverySecondTimerTask(), getRemainingMillis());
    }

    /**
     * @return The remaining milliseconds for the current second.
     */
    private long getRemainingMillis() {
        return 1000L - System.currentTimeMillis() % 1000L;
    }
}
