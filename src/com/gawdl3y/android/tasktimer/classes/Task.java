package com.gawdl3y.android.tasktimer.classes;

import java.io.Serializable;
import java.util.Comparator;

import android.content.res.TypedArray;
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
public class Task implements Serializable {
	private static final long serialVersionUID = -5638162774940783076L;
	
	private String name, description;
	private Time time, goal;
	private boolean indefinite, complete, running, stopAtGoal;
	private int id, position, group;
	
	/**
	 * Default constructor
	 */
	public Task() {
		this("EMPTY NAME", "", new Time(), new Time(), false, false, false, false, -1, -1, -1);
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
	public Task(String name, String description, Time time, Time goal, boolean indefinite, boolean complete, boolean running, boolean stopAtGoal, int id, int position, int group) {
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
	public Time getTime() {
		return time;
	}
	
	/**
	 * Sets the time of the Task
	 * @param time The time of the Task
	 */
	public void setTime(Time time) {
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
	public Time getGoal() {
		return goal;
	}
	
	/**
	 * Sets the goal time of the Task
	 * @param goal The goal time of the Task
	 */
	public void setGoal(Time goal) {
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
	public boolean getIndefinite() {
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
	public boolean getComplete() {
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
	public boolean getRunning() {
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
	 * Gets the category of the Task
	 * @return The category of the Task
	 */
	public int getGroup() {
		return group;
	}

	/**
	 * Sets the category of the Task
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
			return (int) Math.floor((time.getHours() + (time.getMins() / 60.0) + (time.getSecs() / 3600.0)) / ((goal.getHours() + (goal.getMins() / 60.0))) * 100);
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
	public static final void updateView(Task task, View view) {
		TextView nameView = (TextView) view.findViewById(R.id.task_name);
		TextView timeView = (TextView) view.findViewById(R.id.task_time);
		TextView goalView = (TextView) view.findViewById(R.id.task_goal);
		ProgressBar progressView = (ProgressBar) view.findViewById(R.id.task_progress);
		ImageView toggleView = (ImageView) view.findViewById(R.id.task_toggle);

		// Text views
		nameView.setText(task.getName());
		timeView.setText(task.getTime().toString());
		goalView.setText(task.getIndefinite() ? MainActivity.RES.getString(R.string.task_indefinite) : task.getGoal().toString());

		// Progress bar
		progressView.setIndeterminate(task.getIndefinite() && task.getRunning());
		progressView.setProgress(task.getProgress());

		// Change the toggle button to the proper image
		TypedArray ta = view.getContext().obtainStyledAttributes(new int[] { task.getRunning() ? R.attr.ic_pause : R.attr.ic_start });
		toggleView.setImageDrawable(ta.getDrawable(0));
		ta.recycle();

		// Set tags so we can figure out what task is being acted upon later
		view.setTag(task.getPosition());
		toggleView.setTag(task.getPosition());
	}
	
	/**
	 * @author Schuyler Cebulskie
	 * A simple time class used to keep track of Tasks' times
	 */
	public static final class Time implements Serializable, Comparable<Time> {
		private static final long serialVersionUID = -2489624821453413799L;
		
		private int hours;
		private short mins, secs;
		
		/**
		 * Default constructor
		 */
		public Time() {
			this(0, 0, 0);
		}
		
		/**
		 * Fill constructor
		 * @param hours hours
		 * @param mins minutes
		 * @param secs seconds
		 */
		public Time(int hours, int mins, int secs) {
			this.hours = hours;
			this.mins = (short) mins;
			this.secs = (short) secs;
		}
		
		/**
		 * Gets the hours
		 * @return hours
		 */
		public int getHours() {
			return hours;
		}
		
		/**
		 * Sets the hours
		 * @param hours hours
		 */
		public void setHours(int hours) {
			this.hours = hours;
		}
		
		/**
		 * Gets the minutes
		 * @return minutes
		 */
		public int getMins() {
			return mins;
		}
		
		/**
		 * Sets the minutes
		 * @param mins minutes
		 */
		public void setMins(int mins) {
			this.mins = (short) mins;
		}
		
		/**
		 * Gets the seconds
		 * @return seconds
		 */
		public int getSecs() {
			return secs;
		}
		
		/**
		 * Sets the seconds
		 * @param secs seconds
		 */
		public void setSecs(int secs) {
			this.secs = (short) secs;
		}
		
		/**
		 * Sets the hours, minutes, and seconds of the Time
		 * @param hours hours
		 * @param mins minutes
		 * @param secs seconds
		 */
		public void set(int hours, int mins, int secs) {
			this.hours = hours;
			this.mins = (short) mins;
			this.secs = (short) secs;
		}
		
		
		/**
		 * Increments the Time by 1 second
		 */
		public void increment() {
			increment(1);
		}
		
		/**
		 * Increments the Time
		 * @param secs the number of seconds to increment by
		 */
		public void increment(int secs) {
			this.secs += secs;
			this.distribute();
		}
		
		/**
		 * Distributes the hours, minutes, and seconds into the proper amounts
		 * For example, 2 hours 72 minutes 106 seconds will become 3 hours 13 minutes 46 seconds
		 */
		public void distribute() {
			if(secs >= 60) {
				short addMins = (short) (secs / 60);
				secs = (short) (secs - addMins * 60);
				mins += addMins;
			}
			
			if(this.mins >= 60) {
				short addHours = (short) (mins / 60);
				mins = (short) (mins - addHours * 60);
				hours += addHours;
			}
		}
		
		/* (non-Javadoc)
		 * Compares the time to another time
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(Time another) {
			// TODO Implement time comparing
			return 0;
		}
		
		/* (non-Javadoc)
		 * Returns a string representation of the Time (H:MM:SS)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return hours + ":" + (mins < 10 ? "0" : "") + mins + ":" + (secs < 10 ? "0" : "") + secs;
		}
	}
	
	/**
	 * @author Schuyler Cebulskie
	 * The comparator for comparing Task names
	 */
	public static final class NameComparator implements Comparator<Task> {
		@Override
		public int compare(Task t1, Task t2) {
			return t1.getName().compareTo(t2.getName());
		}
	}
	
	/**
	 * @author Schuyler Cebulskie
	 * The comparator for comparing Task positions
	 */
	public static final class PositionComparator implements Comparator<Task> {
		@Override
		public int compare(Task t1, Task t2) {
			if(t1.getPosition() < t2.getPosition()) return -1;
			if(t1.getPosition() > t2.getPosition()) return 1;
			return 0;
		}
	}
	
	/**
	 * @author Schuyler Cebulskie
	 * The comparator for comparing Group IDs
	 */
	public static final class IDComparator implements Comparator<Task> {
		@Override
		public int compare(Task t1, Task t2) {
			if(t1.getId() < t2.getId()) return -1;
			if(t1.getId() > t2.getId()) return 1;
			return 0;
		}
	}
}
