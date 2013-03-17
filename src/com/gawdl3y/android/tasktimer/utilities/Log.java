package com.gawdl3y.android.tasktimer.utilities;

import com.gawdl3y.android.tasktimer.TaskTimerApplication;

/**
 * Wrapper for logging
 * @author Schuyler Cebulskie
 */
public final class Log {
    /**
     * Send a verbose log message (Only sent when TaskTimerApplication.DEBUG is true)
     * @param tag     Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message The message you would like logged.
     * @see android.util.Log#v(String, String)
     */
    public static final void v(String tag, String message) {
        if(TaskTimerApplication.DEBUG) android.util.Log.v(tag, message);
    }

    /**
     * Send a debug log message (Only sent when TaskTimerApplication.DEBUG is true)
     * @param tag     Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message The message you would like logged.
     * @see android.util.Log#d(String, String)
     */
    public static final void d(String tag, String message) {
        if(TaskTimerApplication.DEBUG) android.util.Log.d(tag, message);
    }

    /**
     * Send an information log message (Only sent when TaskTimerApplication.DEBUG is true)
     * @param tag     Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message The message you would like logged.
     * @see android.util.Log#i(String, String)
     */
    public static final void i(String tag, String message) {
        if(TaskTimerApplication.DEBUG) android.util.Log.i(tag, message);
    }

    /**
     * Send a warning log message
     * @param tag     Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message The message you would like logged.
     * @see android.util.Log#w(String, String)
     */
    public static final void w(String tag, String message) {
        android.util.Log.w(tag, message);
    }

    /**
     * Send an error log message
     * @param tag     Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message The message you would like logged.
     * @see android.util.Log#e(String, String)
     */
    public static final void e(String tag, String message) {
        android.util.Log.e(tag, message);
    }

    /**
     * Send a What a Terrible Failure log message
     * @param tag     Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message The message you would like logged.
     * @see android.util.Log#wtf(String, String)
     */
    public static final void wtf(String tag, String message) {
        android.util.Log.wtf(tag, message);
    }
}
