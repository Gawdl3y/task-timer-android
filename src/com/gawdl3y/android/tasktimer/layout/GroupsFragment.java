package com.gawdl3y.android.tasktimer.layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.TaskTimerApplication;
import com.gawdl3y.android.tasktimer.pojos.Group;
import com.gawdl3y.android.tasktimer.util.Log;

import java.util.ArrayList;

/**
 * The list of groups; contains a list using GroupListAdapter
 */
public class GroupsFragment extends Fragment {
    private static final String TAG = "GroupsFragment";

    // Data
    private ArrayList<Group> groups = TaskTimerApplication.GROUPS;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.v(TAG, "Created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        Log.v(TAG, "View created");
        return view;
    }

    public static GroupsFragment newInstance() {
        return new GroupsFragment();
    }
}
