package com.gawdl3y.android.tasktimer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class TaskTimerOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TaskTimer";

    // Task columns
    private static final String TASK_ID = BaseColumns._ID;
    private static final String TASK_NAME = "name";
    private static final String TASK_DESCRIPTION = "description";
    private static final String TASK_TIME = "time";
    private static final String TASK_GOAL = "goal";
    private static final String TASK_INDEFINITE = "indefinite";
    private static final String TASK_COMPLETE = "complete";
    private static final String TASK_SETTINGS = "settings";
    private static final String TASK_GROUP = "group";

    // Task table
    private static final String TASKS_TABLE_NAME = "tasks";
    private static final String TASKS_TABLE_CREATE = "CREATE TABLE "
            + TASKS_TABLE_NAME + " ("
            + TASK_ID + " TEXT, "
            + TASK_NAME + " TEXT, "
            + TASK_DESCRIPTION + " TEXT, "
            + TASK_TIME + " TEXT, "
            + TASK_GOAL + " TEXT, "
            + TASK_INDEFINITE + " INTEGER, "
            + TASK_COMPLETE + " INTEGER, "
            + TASK_SETTINGS + " TEXT, "
            + TASK_GROUP + " INTEGER);";

    // Group columns
    private static final String GROUP_ID = BaseColumns._ID;
    private static final String GROUP_NAME = "name";

    // Group table
    private static final String GROUPS_TABLE_NAME = "categories";
    private static final String GROUPS_TABLE_CREATE = "CREATE TABLE "
            + GROUPS_TABLE_NAME + " ("
            + GROUP_ID + " INTEGER, "
            + GROUP_NAME + " TEXT);";

    TaskTimerOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TASKS_TABLE_CREATE);
        db.execSQL(GROUPS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}