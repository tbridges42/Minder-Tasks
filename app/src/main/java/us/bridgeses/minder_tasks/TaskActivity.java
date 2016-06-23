package us.bridgeses.minder_tasks;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.Collections;
import java.util.Map;

import us.bridgeses.minder_tasks.interfaces.Swappable;
import us.bridgeses.minder_tasks.adapters.TaskRecyclerAdapter;
import us.bridgeses.minder_tasks.fragments.TaskEditorFragment;
import us.bridgeses.minder_tasks.listener.ContextMenuHandler;
import us.bridgeses.minder_tasks.listener.RecyclerMenuListener;
import us.bridgeses.minder_tasks.models.Task;
import us.bridgeses.minder_tasks.startup.StartupFactory;
import us.bridgeses.minder_tasks.storage.PersistenceHelper;
import us.bridgeses.minder_tasks.storage.SwappableLoader;
import us.bridgeses.minder_tasks.storage.TasksContract;
import us.bridgeses.minder_tasks.storage.TasksLoader;
import us.bridgeses.minder_tasks.theme.DefaultTheme;
import us.bridgeses.minder_tasks.theme.Theme;

// TODO: Create preference class to manage saving and loading preferences

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class TaskActivity extends FragmentActivity implements View.OnClickListener,
        TaskRecyclerAdapter.TaskListener, RecyclerMenuListener, AdapterView.OnItemSelectedListener {

    public static final int TASK_LOADER = 0;
    public static final int CATEGORY_LOADER = 1;

    private RecyclerView.Adapter adapter;
    private ContextMenuHandler menuHandler;
    private SwappableLoader taskCallback;
    private Theme theme = new DefaultTheme();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.default_layout);

        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.add_button);
        addButton.setOnClickListener(this);
        addButton.setBackgroundTintList(ColorStateList.valueOf(theme.getHighlightColor()));

        RecyclerView test_tasks = (RecyclerView) findViewById(R.id.test_tasks);
        adapter = new TaskRecyclerAdapter(null, this);
        test_tasks.setLayoutManager(new LinearLayoutManager(this));
        test_tasks.setAdapter(adapter);

        taskCallback = new TasksLoader(this, (Swappable) adapter, -1,
                TasksContract.TasksEntry.COLUMN_CREATION_TIME, true);

        getLoaderManager().initLoader(TASK_LOADER, null, taskCallback);

        SharedPreferences sp = getSharedPreferences("default",0);
        Map<String, Object> preferences = Collections.unmodifiableMap(sp.getAll());
        startup(preferences);
        applyStyle(preferences);

        Spinner sortSpinner = (Spinner) findViewById(R.id.sort_spinner);
        sortSpinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void onStop() {
        if (menuHandler != null) {
            menuHandler.dismiss();
            menuHandler = null;
        }
        super.onStop();
    }

    private void startup(Map<String, Object> preferences) {
        StartupFactory factory = new StartupFactory(preferences);
        new Handler().post(factory.getStartup(this));
    }

    private void applyStyle(Map<String, Object> preferences) {

    }


    @Override
    public void onClick(View v) {
        if (v instanceof FloatingActionButton) {
            DialogFragment newFragment = new TaskEditorFragment();
            newFragment.show(getSupportFragmentManager(), "dialog");
        }
    }

    @Override
    public void onBackPressed() {
        if (menuHandler != null) {
            menuHandler.dismiss();
            menuHandler = null;
            return;
        }
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemClick(long id, View v) {
        editTask(id);
    }

    private void editTask(long id) {
        Task task = new PersistenceHelper(this).loadTask(id);
        TaskEditorFragment fragment = TaskEditorFragment.newInstance(task);
        fragment.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onItemLongClick(long id, View v) {
        if (menuHandler != null) {
            menuHandler.dismiss();
        }
        menuHandler = new ContextMenuHandler(this, R.menu.task_menu, id, v);
    }

    @Override
    public void onItemDismiss(long id, View v) {

    }

    @Override
    public void onItemComplete(long id, View v) {
        PersistenceHelper persistenceHelper = new PersistenceHelper(this);
        persistenceHelper.recordCompletedTask(id);
    }

    private void delete(long id) {
        PersistenceHelper persistenceHelper = new PersistenceHelper(this);
        persistenceHelper.deleteTask(id);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem, long id) {
        switch (menuItem.getItemId()) {
            case R.id.delete: {
                delete(id);
            }
        }
        return false;
    }

    private void changeSort(String sortColumn, boolean asc) {
        taskCallback.setSortColumn(sortColumn);
        taskCallback.setAscending(asc);
        getLoaderManager().restartLoader(TASK_LOADER, null, taskCallback);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String value = (String) parent.getAdapter().getItem(position);
        switch (value) {
            case "Created":
                changeSort(TasksContract.TasksEntry.COLUMN_CREATION_TIME, true);
                break;
            case "Due Date":
                changeSort(TasksContract.TasksEntry.COLUMN_DUE_TIME, true);
                break;
            case "Duration":
                changeSort(TasksContract.TasksEntry.COLUMN_DURATION, true);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
