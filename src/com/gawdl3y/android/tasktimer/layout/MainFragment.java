package com.gawdl3y.android.tasktimer.layout;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.TaskTimerApplication;
import com.gawdl3y.android.tasktimer.adapters.TaskListFragmentAdapter;
import com.gawdl3y.android.tasktimer.classes.Group;

/**
 * The main fragment for Task Timer; contains a TaskListFragmentAdapter
 * @author Schuyler Cebulskie
 */
public class MainFragment extends SherlockFragment {
	private static final String TAG = "MainFragment";
	
	private ArrayList<Group> groups;
	
	public TaskListFragmentAdapter adapter;
	public ViewPager pager;
	
	/* (non-Javadoc)
	 * The fragment is being created
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState != null) {
			// Load from saved instance
			groups = savedInstanceState.getParcelableArrayList("groups");
		} else if(getArguments() != null) {
			// Load from arguments
			groups = getArguments().getParcelableArrayList("groups");
		}
	}
	
	/* (non-Javadoc)
	 * The view for the fragment is being created
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		adapter = new TaskListFragmentAdapter(getFragmentManager(), groups);
        pager = (ViewPager) view.findViewById(R.id.pager);
		
        new SetAdapterTask().execute();
        
        if(TaskTimerApplication.DEBUG) Log.v(TAG, "View created");
		return view;
	}
	
	/* (non-Javadoc)
	 * The instance of the fragment is being saved
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putParcelableArrayList("groups", groups);
	}

	/**
	 * Sets the adapter of the ViewPager
	 * @author Schuyler Cebulskie
	 */
	private class SetAdapterTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			pager.setAdapter(adapter);
			if(TaskTimerApplication.DEBUG) Log.v(TAG, "Set ViewPager adapter");
		}
	}
	
	
	/**
	 * Creates a new instance of MainFragment
	 * @param app The application
	 * @return A new instance of MainFragment
	 */
	public static final MainFragment newInstance(ArrayList<Group> groups) {
		// Create the arguments for the fragment
		Bundle args = new Bundle();
		args.putParcelableArrayList("groups", groups);
		
		// Create the fragment
		MainFragment fragment = new MainFragment();
		fragment.setArguments(args);
		return fragment;
	}
}
