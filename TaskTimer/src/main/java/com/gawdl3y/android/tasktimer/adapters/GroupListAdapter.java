package com.gawdl3y.android.tasktimer.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.gawdl3y.android.actionablelistview.ActionableAdapter;
import com.gawdl3y.android.tasktimer.TaskTimerApplication;
import com.gawdl3y.android.tasktimer.layout.GroupListItem;
import com.gawdl3y.android.tasktimer.pojos.Group;
import com.gawdl3y.android.tasktimer.util.Log;

import java.util.List;

/**
 * The adapter to display a list of Groups
 * @author Schuyler Cebulskie
 */
public class GroupListAdapter extends ActionableAdapter {
    private static final String TAG = "GroupListAdapter";
    public static final int ACTION_DELETE = 0;
    public static final int ACTION_ARCHIVE = 1;

    private Context mContext;
    private List<Group> mGroups;

    /**
     * Fill constructor
     * @param context The context of the adapter
     */
    public GroupListAdapter(Context context, List<Group> groups) {
        mContext = context;
        mGroups = groups;
    }

    @Override
    public int getCount() {
        return mGroups.size();
    }

    @Override
    public Object getItem(int position) {
        Log.v(TAG, "Getting item #" + position);
        return mGroups.get(position);
    }

    @Override
    public long getItemId(int position) {
        if(position >= 0 && position < mGroups.size()) return mGroups.get(position).getId();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroupListItem v;
        if(convertView != null && convertView instanceof GroupListItem) {
            Log.v(TAG, "Converting old view");
            v = (GroupListItem) convertView;
            v.setTag(position);
            v.setGroup((Group) getItem(position));
            v.setChecked(isItemChecked(position));
            v.invalidate();
        } else {
            Log.v(TAG, "Getting new view");
            v = new GroupListItem(mContext, null, (Group) getItem(position));
            v.setTag(position);
            v.setChecked(isItemChecked(position));
        }

        return v;
    }

    @Override
    public boolean performActionOnItem(int actionType, int position) {
        Toast.makeText(mContext, actionType + " " + position, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
