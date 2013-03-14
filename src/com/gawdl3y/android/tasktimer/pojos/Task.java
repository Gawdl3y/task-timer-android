package com.gawdl3y.android.tasktimer.pojos;

import java.util.Comparator;
import java.util.HashMap;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A class that contains a time, goal time, and task-related properties
 * @author Schuyler Cebulskie
 */
public class Task implements Parcelable {
	// Data properties
	private String name, description;
	private TimeAmount time, goal;
	private boolean indefinite, complete, running;
	private int id, position, group;
	private HashMap<String, Object> settings;
	
	// Non-data utility properties
	private int alert;
	private long lastTick;
	
	// Static properties
	public static final HashMap<String, Object> DEFAULT_SETTINGS = new HashMap<String, Object>();
	
	// Static constructor
	static {
		DEFAULT_SETTINGS.put("stop", true);
		DEFAULT_SETTINGS.put("overtime", false);
	}
	
	/**
	 * Default constructor
	 */
	public Task() {
		this("EMPTY NAME", "", new TimeAmount(), new TimeAmount(), false, false, false, -1, -1, -1, new HashMap<String, Object>(), -1, -1);
	}
	
	/**
	 * Name constructor
	 * @param name The name of the Task
	 */
	public Task(String name) {
		this(name, "", new TimeAmount(), new TimeAmount(), false, false, false, -1, -1, -1, new HashMap<String, Object>(), -1, -1);
	}
	
	/**
	 * ID constructor
	 * @param id The ID of the Task
	 */
	public Task(int id) {
		this("EMPTY NAME", "", new TimeAmount(), new TimeAmount(), false, false, false, id, -1, -1, new HashMap<String, Object>(), -1, -1);
	}

	/**
	 * Fill constructor
	 * @param name The name of the Task
	 * @param time The time of the Task
	 * @param goal the goal time of the Task
	 * @param indefinite Whether or not the Task's goal is indefinite
	 * @param complete Whether or not the Task is completed
	 * @param running Whether or not the Task is running
	 * @param id The ID of the Task
	 * @param position The position of the Task in the array/ViewList
	 * @param group The ID of the group that the task is in
	 * @param settings Key-value-pairs of settings
	 * @param alert The pseudo-ID of the alert that is handling the task reaching its goal
	 * @param lastTick The time (in milliseconds) the task's time was last incremented
	 */
	public Task(String name, String description, TimeAmount time, TimeAmount goal, boolean indefinite, boolean complete, boolean running, int id, int position, int group, HashMap<String, Object> settings, int alert, long lastTick) {
		this.name = name;
		this.description = description;
		this.time = time;
		this.goal = goal;
		this.indefinite = indefinite;
		this.complete = complete;
		this.running = running;
		this.id = id;
		this.position = position;
		this.group = group;
		this.settings = settings;
		
		this.alert = alert;
		this.lastTick = lastTick;
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
	 * Gets the settings
	 * @return The settings
	 */
	public HashMap<String, Object> getSettings() {
		return settings;
	}
	
	/**
	 * Sets the settings
	 * @param settings The settings
	 */
	public void setSettings(HashMap<String, Object> settings) {
		this.settings = settings;
	}
	
	
	/**
	 * Sets the pseudo-ID of the alert that is handling the task reaching its goal
	 * @param alert The pseudo-ID
	 */
	public void setAlert(int alert) {
		this.alert = alert;
	}
	
	/**
	 * Gets the pseudo-ID of the alert that is handling the task reaching its goal
	 * @return The pseudo-ID
	 */
	public int getAlert() {
		return alert;
	}
	
	/**
	 * Gets the last tick time (for use in timer threads)
	 * @return The last tick time
	 */
	public long getLastTick() {
		return lastTick;
	}
	
	/**
	 * Sets the last tick time (for use in timer threads)
	 * @param lastTick The last tick time
	 */
	public void setLastTick(long lastTick) {
		this.lastTick = lastTick;
	}
	
	
	/**
	 * Gets a setting from the provided key if it exists, or the default value if it doesn't
	 * @param key The key of the setting
	 * @return The setting
	 */
	public Object getSetting(String key) {
		return settings.containsKey(key) ? settings.get(key) : DEFAULT_SETTINGS.get(key);
	}
	
	/**
	 * Gets a boolean setting
	 * @param key The key of the boolean setting
	 * @return The boolean setting
	 */
	public boolean getBooleanSetting(String key) {
		if(!settings.containsKey(key)) return (Boolean) DEFAULT_SETTINGS.get(key);
		return settings.containsKey(key) ? (Boolean) settings.get(key) : (Boolean) DEFAULT_SETTINGS.get(key);
	}
	
	/**
	 * Sets a setting
	 * @param key The key of the setting
	 * @param value The value of the setting
	 */
	public void setSetting(String key, Object value) {
		settings.put(key, value);
	}
	
	/**
	 * Removes a setting (resets to default)
	 * @param key The key of the setting
	 */
	public void removeSetting(String key) {
		settings.remove(key);
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
	 * Determines whether or not the task should stop running
	 * @return Whether or not the task should stop running
	 */
	public boolean shouldStop() {
		if((!complete && getBooleanSetting("stop")) || (complete && !getBooleanSetting("overtime"))) return true;
		return false;
	}
	
	/**
	 * Toggles the running status of the Task
	 */
	public synchronized void toggle() {
		running = !running;
	}
	
	/**
	 * Increments the Task's time by 1 second
	 */
	public synchronized void incrementTime() {
		incrementTime(1);
	}
	
	/**
	 * Increments the Task's time
	 * @param secs The seconds to increment the time by
	 */
	public synchronized void incrementTime(int secs) {
		time.increment(secs);
		if(time.compareTo(goal) >= 0) {
			if(shouldStop()) running = false;
			complete = true;
		}
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
		dest.writeInt(id);
		dest.writeInt(position);
		dest.writeInt(group);
		dest.writeMap(settings);
	}
	
	/**
	 * Fills the Task from a parcel
	 * @param in The parcel to read from
	 */
	@SuppressWarnings("unchecked")
	private void readFromParcel(Parcel in) {
		name = in.readString();
		description = in.readString();
		time = (TimeAmount) in.readParcelable(TimeAmount.class.getClassLoader());
		goal = (TimeAmount) in.readParcelable(TimeAmount.class.getClassLoader());
		indefinite = in.readByte() == 1 ? true : false;
		complete = in.readByte() == 1 ? true : false;
		running = in.readByte() == 1 ? true : false;
		id = in.readInt();
		position = in.readInt();
		group = in.readInt();
		settings = in.readHashMap(Task.class.getClassLoader());
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
		return "Task { name=\"" + name + "\" id=" + id + " }";
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
