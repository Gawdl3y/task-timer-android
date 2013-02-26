package com.gawdl3y.android.tasktimer.layout;

import android.app.Activity;
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
	 * The fragment is being attached to an activity
	 * @see com.actionbarsherlock.app.SherlockFragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		app = (TaskTimerApplication) activity.getApplication();
		
		if(app.debug) Log.v(TAG, "Attached");
	}
	
	/* (non-Javadoc)
	 * The view for the fragment is being created
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		adapter = new TaskListFragmentAdapter(getFragmentManager(), app);
        pager = (ViewPager) view.findViewById(R.id.pager);
		
        new SetAdapterTask().execute();
        
        if(app.debug) Log.v(TAG, "View created");
		return view;
	}

	/**
	 * @author Schuyler Cebulskie
	 * Sets the adapter of the ViewPager
	 */
	private class SetAdapterTask extends AsyncTask<Void, Void, Void> {
		protected Void doInBackground(Void... params) {
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			pager.setAdapter(adapter);
			if(app.debug) Log.v(TAG, "Set ViewPager adapter");
		}
	}
}
