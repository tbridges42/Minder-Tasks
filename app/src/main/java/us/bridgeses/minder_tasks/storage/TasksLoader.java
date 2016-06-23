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

import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import us.bridgeses.minder_tasks.interfaces.Swappable;

/**
 * Created by Tony on 4/23/2016.
 *
 * A concrete SwappableLoader for managing a cursor of Tasks for a Swappable
 */
public class TasksLoader extends SwappableLoader implements TasksContract.TasksEntry {

    private int categoryId;

    public TasksLoader(Context context, Swappable adapter, int categoryId, String sortColumn,
                       boolean ascending) {
        super(context, adapter, sortColumn, ascending, COLUMN_CATEGORY,
                Integer.toString(categoryId));
        this.categoryId = categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final Uri uri = TASK_URI;
        String select = null;
        String[] selectArgs = null;
        if (categoryId != -1) {
            select = "( " + filterColumn + "= ? )";
            selectArgs = new String[] {filterValue};
        }
        // Sort first incomplete followed by complete, then selected sort
        final String sort = COLUMN_COMPLETED + " ASC, " +
                sortColumn + " " + (ascending ? "ASC" : "DESC");
        return new CursorLoader(context, uri, SUMMARY_PROJECTION, select, selectArgs, sort);
    }
}
