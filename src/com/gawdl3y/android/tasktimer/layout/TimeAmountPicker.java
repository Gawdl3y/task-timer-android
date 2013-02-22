package com.gawdl3y.android.tasktimer.layout;

import com.gawdl3y.android.tasktimer.MainActivity;
import com.gawdl3y.android.tasktimer.R;
import com.michaelnovakjr.numberpicker.NumberPicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

public class TimeAmountPicker extends FrameLayout {
	private final NumberPicker hoursPicker, minsPicker, secsPicker;
	
	public TimeAmountPicker(Context context) {
		this(context, null);
	}
	
	public TimeAmountPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// Inflate layout
		TypedValue typedValue = new TypedValue();
		MainActivity.CONTEXT.getTheme().resolveAttribute(R.attr.timeAmountPickerLayout, typedValue, true);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//inflater.inflate(typedValue.resourceId, this, true);
		inflater.inflate(R.layout.time_amount_picker_holo, this, true);
		
		// Hours picker
		hoursPicker = (NumberPicker) findViewById(R.id.hours);
		hoursPicker.setSpeed(200);
		
		// Minutes picker
		minsPicker = (NumberPicker) findViewById(R.id.minutes);
		minsPicker.setSpeed(100);
		minsPicker.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
		
		// Seconds picker
		secsPicker = (NumberPicker) findViewById(R.id.seconds);
		secsPicker.setSpeed(100);
		secsPicker.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
		
		// Process style attributes
		TypedArray attributesArray = context.obtainStyledAttributes(attrs, R.styleable.TimeAmountPicker, 0, 0);
		try {
			// Set values
			hoursPicker.setCurrent(attributesArray.getInt(R.styleable.TimeAmountPicker_hours, 1));
			minsPicker.setCurrent(attributesArray.getInt(R.styleable.TimeAmountPicker_minutes, 0));
			secsPicker.setCurrent(attributesArray.getInt(R.styleable.TimeAmountPicker_seconds, 0));
			
			// Disable hours
			if(!attributesArray.getBoolean(R.styleable.TimeAmountPicker_hoursEnabled, true)) {
				hoursPicker.setVisibility(GONE);
			}
			
			// Disable seconds
			if(!attributesArray.getBoolean(R.styleable.TimeAmountPicker_secondsEnabled, true)) {
				secsPicker.setVisibility(GONE);
			}
		} finally {
			attributesArray.recycle();
		}
	}
}
