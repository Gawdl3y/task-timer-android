package com.gawdl3y.android.tasktimer.layout;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.adapters.TaskListAdapter;
import com.gawdl3y.android.tasktimer.pojos.Group;
import com.gawdl3y.android.tasktimer.util.Log;

/**
 * The fragment for displaying a list of Tasks
 * @author Schuyler Cebulskie
 */
public class TaskListFragment extends ListFragment {
    private static final String TAG = "TaskListFragment";

    private Group group;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tell the fragment to retain its instance across configuration changes
        setRetainInstance(true);

        // Restore data
        if(savedInstanceState != null) {
            group = savedInstanceState.getParcelable("group");
        } else {
            if(getArguments() != null) {
                group = getArguments().getParcelable("group");
            } else {
                group = new Group();
            }
        }

        Log.v(TAG, "Fragment created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        view.setTag(group.getPosition());

        setListAdapter(new TaskListAdapter(inflater.getContext(), group.getTasks(), group.getPosition()));

        Log.v(TAG, "View created");
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                onListItemLongClick(getListView(), view, position, id);
                return true;
            }
        });
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
        // Toggle the item if we've already toggled others
        if(list.getCheckedItemCount() > 0) {
            TaskListItem item = (TaskListItem) view;
            item.toggle();
            list.setItemChecked(position, item.isChecked());
            Toast.makeText(getActivity(), Integer.toString(list.getCheckedItemCount()), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), getListView().getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
        }

        Log.d(TAG, Integer.toString(list.getCheckedItemCount()));
    }

    /**
     * A list item was long-clicked
     * @param list     The ListView that the item is in
     * @param view     The view of the item
     * @param position The position of the item
     * @param id       The ID of the item
     */
    public void onListItemLongClick(ListView list, View view, int position, long id) {
        TaskListItem item = (TaskListItem) view;
        item.toggle();
        list.setItemChecked(position, item.isChecked());
        Toast.makeText(getActivity(), Integer.toString(getListView().getCheckedItemCount()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("group", group);
    }


    /**
     * Sets the Group that the fragment is displaying
     * @param group The Group
     */
    public void setGroup(Group group) {
        this.group = group;
    }

    /**
     * Gets the Group that the fragment is displaying
     * @return The Group
     */
    public Group getGroup() {
        return group;
    }


    /**
     * Creates a new instance of TaskListFragment
     * @param group The group the fragment is for
     * @return A new instance of the fragment
     */
    public static TaskListFragment newInstance(Group group) {
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        args.putParcelable("group", group);
        fragment.setArguments(args);

        return fragment;
    }
}