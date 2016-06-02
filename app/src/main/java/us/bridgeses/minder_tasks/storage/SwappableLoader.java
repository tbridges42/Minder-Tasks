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

    private final Swappable adapter;
    // Only store application context to prevent leaks of activities or fragments
    protected final Context context;

    public SwappableLoader(Context context, Swappable adapter) {
        this.context = context.getApplicationContext();
        this.adapter = adapter;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
