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

package us.bridgeses.minder_tasks.interfaces;

import android.app.LoaderManager;
import android.content.ContentResolver;

import us.bridgeses.minder_tasks.factories.TaskEditorFactory;
import us.bridgeses.minder_tasks.factories.ThemePersisterFactory;
import us.bridgeses.minder_tasks.startup.StartupFactory;
import us.bridgeses.minder_tasks.storage.PersistenceHelper;
import us.bridgeses.minder_tasks.storage.TasksLoader;

/**
 * Created by tbrid on 7/4/2016.
 */
public interface LocalContext {

    TaskEditorFactory getTaskEditorFactory();
    ThemePersisterFactory getThemePersisterFactory();
    StartupFactory getStartupFactory();
    PersistenceHelper getPersistenceHelper();
    ContentResolver getContentResolver();
    LoaderManager getLoaderManager();
}
