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

package us.bridgeses.minder_tasks.factories;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Context;

import us.bridgeses.minder_tasks.interfaces.LocalContext;
import us.bridgeses.minder_tasks.startup.StartupFactory;
import us.bridgeses.minder_tasks.storage.PersistenceHelper;
import us.bridgeses.minder_tasks.storage.PersistenceHelperImpl;
import us.bridgeses.minder_tasks.storage.TasksLoader;

/**
 * Created by tbrid on 7/4/2016.
 */
public class LocalContextImpl implements LocalContext {

    private Activity activityContext;

    public LocalContextImpl(Activity activityContext) {
        this.activityContext = activityContext;
    }

    @Override
    public TaskEditorFactory getTaskEditorFactory() {
        return new TaskEditorFactory();
    }

    @Override
    public ThemePersisterFactory getThemePersisterFactory() {
        return new ThemePersisterFactory(activityContext.getApplicationContext());
    }

    @Override
    public StartupFactory getStartupFactory() {
        return new StartupFactory(activityContext.getApplicationContext());
    }

    @Override
    public PersistenceHelper getPersistenceHelper() {
        return new PersistenceHelperImpl(getContentResolver());
    }

    @Override
    public ContentResolver getContentResolver() {
        return activityContext.getContentResolver();
    }

    @Override
    public LoaderManager getLoaderManager() {
        return activityContext.getLoaderManager();
    }
}
