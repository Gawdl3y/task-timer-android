package com.gawdl3y.android.tasktimer.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import com.gawdl3y.android.tasktimer.util.Log;

/**
 * The ContentProvider of Tasks and Groups for Task Timer
 * @author Schuyler Cebulskie
 */
public class TaskTimerProvider extends ContentProvider {
    private static final String TAG = "Provider";
    private static final String AUTHORITY = "com.gawdl3y.android.tasktimer.provider";
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int TASKS = 1;
    private static final int TASKS_ID = 2;
    private static final int GROUPS = 3;
    private static final int GROUPS_ID = 4;

    private TaskTimerDatabaseHelper mDbHelper;

    static {
        sUriMatcher.addURI(AUTHORITY, "tasks", TASKS);
        sUriMatcher.addURI(AUTHORITY, "tasks/#", TASKS_ID);
        sUriMatcher.addURI(AUTHORITY, "groups", GROUPS);
        sUriMatcher.addURI(AUTHORITY, "groups/#", GROUPS_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new TaskTimerDatabaseHelper(getContext());
        Log.v(TAG, "Created");
        return true;
    }

    @Override
    public String getType(Uri uri) {
        Log.v(TAG, "Getting type for URI " + uri.toString());
        int match = sUriMatcher.match(uri);
        switch(match) {
            case TASKS:
                return "vnd.android.cursor.dir/vnd." + AUTHORITY + ".tasks";
            case TASKS_ID:
                return "vnd.android.cursor.item/vnd." + AUTHORITY + ".tasks";
            case GROUPS:
                return "vnd.android.cursor.dir/vnd." + AUTHORITY + ".groups";
            case GROUPS_ID:
                return "vnd.android.cursor.item/vnd." + AUTHORITY + ".groups";
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Cursor query(Uri url, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        int match = sUriMatcher.match(url);
        switch(match) {
            case TASKS:
                qb.setTables("tasks");
                break;
            case TASKS_ID:
                qb.setTables("tasks");
                qb.appendWhere("_id=");
                qb.appendWhere(url.getPathSegments().get(1));
                break;
            case GROUPS:
                qb.setTables("groups");
                break;
            case GROUPS_ID:
                qb.setTables("groups");
                qb.appendWhere("_id=");
                qb.appendWhere(url.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URL " + url);
        }

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        if(cursor == null) Log.v(TAG, "Query failed"); else cursor.setNotificationUri(getContext().getContentResolver(), url);
        return cursor;
    }

    @Override
    public int update(Uri url, ContentValues values, String selection, String[] selectionArgs) {
        int match = sUriMatcher.match(url), count;
        long rowID;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        switch(match) {
            case TASKS_ID:
                String segment = url.getPathSegments().get(1);
                rowID = Long.parseLong(segment);
                count = db.update("tasks", values, "_id=" + rowID, null);
                break;
            default:
                throw new UnsupportedOperationException("Cannot update URL: " + url);
        }

        Log.v(TAG, "*** notifyChange() rowID: " + rowID + " url " + url);
        getContext().getContentResolver().notifyChange(url, null);
        return count;
    }

    @Override
    public Uri insert(Uri url, ContentValues values) {
        int match = sUriMatcher.match(url);
        Uri newURL;

        switch(match) {
            case TASKS:
                newURL = mDbHelper.taskInsert(values);
                break;
            case GROUPS:
                newURL = mDbHelper.groupInsert(values);
                break;
            default:
                throw new IllegalArgumentException("Cannot insert into URL: " + url);
        }

        getContext().getContentResolver().notifyChange(newURL, null);
        return newURL;
    }

    @Override
    public int delete(Uri url, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count;
        long rowID = 0;
        String segment;

        switch(sUriMatcher.match(url)) {
            case TASKS:
                count = db.delete("tasks", selection, selectionArgs);
                break;
            case TASKS_ID:
                segment = url.getPathSegments().get(1);
                rowID = Long.parseLong(segment);
                selection = "_id=" + segment + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                count = db.delete("tasks", selection, selectionArgs);
                break;

            case GROUPS:
                count = db.delete("groups", selection, selectionArgs);
                break;
            case GROUPS_ID:
                segment = url.getPathSegments().get(1);
                rowID = Long.parseLong(segment);
                selection = "_id=" + segment + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                count = db.delete("groups", selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Cannot delete from URL: " + url);
        }

        getContext().getContentResolver().notifyChange(url, null);
        return count;
    }
}
