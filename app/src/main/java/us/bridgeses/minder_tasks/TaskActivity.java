package us.bridgeses.minder_tasks;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.test.mock.MockCursor;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Collections;
import java.util.Map;

import us.bridgeses.minder_tasks.adapters.Swappable;
import us.bridgeses.minder_tasks.adapters.TaskRecyclerAdapter;
import us.bridgeses.minder_tasks.adapters.TasksAdapter;
import us.bridgeses.minder_tasks.fragments.TaskEditorFragment;
import us.bridgeses.minder_tasks.listener.TaskOnClickListener;
import us.bridgeses.minder_tasks.models.Task;
import us.bridgeses.minder_tasks.startup.StartupFactory;
import us.bridgeses.minder_tasks.storage.PersistenceHelper;
import us.bridgeses.minder_tasks.storage.SwappableLoader;
import us.bridgeses.minder_tasks.storage.TasksContract;
import us.bridgeses.minder_tasks.storage.TasksLoader;

// TODO: Create preference class to manage saving and loading preferences

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class TaskActivity extends FragmentActivity implements View.OnClickListener,
    TaskEditorFragment.CloseListener, TaskRecyclerAdapter.TaskListener {

    private RecyclerView.Adapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.default_layout);


        findViewById(R.id.add_button).setOnClickListener(this);

        RecyclerView test_tasks = (RecyclerView) findViewById(R.id.test_tasks);
        adapter = new TaskRecyclerAdapter(this, null, createTestBadStuff(), this);
        test_tasks.setLayoutManager(new LinearLayoutManager(this));
        test_tasks.setAdapter(adapter);

        SwappableLoader loaderHandler = new TasksLoader(this, (Swappable) adapter, -1,
                TasksContract.TasksEntry.COLUMN_CREATION_TIME, "ASC");

        getLoaderManager().initLoader(SwappableLoader.TASK_LOADER, null, loaderHandler);

        SharedPreferences sp = getSharedPreferences("default",0);
        Map<String, Object> preferences = Collections.unmodifiableMap(sp.getAll());
        startup(preferences);
        applyStyle(preferences);
    }

    private String[] createTestBadStuff() {
        String[] badStuff = new String[100];
        for (int i = 0; i < badStuff.length; i++) {
            badStuff[i] = "Bad Stuff " + Integer.toString(i);
        }
        return badStuff;
    }

    private Cursor getCursor() {
        return getContentResolver().query(TasksContract.TasksEntry.TASK_URI,
                new String[] { TasksContract.TasksEntry._ID, TasksContract.TasksEntry.COLUMN_NAME},
                null, null, null);
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
            TaskEditorFragment fragment = new TaskEditorFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(R.id.contentPanel, fragment, "TAG").addToBackStack("TAG").commit();
        }
    }

    @Override
    public void close() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        if(fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemClick(long id, View v) {
        Log.d("Click", Long.toString(id));
        editTask(id);
    }

    public void editTask(long id) {
        Task task = new PersistenceHelper(this).loadTask(id);
        TaskEditorFragment fragment = TaskEditorFragment.newInstance(task);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.contentPanel, fragment, "TAG").addToBackStack("TAG").commit();
    }

    @Override
    public void onItemLongClick(long id, View v) {

    }

    @Override
    public void onItemDismiss(long id, View v) {

    }
}
