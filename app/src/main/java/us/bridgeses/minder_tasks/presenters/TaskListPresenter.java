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
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import java.util.Map;

import us.bridgeses.minder_tasks.R;
import us.bridgeses.minder_tasks.adapters.TaskRecyclerAdapter;
import us.bridgeses.minder_tasks.factories.TaskEditorFactory;
import us.bridgeses.minder_tasks.interfaces.LocalContext;
import us.bridgeses.minder_tasks.interfaces.TaskEditor;
import us.bridgeses.minder_tasks.interfaces.TaskListViewTranslator;
import us.bridgeses.minder_tasks.listener.ContextMenuHandler;
import us.bridgeses.minder_tasks.listener.RecyclerMenuListener;
import us.bridgeses.minder_tasks.listener.TaskSaveListener;
import us.bridgeses.minder_tasks.models.Task;
import us.bridgeses.minder_tasks.storage.PersistenceHelper;
import us.bridgeses.minder_tasks.storage.TasksContract;
import us.bridgeses.minder_tasks.storage.TasksLoader;
import us.bridgeses.minder_tasks.theme.Theme;

/**
 * Created by tbrid on 6/24/2016.
 */
public class TaskListPresenter {

    public static final int TASK_LOADER = 0;
    public static final int CATEGORY_LOADER = 1;

    private final TaskListViewTranslator taskList;
    private final PersistenceHelper persistenceHelper;
    private Theme theme;
    private ContextMenuHandler menuHandler;
    private final LoaderManager loaderManager;
    private final TaskRecyclerAdapter taskAdapter;
    private final TasksLoader taskCallback;
    private final ContentResolver contentResolver;
    private final TaskEditorFactory editorFactory;
    private TaskEditor taskEditor;

    public TaskListPresenter(TaskListViewTranslator taskList, LocalContext localContext,
                             TaskRecyclerAdapter adapter, TasksLoader taskCallback, Theme theme) {
        this.taskList = taskList;
        this.persistenceHelper = localContext.getPersistenceHelper();
        this.taskAdapter = adapter;
        this.contentResolver = localContext.getContentResolver();
        this.taskCallback = taskCallback;
        this.loaderManager = localContext.getLoaderManager();
        this.editorFactory = localContext.getTaskEditorFactory();
        this.theme = theme;
    }

    public void initialize() {
        taskList.applyTheme(theme);
        taskList.setOnSortSelectedListener(new TaskSortListener());
        taskList.setOnNewListener(new NewTaskListener());
        taskList.setMenuListener(new ContextMenuListener());
        //taskList.setOnCategorySelectedListener(this);
        taskAdapter.setListener(new TaskSelectedListener());
        taskList.setTasksAdapter(taskAdapter);
        loaderManager.initLoader(TASK_LOADER, null, taskCallback);
        contentResolver.registerContentObserver(TasksContract.TasksEntry.TASK_URI,
                true, new TaskObserver(new Handler()));
        contentResolver.registerContentObserver(TasksContract.TasksEntry.TASK_URI, true,
                taskAdapter.getContentObserver());
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

    public boolean onBackPressed() {
        if (menuHandler != null) {
            menuHandler.dismiss();
            menuHandler = null;
            return true;
        }
        if (taskEditor != null) {
            taskEditor.dismiss();
            taskEditor = null;
            return true;
        }
        return false;
    }

    private void editTask(long id) {
        Task task = loadTask(id);
        taskEditor = editorFactory.getEditor(taskList.getActivityContext(), task, theme);
        taskEditor.setTaskSaveListener(new TaskEditorListener());
        taskEditor.init();
        taskEditor.show(taskList.getActivityContext());
    }

    @Nullable
    private Task loadTask(long id) {
        if (id >= 0) {
            return persistenceHelper.loadTask(id);
        }
        else {
            return null;
        }
    }

    private void deleteTask(long id) {
        persistenceHelper.deleteTask(id);
    }

    private void changeTaskSort(String sortColumn, boolean asc) {
        taskCallback.setSortColumn(sortColumn);
        taskCallback.setAscending(asc);
        loaderManager.restartLoader(TASK_LOADER, null, taskCallback);
    }

    private void startup(Map<String, Object> preferences) {
        // TODO: 6/24/2016 How do we do this without a context?
        //new Handler().post(factory.getStartup(this));
    }


    //<editor-fold desc="Listener Classes">
    private class NewTaskListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            editTask(-1L);
        }
    }

    private class TaskSelectedListener implements TaskRecyclerAdapter.TaskListener {

        @Override
        public void onTaskCompleted(long id, View v) {
            persistenceHelper.recordCompletedTask(id);
        }

        @Override
        public void onTaskContextRequested(long id, View v) {
            // TODO: push up to TaskActivity
            if (menuHandler != null) {
                menuHandler.dismiss();
            }
            menuHandler = new ContextMenuHandler(new ContextMenuListener(),
                    R.menu.task_menu, id, v);
        }

        @Override
        public void onTaskDismissed(long id, View v) {
            // TODO: 6/24/2016
        }

        @Override
        public void onTaskSelected(long id, View v) {
            editTask(id);
        }
    }

    private class TaskSortListener implements AdapterView.OnItemSelectedListener {

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
    }

    private class ContextMenuListener implements RecyclerMenuListener {

        @Override
        public boolean onMenuItemClick(MenuItem menuItem, long id) {
            switch (menuItem.getItemId()) {
                case R.id.delete: {
                    deleteTask(id);
                }
            }
            return false;
        }
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

    private class TaskEditorListener implements TaskSaveListener {
        @Override
        public void onTaskSaved(Task task) {

        }

        @Override
        public void onCancelled() {

        }

        @Override
        public void onDelete() {

        }
    }
    //</editor-fold>
}
