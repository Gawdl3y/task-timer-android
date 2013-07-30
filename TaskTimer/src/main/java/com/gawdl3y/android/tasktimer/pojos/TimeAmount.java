package com.gawdl3y.android.tasktimer.pojos;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * A simple class that contains hours, minutes, and seconds
 * <p/>
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
     * @param mins  minutes
     * @param secs  seconds
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
     * Sets the hours, minutes, and seconds of the TimeAmount
     * @param hours hours
     * @param mins  minutes
     * @param secs  seconds
     */
    public void set(int hours, int mins, int secs) {
        this.hours = hours;
        this.mins = (short) mins;
        this.secs = (short) secs;
    }

    /**
     * Increments the TimeAmount by 1 second
     */
    public synchronized void increment() {
        increment(1);
    }

    /**
     * Increments the TimeAmount
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

    /**
     * Converts the time to a double
     * @return Double form of TimeAmount
     */
    public double toDouble() {
        return hours + (mins / 60.0) + (secs / 3600.0);
    }

    /**
     * Converts the time to a String, using a specific separator
     * @param separator The separator to use
     * @return String form of TimeAmount
     */
    public String toString(String separator) {
        return hours + separator + (mins < 10 ? "0" : "") + mins + separator + (secs < 10 ? "0" : "") + secs;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(hours);
        dest.writeInt(mins);
        dest.writeInt(secs);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public int compareTo(TimeAmount another) {
        return compare(this, another);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj == null || obj.getClass() != getClass()) return false;

        TimeAmount that = (TimeAmount) obj;
        if(hours != that.hours) return false;
        if(mins != that.mins) return false;
        if(secs != that.secs) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = hours;
        result = 31 * result + (int) mins;
        result = 31 * result + (int) secs;
        return result;
    }

    @Override
    public String toString() {
        return hours + ":" + (mins < 10 ? "0" : "") + mins + ":" + (secs < 10 ? "0" : "") + secs;
    }


    /**
     * Returns a new TimeAmount object from the double form
     * @param d The double to convert to a TimeAmount
     * @return A new TimeAmount
     */
    public static TimeAmount fromDouble(double d) {
        TimeAmount time = new TimeAmount();
        time.setHours((int) d);
        time.setMins((int) ((d - time.getHours()) * 60.0));
        time.setSecs((int) Math.round((d - time.getHours() - time.getMins() / 60.0) * 3600.0));
        return time;
    }

    /**
     * Returns a new TimeAmount object from the string form
     * @param string    The String to parse a TimeAmount from
     * @param separator The separator used in the string
     * @return A new TimeAmount
     */
    public static TimeAmount fromString(String string, String separator) {
        String[] parts = string.split(separator);
        TimeAmount time = new TimeAmount();
        time.setHours(Integer.parseInt(parts[0]));
        time.setMins(Integer.parseInt(parts[1]));
        time.setSecs(Integer.parseInt(parts[2]));
        return time;
    }

    /**
     * Returns a new TimeAmount object from the string form
     * @param string The String to parse a TimeAmount from
     * @return A new TimeAmount
     */
    public static TimeAmount fromString(String string) {
        return fromString(string, ":");
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
     * The Parcel creator used to create new instances of TimeAmount from a parcel
     */
    public static final Creator<TimeAmount> CREATOR = new Creator<TimeAmount>() {
        @Override
        public TimeAmount createFromParcel(Parcel in) {
            return new TimeAmount(in);
        }

        @Override
        public TimeAmount[] newArray(int size) {
            return new TimeAmount[size];
        }
    };


    /**
     * JSON Serializer
     * @author Schuyler Cebulskie
     */
    public static class Serializer implements JsonSerializer<TimeAmount> {
        @Override
        public JsonElement serialize(TimeAmount timeAmount, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject obj = new JsonObject();
            obj.addProperty("h", timeAmount.getHours());
            obj.addProperty("m", timeAmount.getMins());
            obj.addProperty("s", timeAmount.getSecs());
            return obj;
        }
    }

    /**
     * JSON Deserializer
     * @author Schuyler Cebulskie
     */
    public static class Deserializer implements JsonDeserializer<TimeAmount> {
        @Override
        public TimeAmount deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            TimeAmount timeAmount = new TimeAmount();
            if(jsonObject.has("h")) timeAmount.setHours(jsonObject.get("h").getAsInt());
            if(jsonObject.has("m")) timeAmount.setMins(jsonObject.get("m").getAsInt());
            if(jsonObject.has("s")) timeAmount.setSecs(jsonObject.get("s").getAsInt());
            return timeAmount;
        }
    }
}
