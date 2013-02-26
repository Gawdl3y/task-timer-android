package com.gawdl3y.android.tasktimer.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A simple class that contains hours, minutes, and seconds
 * <p>
 * This is useful for keeping track of an amount of time, rather than
 * a specific time relative to the day.
 * @author Schuyler Cebulskie
 */
public class TimeAmount implements Parcelable, Comparable<TimeAmount> {
	private int hours;
	private short mins, secs;
	
	/**
	 * Default constructor
	 */
	public TimeAmount() {
		this(0, 0, 0);
	}
	
	/**
	 * Fill constructor
	 * @param hours hours
	 * @param mins minutes
	 * @param secs seconds
	 */
	public TimeAmount(int hours, int mins, int secs) {
		this.hours = hours;
		this.mins = (short) mins;
		this.secs = (short) secs;
	}
	
	/**
	 * Fill constructor (double)
	 * @param d The double (hours) to create from
	 */
	public TimeAmount(double d) {
		TimeAmount time = fromDouble(d);
		this.hours = time.getHours();
		this.mins = (short) time.getMins();
		this.secs = (short) time.getSecs();
	}
	
	/**
	 * Parcel constructor
	 * @param parcel The parcel to read from
	 */
	public TimeAmount(Parcel parcel) {
		this(parcel.readInt(), parcel.readInt(), parcel.readInt());
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
	public synchronized void increment() {
		increment(1);
	}
	
	/**
	 * Increments the Time
	 * @param secs the number of seconds to increment by
	 */
	public synchronized void increment(int secs) {
		this.secs += secs;
		this.distribute();
	}
	
	/**
	 * Distributes the hours, minutes, and seconds into the proper amounts
	 * For example, 2 hours 72 minutes 106 seconds will become 3 hours 13 minutes 46 seconds
	 */
	public synchronized void distribute() {
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
	 * Describes the contents for the parcel
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	/* (non-Javadoc)
	 * Writes the time to a parcel
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(hours);
		dest.writeInt(mins);
		dest.writeInt(secs);
	}
	
	
	/* (non-Javadoc)
	 * Compares the time to another time
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(TimeAmount another) {
		return compare(this, another);
	}
	
	/* (non-Javadoc)
	 * Returns a string representation of the time (H:MM:SS)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return hours + ":" + (mins < 10 ? "0" : "") + mins + ":" + (secs < 10 ? "0" : "") + secs;
	}
	
	/**
	 * Converts the time to a double
	 * @return Double form of Time
	 */
	public double toDouble() {
		return hours + (mins / 60.0) + (secs / 3600.0);
	}
	
	
	/**
	 * Compares two times
	 * @param t1 First time to compare
	 * @param t2 Second time to compare
	 * @return 1 if the first is greater, -1 if the second is greater, or 0 if they are equal
	 */
	public static int compare(TimeAmount t1, TimeAmount t2) {
		double a = t1.toDouble(), b = t2.toDouble();
		if(a < b) return -1;
		if(a > b) return 1;
		return 0;
	}
	
	/**
	 * Returns a new Time object from the double-form time
	 * @param d The double to convert to a Time
	 * @return A new Time
	 */
	public static TimeAmount fromDouble(double d) {
		TimeAmount time = new TimeAmount();
		time.setHours((int) d);
		time.setMins((int) ((d - time.getHours()) * 60.0));
		time.setSecs((int) Math.round((d - time.getHours() - time.getMins() / 60.0) * 3600.0));
		return time;
	}
	
	
	/**
	 * The Parcel creator used to create new instances of the Time from a parcel
	 */
	public static final Parcelable.Creator<TimeAmount> CREATOR = new Parcelable.Creator<TimeAmount>() {
		/* (non-Javadoc)
		 * Create a Time from a Parcel
		 * @see android.os.Parcelable.Creator#createFromParcel(android.os.Parcel)
		 */
		public TimeAmount createFromParcel(Parcel in) {
			return new TimeAmount(in);
		}

		/* (non-Javadoc)
		 * Create a new Time array from a Parcel
		 * @see android.os.Parcelable.Creator#newArray(int)
		 */
		public TimeAmount[] newArray(int size) {
			return new TimeAmount[size];
		}
	};
}
