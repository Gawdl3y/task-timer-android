package com.gawdl3y.android.tasktimer.fragments;

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
import com.gawdl3y.android.tasktimer.MainActivity;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.classes.Group;

public class GroupEditDialogFragment extends SherlockDialogFragment implements OnEditorActionListener {
	private Group group;
	private EditText nameView;
	private Spinner positionView;
	
	public interface GroupEditDialogListener {
		void onFinishEditDialog(Group group);
	}
	
	public GroupEditDialogFragment() {}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Load from arguments
		if(getArguments() != null) {
			Bundle args = getArguments();
			group = (Group) args.getSerializable("group");
		}
		
		// Load from saved instance state
		if(savedInstanceState != null) {
			group = (Group) savedInstanceState.getSerializable("group");
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Define the views
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(R.layout.fragment_group_edit, null);
		nameView = (EditText) view.findViewById(R.id.group_edit_name);
		positionView = (Spinner) view.findViewById(R.id.group_edit_position);
		
		// Add the possible positions to the spinner
		String[] opts = new String[MainActivity.groups.size() + 1];
		opts[MainActivity.groups.size()] = MainActivity.RES.getString(R.string.position_end);
		for(int i  = 0; i < MainActivity.groups.size(); i++)
			opts[i] = String.format(MainActivity.RES.getString(R.string.position_before), MainActivity.groups.get(i).getName());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, opts);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		positionView.setAdapter(adapter);
		
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
				.setPositiveButton(R.string.group_add, new DialogInterface.OnClickListener() {
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
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		
		// Save the data to the saved instance state
		if(group != null) savedInstanceState.putSerializable("group", group);
		savedInstanceState.putString("name", nameView.getText().toString());
		savedInstanceState.putInt("position", positionView.getSelectedItemPosition());
	}
	
	public static final GroupEditDialogFragment newInstance(Group group, int position) {
		// Create a new fragment
		GroupEditDialogFragment fragment = new GroupEditDialogFragment();
		
		// Set the arguments on the fragment
		Bundle args = new Bundle();
		if(group != null) args.putSerializable("group", group);
		args.putInt("position", position);
		fragment.setArguments(args);
		
		return fragment;
	}
}