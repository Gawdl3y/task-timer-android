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

public class TaskTimePicker extends FrameLayout {
	private final NumberPicker hoursPicker, minsPicker, secsPicker;
	
	public TaskTimePicker(Context context) {
		this(context, null);
	}
	
	public TaskTimePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// Inflate layout
		TypedValue typedValue = new TypedValue();
		MainActivity.CONTEXT.getTheme().resolveAttribute(R.attr.taskTimePickerLayout, typedValue, true);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//inflater.inflate(typedValue.resourceId, this, true);
		inflater.inflate(R.layout.task_time_picker, this, true);
		
		// Initialize
		hoursPicker = (NumberPicker) findViewById(R.id.hours);
		minsPicker = (NumberPicker) findViewById(R.id.minutes);
		secsPicker = (NumberPicker) findViewById(R.id.seconds);
		
		minsPicker.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
		secsPicker.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
		
		// Process style attributes
		TypedArray attributesArray = context.obtainStyledAttributes(attrs, R.styleable.TaskTimePicker, 0, 0);
		try {
			hoursPicker.setCurrent(attributesArray.getInt(R.styleable.TaskTimePicker_hours, 1));
			minsPicker.setCurrent(attributesArray.getInt(R.styleable.TaskTimePicker_minutes, 0));
			secsPicker.setCurrent(attributesArray.getInt(R.styleable.TaskTimePicker_seconds, 0));
		} finally {
			attributesArray.recycle();
		}
	}
}
