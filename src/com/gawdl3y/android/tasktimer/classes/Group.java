package com.gawdl3y.android.tasktimer.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author Schuyler Cebulskie
 * A simple class for grouping together Tasks
 */
public class Group implements Serializable {
	private static final long serialVersionUID = -8667036902807578578L;
	
	private String name;
	private ArrayList<Task> tasks;
	private int position, id;
	
	/**
	 * Default constructor
	 */
	public Group() {
		this("EMPTY NAME", new ArrayList<Task>(), -1, -1);
	}
	
	/**
	 * Fill constructor
	 * @param name The name of the Group
	 * @param id The ID of the Group for database use
	 */
	public Group(String name, ArrayList<Task> tasks, int position, int id) {
		this.name = name;
		this.tasks = tasks;
		this.position = position;
		this.id = id;
	}
	
	/**
	 * Gets the name of the Group
	 * @return The name of the Group
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the Group
	 * @param name The name of the Group
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the Tasks assigned to the Group
	 * @return the tasks
	 */
	public ArrayList<Task> getTasks() {
		return tasks;
	}

	/**
	 * Sets the Tasks assigned to the Group
	 * @param tasks the tasks to set
	 */
	public void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}

	/**
	 * Gets the position of the Group
	 * @return The position of the Group
	 */
	public int getPosition() {
		return position;
	}
	
	/**
	 * Sets the position of the group
	 * @param position The position of the group
	 */
	public void setPosition(int position) {
		this.position = position;
	}
	
	/**
	 * Gets the ID of the Group for database use
	 * @return The ID of the Group
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Sets the ID of the Group for database use
	 * @param id The ID of the Group
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @author Schuyler Cebulskie
	 * The comparator for comparing Group names
	 */
	public static final class NameComparator implements Comparator<Group> {
		@Override
		public int compare(Group g1, Group g2) {
			return g1.getName().compareTo(g2.getName());
		}
	}
	
	/**
	 * @author Schuyler Cebulskie
	 * The comparator for comparing Group positions
	 */
	public static final class PositionComparator implements Comparator<Group> {
		@Override
		public int compare(Group g1, Group g2) {
			if(g1.getPosition() < g2.getPosition()) return -1;
			if(g1.getPosition() > g2.getPosition()) return 1;
			return 0;
		}
	}
	
	/**
	 * @author Schuyler Cebulskie
	 * The comparator for comparing Group IDs
	 */
	public static final class IDComparator implements Comparator<Group> {
		@Override
		public int compare(Group g1, Group g2) {
			if(g1.getId() < g2.getId()) return -1;
			if(g1.getId() > g2.getId()) return 1;
			return 0;
		}
	}
	
}
