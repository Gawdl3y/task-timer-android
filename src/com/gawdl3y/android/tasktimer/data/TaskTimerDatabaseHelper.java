package com.gawdl3y.android.tasktimer.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import com.gawdl3y.android.tasktimer.pojos.Group;
import com.gawdl3y.android.tasktimer.pojos.Task;
import com.gawdl3y.android.tasktimer.util.Log;

public class TaskTimerDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "TaskTimerDatabaseHelper";

    // General database info
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TaskTimer.db";

    // Task table
    private static final String TASKS_TABLE_NAME = "tasks";
    private static final String TASKS_TABLE_CREATE = "CREATE TABLE "
            + TASKS_TABLE_NAME + " ("
            + Task.Columns._ID + " INTEGER PRIMARY KEY, "
            + Task.Columns.NAME + " TEXT, "
            + Task.Columns.DESCRIPTION + " TEXT, "
            + Task.Columns.TIME + " TEXT, "
            + Task.Columns.GOAL + " TEXT, "
            + Task.Columns.INDEFINITE + " INTEGER, "
            + Task.Columns.COMPLETE + " INTEGER, "
            + Task.Columns.SETTINGS + " TEXT, "
            + Task.Columns.POSITION + " INTEGER, `"
            + Task.Columns.GROUP + "` INTEGER);";

    // Group table
    private static final String GROUPS_TABLE_NAME = "groups";
    private static final String GROUPS_TABLE_CREATE = "CREATE TABLE "
            + GROUPS_TABLE_NAME + " ("
            + Group.Columns._ID + " INTEGER PRIMARY KEY, "
            + Group.Columns.NAME + " TEXT, "
            + Group.Columns.POSITION + " INTEGER);";

    /**
     * Constructor
     * @param context The context for the database
     */
    TaskTimerDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * The database is first being created
     * @param db The database
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(TASKS_TABLE_CREATE);
        db.execSQL(GROUPS_TABLE_CREATE);

        // Insert default data
        String insertGroup = "INSERT INTO groups (" + Group.Columns.NAME + ", " + Group.Columns.POSITION + ") VALUES ";
        String insertTask = "INSERT INTO tasks ("
                + Task.Columns.NAME + ", "
                + Task.Columns.DESCRIPTION + ", "
                + Task.Columns.TIME + ", "
                + Task.Columns.GOAL + ", "
                + Task.Columns.INDEFINITE + ", "
                + Task.Columns.COMPLETE + ", "
                + Task.Columns.SETTINGS + ", "
                + Task.Columns.POSITION + ", `"
                + Task.Columns.GROUP + "`) VALUES ";

        db.execSQL(insertGroup + "('A group', 0);");
        db.execSQL(insertTask + "('A task', '', '{}', '{\"h\":\"2\",\"m\":\"30\"}', 0, 0, '{}', 0, 1);");
        db.execSQL(insertTask + "('An indefinite task', '', '{}', '{\"h\":\"4\"}', 1, 0, '{}', 1, 1);");
        db.execSQL(insertTask + "('A short task', 'This task has a really short goal so you can test the notifications.', '{\"s\":\"45\"}', '{\"m\":\"1\"}', 0, 0, '{}', 2, 1);");
    }

    /**
     * The database is being upgraded
     * @param db The database
     * @param oldVersion The old database version
     * @param newVersion The new database version
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nothing to upgrade from yet!
    }


    /**
     * Insert a task
     * @param values The values of the row
     * @return The URI for the new insertion
     */
    public Uri taskInsert(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        long rowID = -1;

        // Insert the row
        try {
            // Make sure the ID is unique
            Object value = values.get(Task.Columns._ID);
            if(value != null) {
                int id = (Integer) value;
                if(id > -1) {
                    final Cursor cursor = db.query("tasks", new String[]{Task.Columns._ID}, "_id = ?", new String[] {Integer.toString(id)} , null, null, null);
                    if(cursor.moveToFirst()) {
                        // It exists! Remove the ID so SQLite fills in a new one
                        values.putNull(Task.Columns._ID);
                    }
                }
            }

            // Perform the insertion
            rowID = db.insert("tasks", Task.Columns.NAME, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        // Insertion failure
        if(rowID < 0) throw new SQLException("Failed to insert row");

        Log.v(TAG, "Added new task with ID " + rowID);
        return ContentUris.withAppendedId(Task.Columns.CONTENT_URI, rowID);
    }


    /**
     * Insert a group
     * @param values The values of the row
     * @return The URI for the new insertion
     */
    public Uri groupInsert(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        long rowID = -1;

        // Insert the row
        try {
            // Make sure the ID is unique
            Object value = values.get(Group.Columns._ID);
            if(value != null) {
                int id = (Integer) value;
                if(id > -1) {
                    final Cursor cursor = db.query("groups", new String[]{Group.Columns._ID}, "_id = ?", new String[]{Integer.toString(id)} , null, null, null);
                    if(cursor.moveToFirst()) {
                        // It exists! Remove the ID so SQLite fills in a new one
                        values.putNull(Group.Columns._ID);
                    }
                }
            }

            // Perform the insertion
            rowID = db.insert("groups", Task.Columns.NAME, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        // Insertion failure
        if(rowID < 0) throw new SQLException("Failed to insert row");

        Log.v(TAG, "Added new group with ID " + rowID);
        return ContentUris.withAppendedId(Task.Columns.CONTENT_URI, rowID);
    }
}