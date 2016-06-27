/*
 * Copyright 2016 Tony Bridges
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package us.bridgeses.minder_tasks.storage;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.util.Log;

import us.bridgeses.minder_tasks.interfaces.Swappable;

/**
 * Created by Tony on 4/23/2016.
 *
 * An abstract class implementing LoaderCallbacks capable of managing a cursor for any Swappable
 */
public abstract class SwappableLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private Swappable adapter;
    // Only store application context to prevent leaks of activities or fragments
    protected final Context context;
    protected String sortColumn;
    protected boolean ascending;
    protected String filterColumn;
    protected String filterValue;

    public SwappableLoader(Context context, Swappable adapter, String sortColumn,
                           boolean ascending, String filterColumn, String filterValue) {
        this.context = context.getApplicationContext();
        this.adapter = adapter;
        this.sortColumn = sortColumn;
        this.ascending = ascending;
        this.filterColumn = filterColumn;
        this.filterValue = filterValue;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("loader", "Loader finished");
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public void setAdapter(Swappable adapter) { this.adapter = adapter; }
}
