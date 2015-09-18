package com.mfh.comna.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.format.DateUtils;

import com.mfh.comna.api.utils.MLog;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is responsible for tracking all currently open activities.
 * By doing so this class can detect when the application is in the foreground
 * and when it is running in the background.
 */
public class AppRunningStateManager {
    private static final String TAG = AppRunningStateManager.class.getSimpleName();
    private static final int MESSAGE_NOTIFY_LISTENERS = 1;
    public static final long APP_CLOSED_VALIDATION_TIME_IN_MS = 5 * DateUtils.SECOND_IN_MILLIS; // 5 Seconds

    private Reference<Activity> mForegroundActivity;


    public enum AppRunningState {
        FOREGROUND,
        BACKGROUND
    }
    private AppRunningState mAppRunningState = AppRunningState.BACKGROUND;

    public interface OnAppRunningStateChangeListener {
        /** Called when the running state of the app changes */
        public void onAppRunningStateChange(AppRunningState newState);
    }
    private Set<OnAppRunningStateChangeListener> mListeners = new HashSet<OnAppRunningStateChangeListener>();

    private NotifyListenersHandler mHandler;

    // Make this class a thread safe singleton
    private static class SingletonHolder {
        public static final AppRunningStateManager INSTANCE = new AppRunningStateManager();
    }

    public static AppRunningStateManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private AppRunningStateManager() {
        // Create the handler on the main thread
        mHandler = new NotifyListenersHandler(Looper.getMainLooper());
    }

    /** An activity should call this when it becomes visible */
    public void onActivityVisible(Activity activity) {
        if (mForegroundActivity != null) {
            mForegroundActivity.clear();
        }
        mForegroundActivity = new WeakReference<Activity>(activity);

        determineAppRunningState();
    }

    /** An activity should call this when it is no longer visible */
    public void onActivityInvisible(Activity activity) {
        /*
         * The foreground activity may have been replaced with a new foreground activity in our app.
         * So only clear the foregroundActivity if the new activity matches the foreground activity.
         */
        if (mForegroundActivity != null) {
            Activity ref = mForegroundActivity.get();

            if (activity == ref) {
                // This is the activity that is going away, clear the reference
                mForegroundActivity.clear();
                mForegroundActivity = null;
            }
        }

        determineAppRunningState();
    }

    /**
     * Call to determine the current state, update the tracking global, and notify subscribers if the state has changed.
     */
    private void determineAppRunningState() {
        /* Get the current state */
        AppRunningState oldState = mAppRunningState;

        /* Determine what the new state should be */
        final boolean isInForeground = mForegroundActivity != null && mForegroundActivity.get() != null;
        mAppRunningState = isInForeground ? AppRunningState.FOREGROUND : AppRunningState.BACKGROUND;

        /* If the new state is different then the old state the notify subscribers of the state change */
        if (mAppRunningState != oldState) {
            validateThenNotifyListeners();
        }
    }

    /**
     * This method will notify subscribes that the foreground state has changed when and if appropriate.
     * <br><br>
     * We do not want to just notify listeners right away when the app enters of leaves the foreground. When changing orientations or opening and
     * closing the app quickly we briefly pass through a NOT_IN_FOREGROUND state that must be ignored. To accomplish this a delayed message will be
     * Sent when we detect a change. We will not notify that a foreground change happened until the delay time has been reached. If a second
     * foreground change is detected during the delay period then the notification will be canceled.
     */
    private void validateThenNotifyListeners() {
        // If the app has any pending notifications then throw out the event as the state change has failed validation
        if (mHandler.hasMessages(MESSAGE_NOTIFY_LISTENERS)) {
            MLog.v("Validation Failed: Throwing out app foreground state change notification");
            mHandler.removeMessages(MESSAGE_NOTIFY_LISTENERS);
        } else {
            if (mAppRunningState == AppRunningState.FOREGROUND) {
                // If the app entered the foreground then notify listeners right away; there is no validation time for this
                mHandler.sendEmptyMessage(MESSAGE_NOTIFY_LISTENERS);
            } else {
                // We need to validate that the app entered the background. A delay is used to allow for time when the application went into the
                // background but we do not want to consider the app being backgrounded such as for in app purchasing flow and full screen ads.
                mHandler.sendEmptyMessageDelayed(MESSAGE_NOTIFY_LISTENERS, APP_CLOSED_VALIDATION_TIME_IN_MS);
            }
        }
    }


    /**
     * Add a listener to be notified of app foreground state change events.
     *
     * @param listener
     */
    public void addListener(OnAppRunningStateChangeListener listener) {
        mListeners.add(listener);
    }

    /**
     * Remove a listener from being notified of app foreground state change events.
     *
     * @param listener
     */
    public void removeListener(OnAppRunningStateChangeListener listener) {
        mListeners.remove(listener);
    }

    /** Notify all listeners the app running state has changed */
    private void notifyListeners(AppRunningState newState) {
        android.util.Log.i(TAG, "Notifying subscribers that app just entered state: " + newState);

        for (OnAppRunningStateChangeListener listener : mListeners) {
            listener.onAppRunningStateChange(newState);
        }
    }

    private class NotifyListenersHandler extends Handler {
        private NotifyListenersHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message inputMessage) {
            switch (inputMessage.what) {
                // The decoding is done
                case MESSAGE_NOTIFY_LISTENERS:
                    /* Notify subscribers of the state change */
                    MLog.v(TAG, "App just changed running state to: " + mAppRunningState);
                    notifyListeners(mAppRunningState);
                    break;
                default:
                    super.handleMessage(inputMessage);
            }
        }
    }

}
