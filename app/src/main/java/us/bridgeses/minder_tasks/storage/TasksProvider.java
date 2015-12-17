package us.bridgeses.minder_tasks.storage;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Content Provider for returning Tasks and Categories
 */
// TODO: Try to encapsulate SQL better
// TODO: Write tests
public class TasksProvider extends ContentProvider implements TasksContract {

    private Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    private static final int SINGLE_TASK = 100;
    private static final int MULTIPLE_TASKS = 101;
    private static final int SINGLE_CATEGORY = 102;
    private static final int MULTIPLE_CATEGORIES = 103;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher((UriMatcher.NO_MATCH));

        matcher.addURI(CONTENT_AUTHORITY, TASKS_TABLE, MULTIPLE_TASKS);
        matcher.addURI(CONTENT_AUTHORITY, TASKS_TABLE + "/#", SINGLE_TASK);
        matcher.addURI(CONTENT_AUTHORITY, CATEGORIES_TABLE, MULTIPLE_CATEGORIES);
        matcher.addURI(CONTENT_AUTHORITY, TASKS_TABLE + "/#", SINGLE_CATEGORY);
        return matcher;
    }

    public void setContext(Context context) {
        this.context = context;
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
                return TasksEntry.CONTENT_TASK;
            case MULTIPLE_TASKS:
                return TasksEntry.CONTENT_TASKS;
            default:
                throw new UnsupportedOperationException("Unknown uri: "
                        + uri);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection,
                        String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor result;
        initDb();
        switch (uriMatcher.match(uri)) {
            case SINGLE_CATEGORY:
                selection = CategoryEntry._ID + " = ?";
                selectionArgs = new String[] { uri.getLastPathSegment() };
                result = query(CATEGORIES_TABLE, projection, selection, selectionArgs, sortOrder);
                break;
            case SINGLE_TASK:
                selection = CategoryEntry._ID + " = ?";
                selectionArgs = new String[] { uri.getLastPathSegment() };
                result = query(TASKS_TABLE, projection, selection, selectionArgs, sortOrder);
                break;
            case MULTIPLE_CATEGORIES:
                result = query(CATEGORIES_TABLE, projection, selection, selectionArgs, sortOrder);
                break;
            case MULTIPLE_TASKS:
                result = query(TASKS_TABLE, projection, selection, selectionArgs, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "
                        + uri);
        }
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
        switch (uriMatcher.match(uri)) {
            case MULTIPLE_CATEGORIES:
                id = db.insertWithOnConflict(CATEGORIES_TABLE, null, values,
                        SQLiteDatabase.CONFLICT_REPLACE);
                result = CategoryEntry.CATEGORY_URI.buildUpon().appendPath(Long.toString(id)).build();
                break;
            case MULTIPLE_TASKS:
                id = db.insertWithOnConflict(TASKS_TABLE, null, values,
                        SQLiteDatabase.CONFLICT_REPLACE);
                result = TasksEntry.TASK_URI.buildUpon().appendPath(Long.toString(id)).build();
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "
                        + uri);
        }
        context.getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        initDb();
        int rowsDeleted;

        switch (uriMatcher.match(uri)) {
            case SINGLE_CATEGORY:
                selection = CategoryEntry._ID + " = ?";
                selectionArgs = new String[] { uri.getLastPathSegment() };
                rowsDeleted = delete(CATEGORIES_TABLE, selection, selectionArgs);
                break;
            case SINGLE_TASK:
                selection = CategoryEntry._ID + " = ?";
                selectionArgs = new String[] { uri.getLastPathSegment() };
                rowsDeleted = delete(TASKS_TABLE, selection, selectionArgs);
                break;
            case MULTIPLE_CATEGORIES:
                rowsDeleted = delete(CATEGORIES_TABLE, selection, selectionArgs);
                break;
            case MULTIPLE_TASKS:
                rowsDeleted = delete(TASKS_TABLE, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "
                        + uri);
        }
        if (rowsDeleted != 0) {
            context.getContentResolver().notifyChange(uri, null);
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

        switch (uriMatcher.match(uri)) {
            case MULTIPLE_CATEGORIES:
                rowsUpdated = db.update(CATEGORIES_TABLE, values, selection, selectionArgs);
                break;
            case MULTIPLE_TASKS:
                rowsUpdated = db.update(TASKS_TABLE, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "
                        + uri);
        }

        if (rowsUpdated != 0) {
            context.getContentResolver().notifyChange(uri, null);
        }
        return 0;
    }

    private void initDb() {
        if (db == null) {
            db = dbHelper.getWritableDatabase();
        }
    }
}
