package us.bridgeses.minder_tasks;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.test.mock.MockCursor;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Collections;
import java.util.Map;

import us.bridgeses.minder_tasks.adapters.TasksAdapter;
import us.bridgeses.minder_tasks.fragments.TaskEditorFragment;
import us.bridgeses.minder_tasks.startup.StartupFactory;

// TODO: Create preference class to manage saving and loading preferences

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class TaskActivity extends FragmentActivity implements View.OnClickListener,
    TaskEditorFragment.CloseListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.default_layout);


        findViewById(R.id.add_button).setOnClickListener(this);

        SharedPreferences sp = getSharedPreferences("default",0);
        Map<String, Object> preferences = Collections.unmodifiableMap(sp.getAll());
        startup(preferences);
        applyStyle(preferences);
    }


    private void startup(Map<String, Object> preferences) {
        StartupFactory factory = new StartupFactory(preferences);
        new Handler().post(factory.getStartup(this));
    }

    private void applyStyle(Map<String, Object> preferences) {

    }


    @Override
    public void onClick(View v) {
        Log.d("OnClick", "Button Clicked");
        TaskEditorFragment fragment = new TaskEditorFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.contentPanel, fragment,"TAG").addToBackStack("TAG").commit();
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
}
