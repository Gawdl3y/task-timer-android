package com.gawdl3y.android.tasktimer.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import com.gawdl3y.android.tasktimer.util.Log;

/**
 * The ContentProvider for Task Timer
 * <p>Handles Task/Group data
 */
public class TaskTimerProvider extends ContentProvider {
    public static final String TAG = "TaskTimerProvider";

    private static final int TASKS = 1;
    private static final int TASKS_ID = 2;
    private static final int GROUPS = 3;
    private static final int GROUPS_ID = 4;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private TaskTimerOpenHelper openHelper;

    static {
        uriMatcher.addURI("com.gawdl3y.android.tasktimer", "tasks", TASKS);
        uriMatcher.addURI("com.gawdl3y.android.tasktimer", "tasks/#", TASKS_ID);
        uriMatcher.addURI("com.gawdl3y.android.tasktimer", "groups", GROUPS);
        uriMatcher.addURI("com.gawdl3y.android.tasktimer", "groups/#", GROUPS_ID);
    }

    /**
     * The content provider has been created
     * @return Good or not ;)
     */
    @Override
    public boolean onCreate() {
        openHelper = new TaskTimerOpenHelper(getContext());
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
                return "vnd.android.cursor.dir/tasks";
            case TASKS_ID:
                return "vnd.android.cursor.item/tasks";
            case GROUPS:
                return "vnd.android.cursor.dir/groups";
            case GROUPS_ID:
                return "vnd.android.cursor.item/groups";
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    /**
     * Perform a query
     * @param uri The URI of the query
     * @param projection The list of columns to put into the cursor, or all columns if null
     * @param selection Selection to restrict to, or all rows if null
     * @param selectionArgs Values to replace placeholders with in selection
     * @param sortOrder How the rows should be sorted
     * @return Cursor for the data
     * @see android.content.ContentProvider#query(android.net.Uri, String[], String, String[], String)
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        int match = uriMatcher.match(uri);
        switch(match) {
            case TASKS:
                qb.setTables("tasks");
                break;
            case TASKS_ID:
                qb.setTables("tasks");
                qb.appendWhere("_ID=");
                qb.appendWhere(uri.getPathSegments().get(1));
                break;
            case GROUPS:
                qb.setTables("groups");
                break;
            case GROUPS_ID:
                qb.setTables("groups");
                qb.appendWhere("_ID=");
                qb.appendWhere(uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unkown URI " + uri);
        }

        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        if(cursor == null) Log.v(TAG, "Query failed: "); else cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * Insert a row
     * @param uri The URI for the query
     * @param values The values to insert
     * @return The URI for the inserted items
     * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    /**
     * Delete row(s)
     * @param uri The URI for the query
     * @param selection The selection to restrict to
     * @param selectionArgs Values to replace placeholders with in selection
     * @return The number of rows affected
     * @see android.content.ContentProvider#delete(android.net.Uri, String, String[])
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
     * Update row(s)
     * @param uri The URI for the query
     * @param values The values to update
     * @param selection The selection to restrict to
     * @param selectionArgs Values to replace placeholders with in selection
     * @return The number of rows affected
     * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, String, String[])
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
