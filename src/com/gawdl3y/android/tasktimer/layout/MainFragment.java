package com.gawdl3y.android.tasktimer.layout;

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

/**
 * The main fragment for Task Timer; contains a TaskListFragmentAdapter
 * @author Schuyler Cebulskie
 */
public class MainFragment extends SherlockFragment {
	private static final String TAG = "MainFragment";
	
	private TaskTimerApplication app;
	
	public TaskListFragmentAdapter adapter;
	public ViewPager pager;
	
	/* (non-Javadoc)
	 * The view for the fragment is being created
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		adapter = new TaskListFragmentAdapter(getFragmentManager(), app.groups);
        pager = (ViewPager) view.findViewById(R.id.pager);
		
        new SetAdapterTask().execute();
        
        if(app.debug) Log.v(TAG, "View created");
		return view;
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
			if(app.debug) Log.v(TAG, "Set ViewPager adapter");
		}
	}
	
	
	/**
	 * Creates a new instance of MainFragment
	 * @param app The application
	 * @return A new instance of MainFragment
	 */
	public static final MainFragment newInstance(TaskTimerApplication app) {
		MainFragment fragment = new MainFragment();
		fragment.app = app;
		return fragment;
	}
}
