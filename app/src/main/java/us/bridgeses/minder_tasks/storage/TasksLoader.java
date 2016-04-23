package us.bridgeses.minder_tasks.storage;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import us.bridgeses.minder_tasks.adapters.Swappable;

/**
 * Created by Tony on 4/23/2016.
 */
public class TasksLoader extends SwappableLoader implements TasksContract.TasksEntry {

    private final String sortColumn;
    private final int categoryId;
    private final String sortOrder;

    public TasksLoader(Context context, Swappable adapter, int categoryId, String sortColumn,
                       String sortOrder) {
        super(context, adapter);
        this.categoryId = categoryId;
        this.sortColumn = sortColumn;
        this.sortOrder = sortOrder;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final Uri uri = TASK_URI;
        String select = null;
        String[] selectArgs = null;
        if (categoryId != -1) {
            select = "( " + COLUMN_CATEGORY + "= ? )";
            selectArgs = new String[] {Integer.toString(categoryId)};
        }
        final String sort = COLUMN_COMPLETED + " ASC, " +
                sortColumn + " " + sortOrder;
        // TODO: Develop summary projection in TasksEntry
        return new CursorLoader(context, uri, null, null, null, sort);
    }
}
