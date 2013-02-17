package com.gawdl3y.android.tasktimer.fragments;

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
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.adapters.TaskAdapter;
import com.gawdl3y.android.tasktimer.classes.Group;

public class TaskListFragment extends SherlockListFragment {
	private static final String TAG = "TaskListFragment";
	
	public TaskAdapter adapter;
	public Group group;
	
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
		
		Log.v(TAG, "Fragment created");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_task_list, container, false);
		view.setTag(group.getPosition());
		
		adapter = new TaskAdapter(inflater.getContext(), group.getTasks(), group.getPosition());
		setListAdapter(adapter);
		
		Log.v(TAG, "View created");
		return view;
	}
	
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);

		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Toast.makeText(getActivity(), "Long press", Toast.LENGTH_SHORT).show();
				return true;
			}
		});
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Do stuff on click
		Toast.makeText(getActivity(), getListView().getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("group", group);
    }
	
	public static TaskListFragment newInstance(Group group) {
		TaskListFragment fragment = new TaskListFragment();
		Bundle args = new Bundle();
		args.putParcelable("group", group);
		fragment.setArguments(args);

		return fragment;
	}
}