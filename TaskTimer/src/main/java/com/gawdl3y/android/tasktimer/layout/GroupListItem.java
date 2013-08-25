package com.gawdl3y.android.tasktimer.layout;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.TaskTimerApplication;
import com.gawdl3y.android.tasktimer.pojos.Group;
import com.gawdl3y.android.tasktimer.util.Log;

/**
 * The view for group list items
 * @author Schuyler Cebulskie
 */
public class GroupListItem extends LinearLayout implements Checkable {
    private static final String TAG = "GroupListItem";

    // Data
    private Group mGroup;
    private boolean mChecked = false;

    // Views
    private TextView mNameView, mTaskCountView;

    /**
     * Constructor
     * @param context The context that the view is in
     * @param attrs   The AttributeSet
     * @param group   The Group to display
     */
    public GroupListItem(Context context, AttributeSet attrs, Group group) {
        super(context, attrs);
        this.mGroup = group;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.group_list_item, this);
        if(mNameView == null) onFinishInflate(); // onFinishInflate isn't ever being called by the LayoutInflater for some reason
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mNameView = (TextView) findViewById(R.id.group_name);
        mTaskCountView = (TextView) findViewById(R.id.group_task_count);
        mNameView.setText(mGroup.getName());
        mNameView.setTypeface(TaskTimerApplication.Typefaces.ROBOTO_LIGHT);
        Log.v(TAG, "Inflated");
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = (Bundle) super.onSaveInstanceState();
        bundle.putParcelable("group", mGroup);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        mGroup = ((Bundle) state).getParcelable("group");
        invalidate();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        mNameView.setText(mGroup.getName());
    }

    @Override
    public void setChecked(boolean checked) {
        this.mChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        mChecked = !mChecked;
    }


    /**
     * Sets the Group
     * @param group The Group this list item is for
     */
    public void setGroup(Group group) {
        mGroup = group;
    }

    /**
     * Gets the Group
     * @return The Group this list item is for
     */
    public Group getGroup() {
        return mGroup;
    }
}
