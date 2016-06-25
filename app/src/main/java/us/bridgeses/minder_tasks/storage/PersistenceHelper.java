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

import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;

import us.bridgeses.minder_tasks.models.Category;
import us.bridgeses.minder_tasks.models.Task;

/**
 * Created by tbrid on 6/24/2016.
 */
public interface PersistenceHelper {
    Cursor getRecords(Uri uri, String[] projection, String selection,
                      String[] selectionArgs, String orderBy);

    long saveTask(Task task);

    long saveCategory(Category category);

    Task loadTask(long id) throws Resources.NotFoundException;

    Category loadCategory(long id) throws Resources.NotFoundException;

    Cursor loadAllCategories();

    void recordCompletedTask(long id);

    void deleteTask(long id);

    void deleteCategory(long id);
}
