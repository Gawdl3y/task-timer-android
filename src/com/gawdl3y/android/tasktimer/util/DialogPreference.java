package com.gawdl3y.android.tasktimer.util;

import android.content.Context;
import android.util.AttributeSet;

/**
 * A wrapper for the built-in DialogPreference
 * <p>This implementation does absolutely nothing special.</p>
 */
public class DialogPreference extends android.preference.DialogPreference {
    /**
     * Constructor
     * @param context The context for the DialogPreference
     * @param attrs   The attribute set for the DialogPreference
     * @see android.preference.DialogPreference#DialogPreference(android.content.Context, android.util.AttributeSet)
     */
    public DialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
