package us.bridgeses.minder_tasks.adapters;

import android.database.Cursor;

/**
 * Created by Tony on 4/23/2016.
 *
 * This is a tag interface for classes that have swappable cursors.
 */
public interface Swappable {
    Cursor swapCursor(Cursor newCursor);
}
