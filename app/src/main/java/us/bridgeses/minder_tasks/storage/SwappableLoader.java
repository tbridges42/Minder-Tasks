package us.bridgeses.minder_tasks.storage;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import us.bridgeses.minder_tasks.adapters.CursorRecyclerViewAdapter;
import us.bridgeses.minder_tasks.adapters.Swappable;

/**
 * Created by Tony on 4/23/2016.
 */
public abstract class SwappableLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private final Swappable adapter;
    protected final Context context;

    public static final int TASK_LOADER = 0;
    public static final int CATEGORY_LOADER = 1;

    public SwappableLoader(Context context, Swappable adapter) {
        this.context = context.getApplicationContext();
        this.adapter = adapter;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("Swaping cursor", data.getCount() + " Records");
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
