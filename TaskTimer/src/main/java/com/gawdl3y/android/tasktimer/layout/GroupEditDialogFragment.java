package com.gawdl3y.android.tasktimer.layout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.TaskTimerApplication;
import com.gawdl3y.android.tasktimer.pojos.Group;
import com.gawdl3y.android.tasktimer.pojos.Task;

import java.util.ArrayList;

/**
 * The dialog fragment for editing a Group
 * @author Schuyler Cebulskie
 */
public class GroupEditDialogFragment extends DialogFragment implements OnEditorActionListener {
    private ArrayList<Group> mGroups;
    private Group mGroup;
    private boolean mGroupIsNew;

    private EditText mNameView;
    private Spinner mPositionView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            // Load from saved instance
            mGroups = savedInstanceState.getParcelableArrayList("groups");
            mGroup = savedInstanceState.getParcelable("group");
        } else if(getArguments() != null) {
            // Load from arguments
            mGroups = getArguments().getParcelableArrayList("groups");
            mGroup = getArguments().getParcelable("group");
        }

        if(mGroup == null) mGroupIsNew = true;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Define the views
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_group_edit, null);
        mNameView = (EditText) view.findViewById(R.id.group_edit_name);
        mPositionView = (Spinner) view.findViewById(R.id.group_edit_position);

        // Add the possible positions to the spinner
        String[] opts = new String[mGroups.size() + 1];
        opts[0] = TaskTimerApplication.RESOURCES.getString(R.string.position_first);
        if(mGroups.size() > 0) opts[mGroups.size()] = TaskTimerApplication.RESOURCES.getString(R.string.position_last);
        for(int i = 1; i < mGroups.size(); i++)
            opts[i] = String.format(TaskTimerApplication.RESOURCES.getString(R.string.position_after), mGroups.get(i - 1).getName());
        ArrayAdapter<String> positionAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, opts);
        positionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPositionView.setAdapter(positionAdapter);

        // Set view stuff
        if(getArguments() != null) {
            mPositionView.setSelection(getArguments().getInt("position") == -1 ? 0 : getArguments().getInt("position"));
        }

        // Create the dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle(mGroup == null ? R.string.group_new : R.string.group_edit)
                .setView(view)
                .setCancelable(true)
                .setPositiveButton(mGroup == null ? R.string.group_add : R.string.group_save, new DialogInterface.OnClickListener() {
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
            // Create/modify the Group
            if(mGroup == null) mGroup = new Group();
            if(mGroup.getTasks() == null) mGroup.setTasks(new ArrayList<Task>());
            mGroup.setName(mNameView.getText().toString());
            mGroup.setPosition(mPositionView.getSelectedItemPosition());
            // TODO: fix reordering
            if(mGroupIsNew) TaskTimerApplication.addGroup(mGroup); else TaskTimerApplication.updateGroup(mGroup);
            dismiss();
            return true;
        }

        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Save the data to the saved instance state
        savedInstanceState.putParcelableArrayList("groups", mGroups);
        if(mGroup != null) savedInstanceState.putParcelable("group", mGroup);
    }


    /**
     * Creates a new instance of GroupEditDialogFragment
     * @param groups   The groups
     * @param position The initial position for the position spinner
     * @param group    The already-existing group, if any
     * @return A new instance of the fragment
     */
    public static GroupEditDialogFragment newInstance(ArrayList<Group> groups, int position, Group group) {
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