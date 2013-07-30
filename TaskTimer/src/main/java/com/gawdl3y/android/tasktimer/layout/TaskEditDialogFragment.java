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

    private ArrayList<Group> groups;
    private Task task;
    private boolean isNew;
    private TimeAmount editingTime, editingGoal;

    private EditText nameView, descriptionView;
    private Spinner groupView, positionView;
    private ArrayAdapter<String> groupAdapter, positionAdapter;
    private Button timeView, goalView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            // Load from saved instance
            groups = savedInstanceState.getParcelableArrayList("groups");
            task = savedInstanceState.getParcelable("task");
        } else if(getArguments() != null) {
            // Load from arguments
            groups = getArguments().getParcelableArrayList("groups");
            task = getArguments().getParcelable("task");
        }

        if(task == null) isNew = true;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Define the views
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_task_edit, null);
        nameView = (EditText) view.findViewById(R.id.task_edit_name);
        descriptionView = (EditText) view.findViewById(R.id.task_edit_description);
        groupView = (Spinner) view.findViewById(R.id.task_edit_group);
        positionView = (Spinner) view.findViewById(R.id.task_edit_position);
        timeView = (Button) view.findViewById(R.id.task_edit_time);
        goalView = (Button) view.findViewById(R.id.task_edit_goal);

        // Add the possible groups to the group spinner
        String[] opts = new String[groups.size()];
        for(int i = 0; i < groups.size(); i++) opts[i] = groups.get(i).getName();
        groupAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, opts);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupView.setAdapter(groupAdapter);

        // Add a listener for the group spinner being changed
        groupView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ArrayList<Task> tasks = groups.get(position).getTasks();
                String[] opts = new String[tasks.size() + 1];

                // Set the first and final items
                opts[0] = TaskTimerApplication.RESOURCES.getString(R.string.position_first);
                if(tasks.size() > 0) opts[tasks.size()] = TaskTimerApplication.RESOURCES.getString(R.string.position_last);

                // Add an item for each task
                for(int i = 1; i < tasks.size(); i++)
                    opts[i] = String.format(TaskTimerApplication.RESOURCES.getString(R.string.position_after), tasks.get(i - 1).getName());

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

        // Set the time/goal button listeners
        timeView.setOnClickListener(new View.OnClickListener() {
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
        goalView.setOnClickListener(new View.OnClickListener() {
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
            editingTime = savedInstanceState.getParcelable("editingTime");
            editingGoal = savedInstanceState.getParcelable("editingGoal");
        } else if(task != null) {
            editingTime = task.getTime();
            editingGoal = task.getGoal();
        } else {
            editingTime = new TimeAmount(0, 0, 0);
            editingGoal = new TimeAmount(4, 0, 0);
        }
        timeView.setText(editingTime.toString());
        goalView.setText(editingGoal.toString());

        // Set view stuff
        if(getArguments() != null) {
            groupView.setSelection(getArguments().getInt("group"));
            positionView.setSelection(getArguments().getInt("position"));
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

    @Override
    public void onDialogHmsSet(int reference, int hours, int minutes, int seconds) {
        TimeAmount time = new TimeAmount(hours, minutes, seconds);
        time.distribute();
        if(reference == HMS_REFERENCE_TIME) {
            timeView.setText(time.toString());
            editingTime = time;
        } else if(reference == HMS_REFERENCE_GOAL) {
            goalView.setText(time.toString());
            editingGoal = time;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // Finished editing
        if(actionId == EditorInfo.IME_ACTION_DONE) {
            // Create the task
            if(task == null) task = new Task();
            task.setName(nameView.getText().toString());
            task.setDescription(descriptionView.getText().toString());
            task.setPosition(positionView.getSelectedItemPosition());
            int groupPosition = groupView.getSelectedItemPosition();
            task.setTime(editingTime);
            task.setGoal(editingGoal);
            // TODO: fix reordering
            if(isNew) TaskTimerApplication.addTask(groupPosition, task); else TaskTimerApplication.updateTask(groupPosition, task);
            dismiss();
            return true;
        }

        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("groups", groups);
        if(task != null) savedInstanceState.putParcelable("task", task);
        savedInstanceState.putParcelable("editingTime", editingTime);
        savedInstanceState.putParcelable("editingGoal", editingGoal);
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
