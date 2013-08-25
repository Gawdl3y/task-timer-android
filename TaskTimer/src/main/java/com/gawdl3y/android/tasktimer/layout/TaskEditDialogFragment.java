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
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView.OnEditorActionListener;
import com.doomonafireball.betterpickers.hmspicker.HmsPickerBuilder;
import com.doomonafireball.betterpickers.hmspicker.HmsPickerDialogFragment;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.TaskTimerApplication;
import com.gawdl3y.android.tasktimer.pojos.Group;
import com.gawdl3y.android.tasktimer.pojos.Task;
import com.gawdl3y.android.tasktimer.pojos.TimeAmount;

import java.util.ArrayList;

/**
 * The dialog fragment for editing a Task
 * @author Schuyler Cebulskie
 */
public class TaskEditDialogFragment extends DialogFragment implements OnEditorActionListener, HmsPickerDialogFragment.HmsPickerDialogHandler {
    private static final int HMS_REFERENCE_TIME = 1;
    private static final int HMS_REFERENCE_GOAL = 2;

    private ArrayList<Group> mGroups;
    private Task mTask;
    private boolean mTaskIsNew;
    private TimeAmount mEditingTime, mEditingGoal;

    private EditText mNameView, mDescriptionView;
    private Spinner mGroupView, mPositionView;
    private ArrayAdapter<String> mGroupAdapter, mPositionAdapter;
    private Button mTimeView, mGoalView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            // Load from saved instance
            mGroups = savedInstanceState.getParcelableArrayList("groups");
            mTask = savedInstanceState.getParcelable("task");
        } else if(getArguments() != null) {
            // Load from arguments
            mGroups = getArguments().getParcelableArrayList("groups");
            mTask = getArguments().getParcelable("task");
        }

        if(mTask == null) mTaskIsNew = true;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Define the views
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_task_edit, null);
        mNameView = (EditText) view.findViewById(R.id.task_edit_name);
        mDescriptionView = (EditText) view.findViewById(R.id.task_edit_description);
        mGroupView = (Spinner) view.findViewById(R.id.task_edit_group);
        mPositionView = (Spinner) view.findViewById(R.id.task_edit_position);
        mTimeView = (Button) view.findViewById(R.id.task_edit_time);
        mGoalView = (Button) view.findViewById(R.id.task_edit_goal);

        // Add the possible groups to the group spinner
        String[] opts = new String[mGroups.size()];
        for(int i = 0; i < mGroups.size(); i++) opts[i] = mGroups.get(i).getName();
        mGroupAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, opts);
        mGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGroupView.setAdapter(mGroupAdapter);

        // Add a listener for the group spinner being changed
        mGroupView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ArrayList<Task> tasks = mGroups.get(position).getTasks();
                String[] opts = new String[tasks.size() + 1];

                // Set the first and final items
                opts[0] = TaskTimerApplication.RESOURCES.getString(R.string.position_first);
                if(tasks.size() > 0)
                    opts[tasks.size()] = TaskTimerApplication.RESOURCES.getString(R.string.position_last);

                // Add an item for each task
                for(int i = 1; i < tasks.size(); i++)
                    opts[i] = String.format(TaskTimerApplication.RESOURCES.getString(R.string.position_after), tasks.get(i - 1).getName());

                // Set the adapter and stuff
                mPositionAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, opts);
                mPositionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mPositionView.setAdapter(mPositionAdapter);
                mPositionView.setSelection(opts.length - 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Nothing to see here
            }
        });

        // Set the time/goal button listeners
        mTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HmsPickerBuilder timePicker = new HmsPickerBuilder()
                    .setFragmentManager(getFragmentManager())
                    .setStyleResId(TaskTimerApplication.THEME == R.style.Theme_Dark ? R.style.BetterPickersDialogFragment : R.style.BetterPickersDialogFragment_Light)
                    .addHmsPickerDialogHandler(TaskEditDialogFragment.this)
                    .setReference(HMS_REFERENCE_TIME);
                timePicker.show();
            }
        });
        mGoalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HmsPickerBuilder timePicker = new HmsPickerBuilder()
                    .setFragmentManager(getFragmentManager())
                    .setStyleResId(TaskTimerApplication.THEME == R.style.Theme_Dark ? R.style.BetterPickersDialogFragment : R.style.BetterPickersDialogFragment_Light)
                    .addHmsPickerDialogHandler(TaskEditDialogFragment.this)
                    .setReference(HMS_REFERENCE_GOAL);
                timePicker.show();
            }
        });

        // Update the time/goal buttons
        if(savedInstanceState != null) {
            mEditingTime = savedInstanceState.getParcelable("editingTime");
            mEditingGoal = savedInstanceState.getParcelable("mEditingGoal");
        } else if(mTask != null) {
            mEditingTime = mTask.getTime();
            mEditingGoal = mTask.getGoal();
        } else {
            mEditingTime = new TimeAmount(0, 0, 0);
            mEditingGoal = new TimeAmount(4, 0, 0);
        }
        mTimeView.setText(mEditingTime.toString());
        mGoalView.setText(mEditingGoal.toString());

        // Set view stuff
        if(getArguments() != null) {
            mGroupView.setSelection(getArguments().getInt("group"));
            mPositionView.setSelection(getArguments().getInt("position"));
        }

        // Create the dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle(mTask == null ? R.string.task_new : R.string.task_edit)
                .setView(view)
                .setCancelable(true)
                .setPositiveButton(mTask == null ? R.string.task_add : R.string.task_save, new DialogInterface.OnClickListener() {
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
    public void onDialogHmsSet(int reference, int hours, int minutes, int seconds) {
        TimeAmount time = new TimeAmount(hours, minutes, seconds);
        time.distribute();
        if(reference == HMS_REFERENCE_TIME) {
            mTimeView.setText(time.toString());
            mEditingTime = time;
        } else if(reference == HMS_REFERENCE_GOAL) {
            mGoalView.setText(time.toString());
            mEditingGoal = time;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // Finished editing
        if(actionId == EditorInfo.IME_ACTION_DONE) {
            // Create the task
            if(mTask == null) mTask = new Task();
            mTask.setName(mNameView.getText().toString());
            mTask.setDescription(mDescriptionView.getText().toString());
            mTask.setPosition(mPositionView.getSelectedItemPosition());
            int groupPosition = mGroupView.getSelectedItemPosition();
            mTask.setTime(mEditingTime);
            mTask.setGoal(mEditingGoal);
            // TODO: fix reordering
            if(mTaskIsNew) TaskTimerApplication.addTask(groupPosition, mTask); else TaskTimerApplication.updateTask(groupPosition, mTask);
            dismiss();
            return true;
        }

        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("groups", mGroups);
        if(mTask != null) savedInstanceState.putParcelable("task", mTask);
        savedInstanceState.putParcelable("editingTime", mEditingTime);
        savedInstanceState.putParcelable("mEditingGoal", mEditingGoal);
    }


    /**
     * Creates a new instance of TaskEditDialogFragment
     * @param groups The groups
     * @param group  The position of the group that the task is being edited in
     * @param task   The already-existing task, if any
     * @return A new instance of the fragment
     */
    public static TaskEditDialogFragment newInstance(ArrayList<Group> groups, int group, Task task) {
        // Create the arguments for the fragment
        Bundle args = new Bundle();
        args.putParcelableArrayList("groups", groups);
        args.putInt("group", group);
        args.putInt("position", 0);
        if(task != null) args.putParcelable("task", task);

        // Create the fragment
        TaskEditDialogFragment fragment = new TaskEditDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
