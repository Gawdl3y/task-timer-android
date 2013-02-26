package com.gawdl3y.android.tasktimer.layout;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.TaskTimerApplication;
import com.gawdl3y.android.tasktimer.classes.Task;

/**
 * The dialog fragment for editing a Task
 * @author Schuyler Cebulskie
 */
public class TaskEditDialogFragment extends SherlockDialogFragment implements OnEditorActionListener {
	private TaskTimerApplication app;
	
	private Task task;
	private EditText nameView, descriptionView;
	private Spinner groupView, positionView;
	private ArrayAdapter<String> groupAdapter, positionAdapter;
	
	/**
	 * Interface used for listening for the changes to be completed on a Task
	 * @author Schuyler Cebulskie
	 */
	public interface TaskEditDialogListener {
		void onFinishEditDialog(Task task, int group);
	}
	
	/**
	 * Default constructor
	 */
	public TaskEditDialogFragment() {}
	
	/* (non-Javadoc)
	 * The fragment is being created
	 * @see android.support.v4.app.DialogFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (TaskTimerApplication) getActivity().getApplication();
		
		// Load from arguments
		if(getArguments() != null) {
			Bundle args = getArguments();
			task = (Task) args.getParcelable("task");
		}
		
		// Load from saved instance state
		if(savedInstanceState != null) {
			task = (Task) savedInstanceState.getParcelable("task");
		}
	}
	
	/* (non-Javadoc)
	 * The dialog is being created
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Define the views
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(R.layout.fragment_task_edit, null);
		nameView = (EditText) view.findViewById(R.id.task_edit_name);
		descriptionView = (EditText) view.findViewById(R.id.task_edit_description);
		groupView = (Spinner) view.findViewById(R.id.task_edit_group);
		positionView = (Spinner) view.findViewById(R.id.task_edit_position);
		
		// Add the possible groups to the group spinner
		String[] opts = new String[app.groups.size()];
		for(int i  = 0; i < app.groups.size(); i++) opts[i] = app.groups.get(i).getName();
		groupAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, opts);
		groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		groupView.setAdapter(groupAdapter);
		
		// Add a listener for the group spinner being changed
		groupView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				ArrayList<Task> tasks = app.groups.get(position).getTasks();
				String[] opts = new String[tasks.size() + 1];
				
				// Set the final item
				opts[tasks.size()] = app.resources.getString(R.string.position_end);
				
				// Add an item for each task
				for(int i  = 0; i < tasks.size(); i++)
					opts[i] = String.format(app.resources.getString(R.string.position_before), tasks.get(i).getName());
				
				// Set the adapter and stuff
				positionAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, opts);
				positionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				positionView.setAdapter(positionAdapter);
				positionView.setSelection(opts.length - 1);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// Nothing to see here
			}
		});
		
		// Load from arguments
		if(getArguments() != null) {
			Bundle args = getArguments();
			groupView.setSelection(args.getInt("group"));
			positionView.setSelection(args.getInt("position"));
		}
		
		// Load from saved instance state
		if(savedInstanceState != null) {
			nameView.setText(savedInstanceState.getString("name"));
			groupView.setSelection(savedInstanceState.getInt("group"));
			positionView.setSelection(savedInstanceState.getInt("position"));
			descriptionView.setText(savedInstanceState.getString("description"));
		}
		
		// Create the dialog
		return new AlertDialog.Builder(getActivity())
				.setTitle(task == null ? R.string.task_new : R.string.task_edit)
				.setView(view)
				.setCancelable(true)
				.setPositiveButton(task == null ? R.string.task_add : R.string.task_save, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						onEditorAction(null, EditorInfo.IME_ACTION_DONE, null);
					}
				})
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).create();
	}
	
	/* (non-Javadoc)
	 * An action is triggered by the user
	 * @see android.widget.TextView.OnEditorActionListener#onEditorAction(android.widget.TextView, int, android.view.KeyEvent)
	 */
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// Finished editing
		if(actionId == EditorInfo.IME_ACTION_DONE) {
			// Create the task
			if(task == null) task = new Task();
			task.setName(nameView.getText().toString());
			task.setDescription(descriptionView.getText().toString());
			task.setPosition(positionView.getSelectedItemPosition());
			
			// Return task to activity
			TaskEditDialogListener activity = (TaskEditDialogListener) getActivity();
			activity.onFinishEditDialog(task, groupView.getSelectedItemPosition());
			dismiss();
			return true;
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * The instance is being saved
	 * @see android.support.v4.app.DialogFragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		
		// Save the data to the saved instance state
		if(task != null) savedInstanceState.putParcelable("task", task);
		savedInstanceState.putString("name", nameView.getText().toString());
		savedInstanceState.putInt("group", groupView.getSelectedItemPosition());
		savedInstanceState.putInt("position", positionView.getSelectedItemPosition());
		savedInstanceState.putString("description", descriptionView.getText().toString());
	}
	
	/**
	 * Creates a new instance of TaskEditDialogFragment
	 * @param task The already-existing task, if any
	 * @param group The position of the group that the task is being edited in
	 * @return A new instance of the fragment
	 */
	public static final TaskEditDialogFragment newInstance(Task task, int group) {
		// Create a new fragment
		TaskEditDialogFragment fragment = new TaskEditDialogFragment();
		
		// Set the arguments on the fragment
		Bundle args = new Bundle();
		if(task != null) args.putParcelable("task", task);
		args.putInt("group", group);
		args.putInt("position", 0);
		fragment.setArguments(args);
		
		return fragment;
	}
}
