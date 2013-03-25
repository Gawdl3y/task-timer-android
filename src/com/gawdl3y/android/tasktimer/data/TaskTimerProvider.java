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
 * The ContentProvider for Task Timer
 * <p>Handles Task/Group data
 * @author Schuyler Cebulskie
 */
public class TaskTimerProvider extends ContentProvider {
    public static final String TAG = "TaskTimerProvider";

    private static final int TASKS = 1;
    private static final int TASKS_ID = 2;
    private static final int GROUPS = 3;
    private static final int GROUPS_ID = 4;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private TaskTimerDatabaseHelper dbHelper;

    static {
        uriMatcher.addURI("com.gawdl3y.android.tasktimer", "tasks", TASKS);
        uriMatcher.addURI("com.gawdl3y.android.tasktimer", "tasks/#", TASKS_ID);
        uriMatcher.addURI("com.gawdl3y.android.tasktimer", "groups", GROUPS);
        uriMatcher.addURI("com.gawdl3y.android.tasktimer", "groups/#", GROUPS_ID);
    }

    /**
     * The content provider has been created
     * @return Good or not ;)
     * @see android.content.ContentProvider#onCreate()
     */
    @Override
    public boolean onCreate() {
        dbHelper = new TaskTimerDatabaseHelper(getContext());
        return true;
    }

    /**
     * Get the MIME type of the data for a URI
     * @param uri The URI to get the MIME type of the data for
     * @return The MIME type of the data for the URI
     * @see android.content.ContentProvider#getType(android.net.Uri)
     */
    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);
        switch(match) {
            case TASKS:
                return "vnd.android.cursor.dir/vnd.com.gawdl3y.android.tasktimer.provider.tasks";
            case TASKS_ID:
                return "vnd.android.cursor.item/vnd.com.gawdl3y.android.tasktimer.provider.tasks";
            case GROUPS:
                return "vnd.android.cursor.dir/vnd.com.gawdl3y.android.tasktimer.provider.groups";
            case GROUPS_ID:
                return "vnd.android.cursor.item/vnd.com.gawdl3y.android.tasktimer.provider.groups";
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    /**
     * Perform a query
     * @param url           The URI of the query
     * @param projection    The list of columns to put into the cursor, or all columns if null
     * @param selection     Selection to restrict to, or all rows if null
     * @param selectionArgs Values to replace placeholders with in selection
     * @param sortOrder     How the rows should be sorted
     * @return Cursor for the data
     * @see android.content.ContentProvider#query(android.net.Uri, String[], String, String[], String)
     */
    @Override
    public Cursor query(Uri url, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        int match = uriMatcher.match(url);
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

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        if(cursor == null) Log.v(TAG, "Query failed"); else cursor.setNotificationUri(getContext().getContentResolver(), url);
        return cursor;
    }

    /**
     * Update row(s)
     * @param url           The URI for the query
     * @param values        The values to update
     * @param selection     The selection to restrict to
     * @param selectionArgs Values to replace placeholders with in selection
     * @return The number of rows affected
     * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, String, String[])
     */
    @Override
    public int update(Uri url, ContentValues values, String selection, String[] selectionArgs) {
        int match = uriMatcher.match(url), count;
        long rowID;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

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

    /**
     * Insert a row
     * @param url    The URI for the query
     * @param values The values to insert
     * @return The URI for the inserted items
     * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
     */
    @Override
    public Uri insert(Uri url, ContentValues values) {
        int match = uriMatcher.match(url);
        Uri newURL;

        switch(match) {
            case TASKS:
                newURL = dbHelper.taskInsert(values);
                break;
            case GROUPS:
                newURL = dbHelper.groupInsert(values);
                break;
            default:
                throw new IllegalArgumentException("Cannot insert into URL: " + url);
        }

        getContext().getContentResolver().notifyChange(newURL, null);
        return newURL;
    }

    /**
     * Delete row(s)
     * @param url           The URI for the query
     * @param selection     The selection to restrict to
     * @param selectionArgs Values to replace placeholders with in selection
     * @return The number of rows affected
     * @see android.content.ContentProvider#delete(android.net.Uri, String, String[])
     */
    @Override
    public int delete(Uri url, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        long rowID = 0;
        String segment;

        switch(uriMatcher.match(url)) {
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
