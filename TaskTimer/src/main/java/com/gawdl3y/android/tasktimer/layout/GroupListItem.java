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
    private Group group;
    private boolean checked = false;

    // Views
    private TextView name, taskCount;

    /**
     * Constructor
     * @param context The context that the view is in
     * @param attrs   The AttributeSet
     * @param group   The Group to display
     */
    public GroupListItem(Context context, AttributeSet attrs, Group group) {
        super(context, attrs);
        this.group = group;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.group_list_item, this);
        if(name == null) onFinishInflate(); // onFinishInflate isn't ever being called by the LayoutInflater for some reason
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        name = (TextView) findViewById(R.id.group_name);
        taskCount = (TextView) findViewById(R.id.group_task_count);
        name.setText(group.getName());
        name.setTypeface(TaskTimerApplication.Typefaces.ROBOTO_LIGHT);
        Log.v(TAG, "Inflated");
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = (Bundle) super.onSaveInstanceState();
        bundle.putParcelable("group", group);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        group = ((Bundle) state).getParcelable("group");
        invalidate();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        name.setText(group.getName());
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        checked = !checked;
    }


    /**
     * Sets the Group
     * @param group The Group this list item is for
     */
    public void setGroup(Group group) {
        this.group = group;
    }

    /**
     * Gets the Group
     * @return The Group this list item is for
     */
    public Group getGroup() {
        return group;
    }
}
