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

package us.bridgeses.minder_tasks.presenters;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import java.util.Map;

import us.bridgeses.minder_tasks.R;
import us.bridgeses.minder_tasks.adapters.TaskRecyclerAdapter;
import us.bridgeses.minder_tasks.fragments.TaskEditorFragment;
import us.bridgeses.minder_tasks.interfaces.TaskListViewTranslator;
import us.bridgeses.minder_tasks.listener.ContextMenuHandler;
import us.bridgeses.minder_tasks.listener.RecyclerMenuListener;
import us.bridgeses.minder_tasks.models.Task;
import us.bridgeses.minder_tasks.startup.StartupFactory;
import us.bridgeses.minder_tasks.storage.PersistenceHelper;
import us.bridgeses.minder_tasks.storage.TasksContract;
import us.bridgeses.minder_tasks.storage.TasksLoader;
import us.bridgeses.minder_tasks.theme.Theme;

/**
 * Created by tbrid on 6/24/2016.
 */
public class TaskListPresenter implements View.OnClickListener,
        TaskRecyclerAdapter.TaskListener, RecyclerMenuListener, AdapterView.OnItemSelectedListener{

    public static final int TASK_LOADER = 0;
    public static final int CATEGORY_LOADER = 1;

    private TaskListViewTranslator taskList;
    private PersistenceHelper persistenceHelper;
    private Theme theme;
    private ContextMenuHandler menuHandler;
    private LoaderManager loaderManager;
    private TaskRecyclerAdapter taskAdapter;
    private TasksLoader taskCallback;
    private ContentResolver contentResolver;

    public TaskListPresenter(TaskListViewTranslator taskList, PersistenceHelper persistenceHelper,
                             TaskRecyclerAdapter taskAdapter, ContentResolver contentResolver,
                             TasksLoader taskCallback, LoaderManager loaderManager, Theme theme) {
        this.taskList = taskList;
        this.persistenceHelper = persistenceHelper;
        this.taskAdapter = taskAdapter;
        this.contentResolver = contentResolver;
        this.taskCallback = taskCallback;
        this.loaderManager = loaderManager;
        this.theme = theme;
    }

    public void initialize() {
        taskList.applyTheme(theme);
        taskList.setOnSortSelectedListener(this);
        taskList.setOnNewListener(this);
        taskList.setMenuListener(this);
        //taskList.setOnCategorySelectedListener(this);
        taskAdapter.setListener(this);
        taskList.setTasksAdapter(taskAdapter);
        loaderManager.initLoader(TASK_LOADER, null, taskCallback);
        contentResolver.registerContentObserver(TasksContract.TasksEntry.TASK_URI,
                true, new TaskObserver(new Handler()));
    }

    public void tearDown() {
        taskList.setOnSortSelectedListener(null);
        taskList.setOnNewListener(null);
        taskList.setMenuListener(null);
        //taskList.setOnCategorySelectedListener(this);
        taskAdapter.setListener(null);
        taskList.setTasksAdapter(null);
        loaderManager.destroyLoader(TASK_LOADER);
    }

    @Override
    public void onClick(View v) {
        editTask(-1);
    }

    public boolean onBackPressed() {
        if (menuHandler != null) {
            menuHandler.dismiss();
            menuHandler = null;
            return true;
        }
        return false;
    }

    @Override
    public void onTaskSelected(long id, View v) {
        editTask(id);
    }

    private void editTask(long id) {
        Task task = null;
        if (id >= 0) {
            task = persistenceHelper.loadTask(id);
        }
        TaskEditorFragment fragment = TaskEditorFragment.newInstance(task);
        fragment.applyTheme(theme);
        taskList.displayFragment(fragment);
    }

    @Override
    public void onTaskContextRequested(long id, View v) {
        // TODO: push up to TaskActivity
        if (menuHandler != null) {
            menuHandler.dismiss();
        }
        menuHandler = new ContextMenuHandler(this, R.menu.task_menu, id, v);
    }

    @Override
    public void onTaskDismissed(long id, View v) {
        // TODO: 6/24/2016
    }

    @Override
    public void onTaskCompleted(long id, View v) {
        persistenceHelper.recordCompletedTask(id);
    }

    private void delete(long id) {
        persistenceHelper.deleteTask(id);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem, long id) {
        // Encapsulate? Should not need to know ids
        switch (menuItem.getItemId()) {
            case R.id.delete: {
                delete(id);
            }
        }
        return false;
    }

    private void changeTaskSort(String sortColumn, boolean asc) {
        taskCallback.setSortColumn(sortColumn);
        taskCallback.setAscending(asc);
        loaderManager.restartLoader(TASK_LOADER, null, taskCallback);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String value = (String) parent.getAdapter().getItem(position);
        // TODO: 6/24/2016 Get rid of hard-coded strings. Should this be here?
        switch (value) {
            case "Created":
                changeTaskSort(TasksContract.TaskViewEntry.COLUMN_CREATION_TIME, true);
                break;
            case "Due Date":
                changeTaskSort(TasksContract.TaskViewEntry.COLUMN_DUE_TIME, true);
                break;
            case "Duration":
                changeTaskSort(TasksContract.TaskViewEntry.COLUMN_DURATION, true);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO: 6/24/2016 What to do here?
    }

    private void startup(Map<String, Object> preferences) {
        StartupFactory factory = new StartupFactory(preferences);
        // TODO: 6/24/2016 How do we do this without a context?
        //new Handler().post(factory.getStartup(this));
    }

    private class TaskObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public TaskObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            loaderManager.restartLoader(TASK_LOADER, null, taskCallback);
        }
    }
}
