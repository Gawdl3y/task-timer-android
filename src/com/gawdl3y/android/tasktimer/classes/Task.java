package com.gawdl3y.android.tasktimer.classes;

import java.io.Serializable;
import java.util.Comparator;

import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gawdl3y.android.tasktimer.MainActivity;
import com.gawdl3y.android.tasktimer.R;

/**
 * @author Schuyler Cebulskie
 * A class that contains a time, goal time, and task-related properties
 */
public class Task implements Serializable, Parcelable {
	private static final long serialVersionUID = -5638162774940783076L;
	
	private String name, description;
	private TimeAmount time, goal;
	private boolean indefinite, complete, running, stopAtGoal;
	private int id, position, group;
	
	/**
	 * Default constructor
	 */
	public Task() {
		this("EMPTY NAME", "", new TimeAmount(), new TimeAmount(), false, false, false, false, -1, -1, -1);
	}

	/**
	 * Fill constructor
	 * @param name The name of the Task
	 * @param time The time of the Task
	 * @param goal the goal time of the Task
	 * @param indefinite Whether or not the Task's goal is indefinite
	 * @param complete Whether or not the Task is completed
	 * @param running Whether or not the Task is running
	 * @param stopAtGoal Whether or not to stop the Task when it hits its goal
	 * @param id The ID of the Task
	 * @param position The position of the Task in the array/ViewList
	 * @param group The ID of the group that the task is in
	 */
	public Task(String name, String description, TimeAmount time, TimeAmount goal, boolean indefinite, boolean complete, boolean running, boolean stopAtGoal, int id, int position, int group) {
		this.name = name;
		this.description = description;
		this.time = time;
		this.goal = goal;
		this.indefinite = indefinite;
		this.complete = complete;
		this.running = running;
		this.stopAtGoal = stopAtGoal;
		this.id = id;
		this.position = position;
		this.group = group;
	}
	
	/**
	 * Parcel constructor
	 * @param parcel The parcel to read from
	 */
	public Task(Parcel parcel) {
		readFromParcel(parcel);
	}
	

	/**
	 * Gets the name of the Task
	 * @return The name of the Task
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the Task
	 * @param name The name of the Task
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the description of the Task
	 * @return The description of the Task
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the Task
	 * @param description The description of the Task
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets the time of the Task
	 * @return The time of the Task
	 */
	public TimeAmount getTime() {
		return time;
	}
	
	/**
	 * Sets the time of the Task
	 * @param time The time of the Task
	 */
	public void setTime(TimeAmount time) {
		this.time = time;
	}
	
	/**
	 * Sets the time of the Task
	 * @param hours The hours of the time
	 * @param mins The minutes of the time
	 * @param secs The seconds of the time
	 */
	public void setTime(int hours, int mins, int secs) {
		time.setHours(hours);
		time.setMins(mins);
		time.setSecs(secs);
	}
	
	/**
	 * Gets the goal time of the Task
	 * @return The goal time of the Task
	 */
	public TimeAmount getGoal() {
		return goal;
	}
	
	/**
	 * Sets the goal time of the Task
	 * @param goal The goal time of the Task
	 */
	public void setGoal(TimeAmount goal) {
		this.goal = goal;
	}
	
	/**
	 * Sets the goal time of the Task
	 * @param hours The hours of the goal time
	 * @param mins The minutes of the goal time
	 */
	public void setGoal(int hours, int mins) {
		goal.setHours(hours);
		goal.setMins(mins);
	}
	
	/**
	 * Gets whether or not the Task's goal is indefinite
	 * @return Whether or not the Task's goal is indefinite
	 */
	public boolean isIndefinite() {
		return indefinite;
	}
	
	/**
	 * Sets whether or not the Task's goal is indefinite
	 * @param indefinite Whether or not the Task's goal is indefinite
	 */
	public void setIndefinite(boolean indefinite) {
		this.indefinite = indefinite;
	}
	
	/**
	 * Gets whether or not the Task is completed
	 * @return Whether or not the Task is completed
	 */
	public boolean isComplete() {
		return complete;
	}
	
	/**
	 * Sets whether or not the Task is completed
	 * @param complete Whether or not the Task is completed
	 */
	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	
	/**
	 * Gets whether or not the Task is running
	 * @return Whether or not the Task is running
	 */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * Sets whether or not the Task is running
	 * @param running Whether or not the Task is running
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	/**
	 * Gets whether or not the Task will stop when its goal is reached
	 * @return Whether or not the Task will stop when its goal is reached
	 */
	public boolean getStopAtGoal() {
		return stopAtGoal;
	}
	
	/**
	 * Sets whether or not the Task will stop when its goal is reached
	 * @param stopAtGoal Whether or not the Task will stop when its goal is reached
	 */
	public void setStopAtGoal(boolean stopAtGoal) {
		this.stopAtGoal = stopAtGoal;
	}
	

	/**
	 * Gets the group of the Task
	 * @return The category of the Task
	 */
	public int getGroup() {
		return group;
	}

	/**
	 * Sets the group of the Task
	 * @param category The category of the Task
	 */
	public void setGroup(int category) {
		this.group = category;
	}
	
	/**
	 * Gets the ID of the Task
	 * @return The ID of the Task
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Sets the ID of the Task
	 * @param id The ID of the Task
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the position of the Task in the array/ListView
	 * @return The position of the Task in the array/ListView
	 */
	public int getPosition() {
		return position;
	}
	
	/**
	 * Sets the position of the Task in the array/ListView
	 * @param position The position of the Task in the array/ListView
	 */
	public void setPosition(int position) {
		this.position = position;
	}
	
	/**
	 * Gets the progress of the Task
	 * @return progress The progress of the Task (0 to 100)
	 */
	public int getProgress() {
		if(indefinite) return 0;
		try {
			return (int) Math.floor(time.toDouble() / goal.toDouble() * 100);
		} catch(ArithmeticException e) {
			return 100;
		}
	}
	
	/**
	 * Toggles the running status of the Task
	 */
	public void toggle() {
		running = !running;
	}
	
	/**
	 * Increments the Task's time by 1 second
	 */
	public void incrementTime() {
		incrementTime(1);
	}
	
	/**
	 * Increments the Task's time
	 * @param secs The seconds to increment the time by
	 */
	public void incrementTime(int secs) {
		time.increment(secs);
		if(time.compareTo(goal) >= 0) {
			complete = true;
			
			if(stopAtGoal) {
				// Stop the task and update the entire view
				running = false;
			}
		}
	}
	
	/**
	 * Updates the view of a Task
	 * @param task The Task to update the view of
	 * @param view The view of the task
	 */
	public static void updateView(Task task, View view) {
		TextView nameView = (TextView) view.findViewById(R.id.task_name);
		TextView timeView = (TextView) view.findViewById(R.id.task_time);
		TextView goalView = (TextView) view.findViewById(R.id.task_goal);
		ProgressBar progressView = (ProgressBar) view.findViewById(R.id.task_progress);
		ImageView toggleView = (ImageView) view.findViewById(R.id.task_toggle);

		// Text views
		nameView.setText(task.getName());
		timeView.setText(task.getTime().toString());
		goalView.setText(task.isIndefinite() ? MainActivity.RES.getString(R.string.task_indefinite) : task.getGoal().toString());

		// Progress bar
		progressView.setIndeterminate(task.isIndefinite() && task.isRunning());
		progressView.setProgress(task.getProgress());

		// Change the toggle button to the proper image
		TypedArray ta = view.getContext().obtainStyledAttributes(new int[] { task.isRunning() ? R.attr.ic_pause : R.attr.ic_start });
		toggleView.setImageDrawable(ta.getDrawable(0));
		ta.recycle();

		// Set tags so we can figure out what task is being acted upon later
		view.setTag(task.getPosition());
		toggleView.setTag(task.getPosition());
	}
	
	
	/**
	 * Tests to see if the provided object is the same Task as this one (using IDs)
	 * @param obj The object to compare to
	 * @return Whether or not the object is the same
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		return id == ((Task) obj).getId();
	}
	
	
	/* (non-Javadoc)
	 * Describe the contents for the parcel
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	/* (non-Javadoc)
	 * Write the Task to a parcel
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(description);
		dest.writeParcelable(time, flags);
		dest.writeParcelable(goal, flags);
		dest.writeByte((byte) (indefinite ? 1 : 0));
		dest.writeByte((byte) (complete ? 1 : 0));
		dest.writeByte((byte) (running ? 1 : 0));
		dest.writeByte((byte) (stopAtGoal ? 1 : 0));
		dest.writeInt(id);
		dest.writeInt(position);
		dest.writeInt(group);
	}
	
	/**
	 * Fills the Task from a parcel
	 * @param in The parcel to read from
	 */
	private void readFromParcel(Parcel in) {
		name = in.readString();
		description = in.readString();
		time = (TimeAmount) in.readParcelable(TimeAmount.class.getClassLoader());
		goal = (TimeAmount) in.readParcelable(TimeAmount.class.getClassLoader());
		indefinite = in.readByte() == 1 ? true : false;
		complete = in.readByte() == 1 ? true : false;
		running = in.readByte() == 1 ? true : false;
		stopAtGoal = in.readByte() == 1 ? true : false;
		id = in.readInt();
		position = in.readInt();
		group = in.readInt();
	}
	
	
	/**
	 * The Parcel creator used to create new instances of the Task from a parcel
	 */
	public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
		public Task createFromParcel(Parcel in) {
			return new Task(in);
		}

		public Task[] newArray(int size) {
			return new Task[size];
		}
	};
	
	
	/* (non-Javadoc)
	 * Gets a string representation of the object
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Task[name: \"" + name + "\", id: " + id + "]";
	}
	
	
	/**
	 * Comparator for comparing task names
	 */
	public static final Comparator<Task> NameComparator = new Comparator<Task>() {
		@Override
		public int compare(Task t1, Task t2) {
			return t1.getName().compareTo(t2.getName());
		}
	};
	
	/**
	 * Comparator for comparing task positions
	 */
	public static final Comparator<Task> PositionComparator = new Comparator<Task>() {
		@Override
		public int compare(Task t1, Task t2) {
			if(t1.getPosition() < t2.getPosition()) return -1;
			if(t1.getPosition() > t2.getPosition()) return 1;
			return 0;
		}
	};
	
	/**
	 * Comparator for comparing task IDs
	 */
	public static final Comparator<Task> IDComparator = new Comparator<Task>() {
		@Override
		public int compare(Task t1, Task t2) {
			if(t1.getId() < t2.getId()) return -1;
			if(t1.getId() > t2.getId()) return 1;
			return 0;
		}
	};
}
