package com.gawdl3y.android.tasktimer.pojos;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * A category for Task objcts; contains an array of Tasks if necessary
 * @author Schuyler Cebulskie
 */
public class Group implements Parcelable {
    private String name;
    private int id, position;
    private ArrayList<Task> tasks;

    /**
     * Default constructor
     */
    public Group() {
        this("EMPTY NAME", -1, -1, null);
    }

    /**
     * Name constructor
     * @param name The name of the Group
     */
    public Group(String name) {
        this(name, -1, -1, null);
    }

    /**
     * ID constructor
     * @param id The ID of the Group
     */
    public Group(int id) {
        this("EMPTY NAME", id, -1, null);
    }

    /**
     * Fill constructor
     * @param name     The name of the Group
     * @param id       The ID of the Group
     * @param position The position of the Group
     * @param tasks    The tasks in the Group
     */
    public Group(String name, int id, int position, ArrayList<Task> tasks) {
        this.name = name;
        this.id = id;
        this.position = position;
        this.tasks = tasks;
    }

    /**
     * Parcel constructor
     * @param parcel The parcel to read from
     */
    public Group(Parcel parcel) {
        readFromParcel(parcel);
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
     * Tests to see if the provided object is the same Group as this one (using IDs)
     * @param obj The object to compare to
     * @return Whether or not the object is the same
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj.getClass() != getClass()) return false;
        return id == ((Group) obj).getId();
    }


    /* (non-Javadoc)
     * Describes the contents for the parcel
     * @see android.os.Parcelable#describeContents()
     */
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * Writes the group to a parcel
     * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeTypedList(tasks);
        dest.writeInt(position);
        dest.writeInt(id);
    }

    /**
     * Fills the Group from a parcel
     * @param in The parcel to read from
     */
    private void readFromParcel(Parcel in) {
        name = in.readString();
        tasks = in.createTypedArrayList(Task.CREATOR);
        position = in.readInt();
        id = in.readInt();
    }


    /**
     * The Parcel creator used to create new instances of the Group from a parcel
     */
    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        public Group[] newArray(int size) {
            return new Group[size];
        }
    };


    /* (non-Javadoc)
     * Gets a string representation of the object
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Group { name=\"" + name + "\" id=" + id + " }";
    }


    /**
     * Comparator for comparing group names
     */
    public static final Comparator<Group> NAME_COMPARATOR = new Comparator<Group>() {
        @Override
        public int compare(Group g1, Group g2) {
            return g1.getName().compareTo(g2.getName());
        }
    };

    /**
     * Comparator for comparing group positions
     */
    public static final Comparator<Group> POSITION_COMPARATOR = new Comparator<Group>() {
        @Override
        public int compare(Group g1, Group g2) {
            if(g1.getPosition() < g2.getPosition()) return -1;
            if(g1.getPosition() > g2.getPosition()) return 1;
            return 0;
        }
    };

    /**
     * Comparator for comparing group IDs
     */
    public static final Comparator<Group> ID_COMPARATOR = new Comparator<Group>() {
        @Override
        public int compare(Group g1, Group g2) {
            if(g1.getId() < g2.getId()) return -1;
            if(g1.getId() > g2.getId()) return 1;
            return 0;
        }
    };

    /**
     * Database columns for Groups
     * @author Schuyler Cebulskie
     */
    public static final class Columns implements BaseColumns {
        /**
         * The content:// URL for the table
         */
        public static final Uri CONTENT_URI = Uri.parse("content://com.gawdl3y.android.tasktimer/groups");

        /**
         * The name of the Group (Type: STRING)
         */
        public static final String NAME = "name";

        /**
         * The position of the Group (Type: INTEGER)
         */
        public static final String POSITION = "position";
    }
}
