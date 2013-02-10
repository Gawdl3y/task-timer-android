package com.gawdl3y.android.tasktimer.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.gawdl3y.android.tasktimer.MainActivity;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.adapters.TaskListFragmentAdapter;

public class MainFragment extends SherlockFragment {
	private static final String TAG = "MainFragment";
	
	private View view;
	private TaskListFragmentAdapter adapter;
	private ViewPager pager;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_main, container, false);
		adapter = new TaskListFragmentAdapter(getFragmentManager(), MainActivity.groups);
        pager = (ViewPager) view.findViewById(R.id.pager);
		
        new SetAdapterTask().execute();
        
        Log.v(TAG, "View created");
		return view;
	}

	private class SetAdapterTask extends AsyncTask<Void, Void, Void> {
		protected Void doInBackground(Void... params) {
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			pager.setAdapter(adapter);
			Log.v(TAG, "Set ViewPager adapter");
		}
	}
}
