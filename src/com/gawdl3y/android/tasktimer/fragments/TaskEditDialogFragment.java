package com.gawdl3y.android.tasktimer.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.classes.Task;

public class TaskEditDialogFragment extends SherlockDialogFragment implements OnEditorActionListener {
	private Task task;
	private EditText nameView;
	
	public interface TaskEditDialogListener {
		void onFinishEditDialog(Task task);
	}
	
	public TaskEditDialogFragment() {}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState != null) {
			task = (Task) savedInstanceState.getSerializable("task");
		}
		
		if(getArguments() != null) {
			task = (Task) getArguments().getSerializable("task");
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(R.layout.fragment_task_edit, null);
		nameView = (EditText) view.findViewById(R.id.task_edit_name);
		
		if(savedInstanceState != null) {
			nameView.setText(savedInstanceState.getString("name"));
		}
		
		return new AlertDialog.Builder(getActivity())
				.setTitle(task == null ? R.string.task_new : R.string.task_edit)
				.setView(view)
				.setCancelable(true)
				.setPositiveButton(R.string.task_add,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// validation code
							}
						})
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						}).create();
	}
	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if(actionId == EditorInfo.IME_ACTION_DONE) {
			// Return task to activity
			TaskEditDialogListener activity = (TaskEditDialogListener) getActivity();
			activity.onFinishEditDialog(new Task());
			this.dismiss();
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		
		if(task != null) savedInstanceState.putSerializable("task", task);
		savedInstanceState.putString("name", nameView.getText().toString());
	}
	
	public static final TaskEditDialogFragment newInstance(Task task) {
		TaskEditDialogFragment fragment = new TaskEditDialogFragment();
		
		if(task != null) {
			Bundle args = new Bundle();
			args.putSerializable("task", task);
			fragment.setArguments(args);
		}
		
		return fragment;
	}
}
