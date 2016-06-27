package us.bridgeses.minder_tasks.storage;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Arrays;

/**
 * Content Provider for returning Tasks and Categories
 */
// TODO: Can we set matching at runtime?
// TODO: Write tests
public class TasksProvider extends ContentProvider implements TasksContract {

    private Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    private static final int SINGLE_TASK = 100;
    private static final int MULTIPLE_TASKS = 101;
    private static final int SINGLE_CATEGORY = 102;
    private static final int MULTIPLE_CATEGORIES = 103;
    private static final int SINGLE_TASK_VIEW = 104;
    private static final int MULTIPLE_TASKS_VIEW = 105;

    public static final String INSERT = "insert";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher((UriMatcher.NO_MATCH));

        matcher.addURI(CONTENT_AUTHORITY, TASKS_TABLE, MULTIPLE_TASKS);
        matcher.addURI(CONTENT_AUTHORITY, TASKS_TABLE + "/#", SINGLE_TASK);
        matcher.addURI(CONTENT_AUTHORITY, TASKS_VIEW, MULTIPLE_TASKS_VIEW);
        matcher.addURI(CONTENT_AUTHORITY, TASKS_VIEW + "/#", SINGLE_TASK_VIEW);
        matcher.addURI(CONTENT_AUTHORITY, CATEGORIES_TABLE, MULTIPLE_CATEGORIES);
        matcher.addURI(CONTENT_AUTHORITY, CATEGORIES_TABLE + "/#", SINGLE_CATEGORY);
        return matcher;
    }

    public void setContext(Context context) {
        this.context = context.getApplicationContext();
    }

    public void setDbHelper(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public boolean onCreate() {
        if (context == null) {
            context = getContext();
        }
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
        }
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case SINGLE_CATEGORY:
                return CategoryEntry.CONTENT_CATEGORY;
            case MULTIPLE_CATEGORIES:
                return CategoryEntry.CONTENT_CATEGORIES;
            case SINGLE_TASK:
            case SINGLE_TASK_VIEW:
                return TasksEntry.CONTENT_TASK;
            case MULTIPLE_TASKS:
            case MULTIPLE_TASKS_VIEW:
                return TasksEntry.CONTENT_TASKS;
            default:
                throw new UnsupportedOperationException("Unknown uri: "
                        + uri);
        }
    }

    private String getTable(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case SINGLE_CATEGORY:
            case MULTIPLE_CATEGORIES:
                return CATEGORIES_TABLE;
            case SINGLE_TASK:
            case MULTIPLE_TASKS:
                return TASKS_TABLE;
            case SINGLE_TASK_VIEW:
            case MULTIPLE_TASKS_VIEW:
                return TASKS_VIEW;
            default:
                throw new UnsupportedOperationException("Unknown uri: "
                        + uri);
        }
    }

    private boolean isMultiple(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case SINGLE_CATEGORY:
            case SINGLE_TASK:
            case SINGLE_TASK_VIEW:
                return false;
            case MULTIPLE_CATEGORIES:
            case MULTIPLE_TASKS:
            case MULTIPLE_TASKS_VIEW:
                return true;
            default:
                throw new UnsupportedOperationException("Unknown uri: "
                        + uri);
        }
    }

    private Uri getBaseUri(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case SINGLE_CATEGORY:
            case MULTIPLE_CATEGORIES:
                return CategoryEntry.CATEGORY_URI;
            case SINGLE_TASK:
            case MULTIPLE_TASKS:
                return TasksEntry.TASK_URI;
            case SINGLE_TASK_VIEW:
            case MULTIPLE_TASKS_VIEW:
                return TaskViewEntry.TASK_URI;
            default:
                throw new UnsupportedOperationException("Unknown uri: "
                        + uri);
        }
    }

    private Uri buildCallbackUri(Uri uri, String changeType) {
        Uri returnUri;
        switch (uriMatcher.match(uri)) {
            case SINGLE_CATEGORY:
            case MULTIPLE_CATEGORIES:
                returnUri = CategoryEntry.CATEGORY_URI;
                break;
            case SINGLE_TASK:
            case SINGLE_TASK_VIEW:
            case MULTIPLE_TASKS:
            case MULTIPLE_TASKS_VIEW:
                returnUri = TasksEntry.TASK_URI;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "
                        + uri);
        }
        Uri.Builder builder = returnUri.buildUpon();
        builder.appendPath(changeType);
        Log.d("provideruri", uri.toString());
        if (!isMultiple(uri)) {
            builder.appendPath(uri.getLastPathSegment());
        }
        returnUri = builder.build();
        Log.d("providerreturn", returnUri.toString());
        return returnUri;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection,
                        String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor result;
        initDb();
        if (!isMultiple(uri)) {
            selection = BaseColumns._ID + " = ?";
            selectionArgs = new String[]{uri.getLastPathSegment()};
        }
        result = query(getTable(uri), projection, selection, selectionArgs, sortOrder);
        result.setNotificationUri(context.getContentResolver(), uri);
        return result;
    }

    private Cursor query(String table, String[] projection,
                         String selection, String[] selectionArgs,
                         String sortOrder) {
        return db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri result;
        long id;
        initDb();
        Uri baseUri = getBaseUri(uri);
        id = db.insertWithOnConflict(getTable(uri), null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
        result = baseUri.buildUpon().appendPath(Long.toString(id)).build();
        Log.d("insert", uri.toString());
        context.getContentResolver().notifyChange(buildCallbackUri(result, INSERT), null);
        return result;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        initDb();
        int rowsDeleted;

        if (!isMultiple(uri)) {
            selection = BaseColumns._ID + " = ?";
            selectionArgs = new String[]{uri.getLastPathSegment()};
        }
        rowsDeleted = delete(getTable(uri), selection, selectionArgs);

        if (rowsDeleted != 0) {
            Log.d("provider", buildCallbackUri(uri, DELETE).toString());
            context.getContentResolver().notifyChange(buildCallbackUri(uri, DELETE), null);
        }
        return rowsDeleted;
    }

    private int delete(String tableName, String selection, String[] selectionArgs) {
        return db.delete(tableName, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        initDb();
        int rowsUpdated;

        if (!isMultiple(uri)) {
            selection = BaseColumns._ID + " = ?";
            selectionArgs = new String[]{uri.getLastPathSegment()};
            Log.d("update", selection);
            Log.d("update", selectionArgs[0]);
        }
        rowsUpdated = db.update(getTable(uri), values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            context.getContentResolver().notifyChange(buildCallbackUri(uri, UPDATE), null);
        }
        return rowsUpdated;
    }

    private void initDb() {
        if (db == null) {
            db = dbHelper.getWritableDatabase();
        }
    }
}
