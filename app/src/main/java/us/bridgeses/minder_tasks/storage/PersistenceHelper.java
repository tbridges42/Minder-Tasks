package us.bridgeses.minder_tasks.storage;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;

import us.bridgeses.minder_tasks.models.Task;
import static us.bridgeses.minder_tasks.storage.TasksContract.TasksEntry;

/**
 * Created by Tony on 4/16/2016.
 */
public class PersistenceHelper {

    private final Context context;

    public PersistenceHelper(Context context) {
        this.context = context;
    }

    public long saveTask(Task task) {
        final ContentResolver resolver = context.getContentResolver();
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
        return Long.parseLong(resolver.insert(TasksEntry.TASK_URI, values).getLastPathSegment());
    }

    public Task loadTask(long id) throws Resources.NotFoundException {
        final ContentResolver resolver = context.getContentResolver();
        Uri uri = TasksEntry.TASK_URI.buildUpon().appendPath(Long.toString(id)).build();
        final Cursor cursor = resolver.query(uri, null, null, null, null);
        if ((cursor == null) || (cursor.getCount() == 0)){
            throw new Resources.NotFoundException("Task not found");
        }
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(TasksEntry.COLUMN_NAME));
        long created_time = cursor.getLong(cursor.getColumnIndex(TasksEntry.COLUMN_CREATION_TIME));
        int duration = cursor.getInt(cursor.getColumnIndex(TasksEntry.COLUMN_DURATION));
        long due_time = cursor.getLong(cursor.getColumnIndex(TasksEntry.COLUMN_DUE_TIME));
        Task task = new Task.Builder(name)
                .setId(id)
                .setCreationTime(created_time)
                .setDueTime(due_time)
                .setDuration(duration)
                .build();
        cursor.close();
        return task;
    }
}
