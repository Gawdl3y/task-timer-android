package com.gawdl3y.android.tasktimer.layout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.gawdl3y.android.tasktimer.MainActivity;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.adapters.TaskAdapter;
import com.gawdl3y.android.tasktimer.classes.Group;

public class TaskListFragment extends SherlockListFragment {
	private static final String TAG = "TaskListFragment";
	
	public TaskAdapter adapter;
	public Group group;
	
	/* (non-Javadoc)
	 * The fragment is being created
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState != null) {
			group = (Group) savedInstanceState.getParcelable("group");
		} else {
			if(getArguments() != null) {
				group = (Group) getArguments().getParcelable("group");
			} else {
				group = new Group();
			}
		}
		
		if(MainActivity.DEBUG) Log.v(TAG, "Fragment created");
	}

	/* (non-Javadoc)
	 * The view for the fragment is being created
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_task_list, container, false);
		view.setTag(group.getPosition());
		
		adapter = new TaskAdapter(inflater.getContext(), group.getTasks(), group.getPosition());
		setListAdapter(adapter);
		
		if(MainActivity.DEBUG) Log.v(TAG, "View created");
		return view;
	}
	
	/* (non-Javadoc)
	 * The fragment is attached to the activity
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Toast.makeText(getActivity(), "Long press", Toast.LENGTH_SHORT).show();
				return true;
			}
		});
	}

	/* (non-Javadoc)
	 * A list item was clicked
	 * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Do stuff on click
		Toast.makeText(getActivity(), getListView().getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
	}
	
	/* (non-Javadoc)
	 * The instance is being saved
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("group", group);
    }
	
	/**
	 * Creates a new instance of the fragment
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