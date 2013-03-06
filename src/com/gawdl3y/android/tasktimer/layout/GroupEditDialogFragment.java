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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.TaskTimerApplication;
import com.gawdl3y.android.tasktimer.classes.Group;

/**
 * The dialog fragment for editing a Group
 * @author Schuyler Cebulskie
 */
public class GroupEditDialogFragment extends SherlockDialogFragment implements OnEditorActionListener {
	private TaskTimerApplication app;
	
	private ArrayList<Group> groups;
	private Group group;
	
	private EditText nameView;
	private Spinner positionView;
	
	/**
	 * Interface used for listening for the changes to be completed on a Group
	 * @author Schuyler Cebulskie
	 */
	public interface GroupEditDialogListener {
		void onFinishEditDialog(Group group);
	}
	
	/* (non-Javadoc)
	 * The fragment is being created
	 * @see android.support.v4.app.DialogFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (TaskTimerApplication) getActivity().getApplication();
		
		if(savedInstanceState != null) {
			// Load from saved instance
			groups = savedInstanceState.getParcelableArrayList("groups");
			group = (Group) savedInstanceState.getParcelable("group");
		} else if(getArguments() != null) {
			// Load from arguments
			groups = getArguments().getParcelableArrayList("groups");
			group = (Group) getArguments().getParcelable("group");
		}
		
		// Load from saved instance state
	}
	
	/* (non-Javadoc)
	 * The dialog is being created
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Define the views
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(R.layout.fragment_group_edit, null);
		nameView = (EditText) view.findViewById(R.id.group_edit_name);
		positionView = (Spinner) view.findViewById(R.id.group_edit_position);
		
		// Add the possible positions to the spinner
		String[] opts = new String[groups.size() + 1];
		opts[groups.size()] = app.resources.getString(R.string.position_end);
		for(int i  = 0; i < groups.size(); i++)
			opts[i] = String.format(app.resources.getString(R.string.position_before), groups.get(i).getName());
		ArrayAdapter<String> positionAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, opts);
		positionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		positionView.setAdapter(positionAdapter);
		
		// Load from arguments
		if(getArguments() != null) {
			Bundle args = getArguments();
			positionView.setSelection(args.getInt("position") == -1 ? 0 : args.getInt("position"));
		}
		
		// Load from saved instance state
		if(savedInstanceState != null) {
			nameView.setText(savedInstanceState.getString("name"));
			positionView.setSelection(savedInstanceState.getInt("position"));
		}
		
		// Create the dialog
		return new AlertDialog.Builder(getActivity())
				.setTitle(group == null ? R.string.group_new : R.string.group_edit)
				.setView(view)
				.setCancelable(true)
				.setPositiveButton(group == null ? R.string.group_add : R.string.group_save, new DialogInterface.OnClickListener() {
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
		if(actionId == EditorInfo.IME_ACTION_DONE) {
			// Create the Group
			if(group == null) group = new Group();
			group.setName(nameView.getText().toString());
			group.setPosition(positionView.getSelectedItemPosition());
			
			// Return group to activity
			GroupEditDialogListener activity = (GroupEditDialogListener) getActivity();
			activity.onFinishEditDialog(group);
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
		savedInstanceState.putParcelableArrayList("groups", groups);
		savedInstanceState.putString("name", nameView.getText().toString());
		savedInstanceState.putInt("position", positionView.getSelectedItemPosition());
		if(group != null) savedInstanceState.putParcelable("group", group);
	}
	
	
	/**
	 * Creates a new instance of GroupEditDialogFragment
	 * @param groups The groups
	 * @param position The initial position for the position spinner
	 * @param group The already-existing group, if any
	 * @return A new instance of the fragment
	 */
	public static final GroupEditDialogFragment newInstance(ArrayList<Group> groups, int position, Group group) {
		// Create the arguments for the fragment
		Bundle args = new Bundle();
		args.putParcelableArrayList("groups", groups);
		args.putInt("position", position);
		if(group != null) args.putParcelable("group", group);
		
		// Create the fragment
		GroupEditDialogFragment fragment = new GroupEditDialogFragment();
		fragment.setArguments(args);
		return fragment;
	}
}