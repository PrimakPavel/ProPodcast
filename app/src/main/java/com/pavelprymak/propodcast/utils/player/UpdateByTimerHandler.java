package com.pavelprymak.propodcast.utils.player;


import android.os.Handler;

public abstract class UpdateByTimerHandler {
    private long delay = 1000L;
    private Handler mHandler = new Handler();
    // flag that should be set true if handler should stop
    private boolean mStopHandler = false;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            // do your stuff - don't create a new runnable here!
            try {
                if (!mStopHandler) {
                    doOperation();
                    mHandler.postDelayed(this, delay);
                }
            } catch (Exception ex) {
                //ignore
            }
        }
    };

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public void startHandler() {
        // start
        mHandler.removeCallbacks(mRunnable);
        mStopHandler = false;
        mHandler.post(mRunnable);
    }

    public void stopHandler() {
        //stop
        mStopHandler = true;
        mHandler.removeCallbacks(mRunnable);
    }

    public boolean isStopHandler() {
        return mStopHandler;
    }

    public abstract void doOperation();
}
