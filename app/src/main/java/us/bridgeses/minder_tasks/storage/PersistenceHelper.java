package us.bridgeses.minder_tasks.storage;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;

import us.bridgeses.minder_tasks.models.Category;
import us.bridgeses.minder_tasks.models.Task;

/**
 * Created by Tony on 4/16/2016.
 *
 * Helper for persisting and retrieving tasks and categories
 */
// TODO: break contentvalues -> task -> cursor into separate methods
public class PersistenceHelper implements TasksContract {

    private final ContentResolver resolver;

    public PersistenceHelper(Context context) {
        resolver = context.getContentResolver();
    }

    public long saveTask(Task task) {
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
        Uri uri = resolver.insert(TasksEntry.TASK_URI, values);
        if (uri == null) {
            throw new Resources.NotFoundException("Failed to insert task");
        }
        return Long.parseLong(uri.getLastPathSegment());
    }

    public long saveCategory(Category category) {
        final ContentValues values = new ContentValues();
        if (category.getId() >= 0) {
            values.put(CategoryEntry._ID, category.getId());
        }
        values.put(CategoryEntry.COLUMN_NAME, category.getName());
        values.put(CategoryEntry.COLUMN_COLOR, category.getColor());
        Uri uri = resolver.insert(CategoryEntry.CATEGORY_URI, values);
        if (uri == null) {
            throw new Resources.NotFoundException("Failed to insert category");
        }
        return Long.parseLong(uri.getLastPathSegment());
    }

    public Task loadTask(long id) throws Resources.NotFoundException {
        Uri uri = TasksEntry.TASK_URI.buildUpon().appendPath(Long.toString(id)).build();
        final Cursor cursor = resolver.query(uri, null, null, null, null);
        if ((cursor == null) || (cursor.getCount() == 0)){
            throw new Resources.NotFoundException("Task not found");
        }
        cursor.moveToFirst();
        final String name =
                cursor.getString(cursor.getColumnIndex(TasksEntry.COLUMN_NAME));
        final long created_time =
                cursor.getLong(cursor.getColumnIndex(TasksEntry.COLUMN_CREATION_TIME));
        final int duration =
                cursor.getInt(cursor.getColumnIndex(TasksEntry.COLUMN_DURATION));
        final long due_time =
                cursor.getLong(cursor.getColumnIndex(TasksEntry.COLUMN_DUE_TIME));
        final boolean completed =
                cursor.getInt(cursor.getColumnIndex(TasksEntry.COLUMN_COMPLETED)) == 1;
        Task task = new Task.Builder(name)
                .setId(id)
                .setCreationTime(created_time)
                .setDueTime(due_time)
                .setDuration(duration)
                .setCompleted(completed)
                .build();
        cursor.close();
        return task;
    }

    public Category loadCategory(long id) throws Resources.NotFoundException {
        Uri uri = CategoryEntry.CATEGORY_URI.buildUpon().appendPath(Long.toString(id)).build();
        final Cursor cursor = resolver.query(uri, null, null, null, null);
        if ((cursor == null) || (cursor.getCount() == 0)){
            throw new Resources.NotFoundException("Category not found");
        }
        cursor.moveToFirst();
        final String name =
                cursor.getString(cursor.getColumnIndex(CategoryEntry.COLUMN_NAME));
        final int color =
                cursor.getInt(cursor.getColumnIndex(CategoryEntry.COLUMN_COLOR));
        Category category = new Category(id, name, color);
        cursor.close();
        return category;
    }

    public void recordCompletedTask(long id) {
        final ContentValues values = new ContentValues();
        values.put(TasksEntry.COLUMN_COMPLETED, 1);
        resolver.update(TasksEntry.TASK_URI, values, TasksEntry._ID + " = ?",
                new String[]{ Long.toString(id) });
    }

    public void deleteTask(long id) {
        resolver.delete(TasksEntry.TASK_URI, TasksEntry._ID + " = ?",
                new String[] { Long.toString(id) });
    }

    public void deleteCategory(long id) {
        resolver.delete(CategoryEntry.CATEGORY_URI, CategoryEntry._ID + " = ?",
                new String[] { Long.toString(id) });
    }
}
