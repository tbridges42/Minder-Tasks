package us.bridgeses.minder_tasks.storage;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.Arrays;

import us.bridgeses.minder_tasks.models.Category;
import us.bridgeses.minder_tasks.models.Task;

/**
 * Created by Tony on 4/16/2016.
 *
 * Helper for persisting and retrieving tasks and categories
 */
public class PersistenceHelperImpl implements TasksContract, PersistenceHelper {
    private final ContentResolver resolver;

    public PersistenceHelperImpl(ContentResolver resolver) {
        this.resolver = resolver;
    }

    private Cursor getRecords(Uri uri, String[] projection, String selection,
                             String[] selectionArgs, String orderBy) {
        return resolver.query(uri, projection, selection, selectionArgs, orderBy);
    }

    /**
     * Convert a Cursor row to a Task
     * @param cursor the cursor must already be set to the correct row
     */
    private Task taskFromCursor(Cursor cursor) {
        final long id =
                cursor.getLong(cursor.getColumnIndex(TaskViewEntry._ID));
        final Category category =
                new Category(cursor.getLong(cursor.getColumnIndex(TaskViewEntry.COLUMN_CATEGORY_ID)),
                        cursor.getString(cursor.getColumnIndex(TaskViewEntry.COLUMN_CATEGORY)),
                        cursor.getInt(cursor.getColumnIndex(TaskViewEntry.COLUMN_CATEGORY_COLOR)));
        final String name =
                cursor.getString(cursor.getColumnIndex(TaskViewEntry.COLUMN_NAME));
        final long created_time =
                cursor.getLong(cursor.getColumnIndex(TaskViewEntry.COLUMN_CREATION_TIME));
        final int duration =
                cursor.getInt(cursor.getColumnIndex(TaskViewEntry.COLUMN_DURATION));
        final long due_time =
                cursor.getLong(cursor.getColumnIndex(TaskViewEntry.COLUMN_DUE_TIME));
        final boolean completed =
                cursor.getInt(cursor.getColumnIndex(TaskViewEntry.COLUMN_COMPLETED)) == 1;
        return new Task.Builder(name)
                .setId(id)
                .setCategory(category)
                .setCreationTime(created_time)
                .setDueTime(due_time)
                .setDuration(duration)
                .setCompleted(completed)
                .build();
    }

    private Category categoryFromCursor(Cursor cursor) {
        final Long id =
                cursor.getLong(cursor.getColumnIndex(CategoryEntry._ID));
        final String name =
                cursor.getString(cursor.getColumnIndex(CategoryEntry.COLUMN_NAME));
        final int color =
                cursor.getInt(cursor.getColumnIndex(CategoryEntry.COLUMN_COLOR));
        return new Category(id, name, color);
    }

    private ContentValues cvFromTask(Task task) {
        final ContentValues values = new ContentValues();
        if (task.getId() >= 0) {
            values.put(TasksEntry._ID, task.getId());
        }
        values.put(TasksEntry.COLUMN_NAME, task.getName());
        if (task.getCategory() != null) {
            values.put(TasksEntry.COLUMN_CATEGORY, task.getCategory().getId());
        }
        values.put(TasksEntry.COLUMN_CREATION_TIME, task.getCreationTime());
        values.put(TasksEntry.COLUMN_DURATION, task.getDuration());
        values.put(TasksEntry.COLUMN_DUE_TIME, task.getDueTime());
        values.put(TasksEntry.COLUMN_COMPLETED, task.isCompleted());
        return values;
    }

    private ContentValues cvFromCategory(Category category) {
        final ContentValues values = new ContentValues();
        if (category.getId() >= 0) {
            values.put(CategoryEntry._ID, category.getId());
        }
        values.put(CategoryEntry.COLUMN_NAME, category.getName());
        values.put(CategoryEntry.COLUMN_COLOR, category.getColor());
        return values;
    }

    @Override
    public long saveTask(Task task) {
        boolean newTask = task.getId() < 0;
        final ContentValues values = cvFromTask(task);
        if (newTask) {
            Uri uri = resolver.insert(TasksEntry.TASK_URI, values);
            if (uri == null) {
                throw new Resources.NotFoundException("Failed to insert task");
            }
            return Long.parseLong(uri.getLastPathSegment());
        }
        else {
            int rows = resolver.update(
                    TasksEntry.TASK_URI.buildUpon().appendPath(Long.toString(task.getId())).build(),
                    values, null, null);
            if (rows == 0) {
                throw new Resources.NotFoundException("Failed to update task");
            }
            return task.getId();
        }

    }

    @Override
    public long saveCategory(Category category) {
        final ContentValues values = cvFromCategory(category);
        Uri uri = resolver.insert(CategoryEntry.CATEGORY_URI, values);
        if (uri == null) {
            throw new Resources.NotFoundException("Failed to insert category");
        }
        return Long.parseLong(uri.getLastPathSegment());
    }

    @Override
    public Task loadTask(long id) throws Resources.NotFoundException {
        Uri uri = TaskViewEntry.TASK_URI.buildUpon().appendPath(Long.toString(id)).build();
        final Cursor cursor = getRecords(uri, null, null, null, null);
        if ((cursor == null) || (cursor.getCount() == 0)){
            throw new Resources.NotFoundException("Task not found");
        }
        Log.d("loadtask", "cursor: " + cursor.getCount());
        Log.d("loadtask", Arrays.toString(cursor.getColumnNames()));
        cursor.moveToFirst();

        Task task = taskFromCursor(cursor);
        cursor.close();
        return task;
    }

    @Override
    public Category loadCategory(long id) throws Resources.NotFoundException {
        Uri uri = CategoryEntry.CATEGORY_URI.buildUpon().appendPath(Long.toString(id)).build();
        final Cursor cursor = getRecords(uri, null, null, null, null);
        if ((cursor == null) || (cursor.getCount() == 0)){
            throw new Resources.NotFoundException("Category not found");
        }
        cursor.moveToFirst();
        Category category = categoryFromCursor(cursor);
        cursor.close();
        return category;
    }

    @Override
    public Cursor loadAllCategories() {
        Uri uri = CategoryEntry.CATEGORY_URI;
        return getRecords(uri, CategoryEntry.SUMMARY_PROJECTION, null, null, null);
    }

    @Override
    public Cursor loadTaskSummaries() {
        Uri uri = TaskViewEntry.TASK_URI;
        return getRecords(uri, TaskViewEntry.SUMMARY_PROJECTION, null, null, null);
    }

    @Override
    public void recordCompletedTask(long id) {
        final ContentValues values = new ContentValues();
        values.put(TasksEntry.COLUMN_COMPLETED, 1);
        resolver.update(TasksEntry.TASK_URI, values, TasksEntry._ID + " = ?",
                new String[]{ Long.toString(id) });
    }

    @Override
    public void deleteTask(long id) {
        resolver.delete(TasksEntry.TASK_URI.buildUpon().appendPath(Long.toString(id)).build()
                , null, null);
    }

    @Override
    public void deleteCategory(long id) {
        resolver.delete(CategoryEntry.CATEGORY_URI, CategoryEntry._ID + " = ?",
                new String[] { Long.toString(id) });
    }
}
