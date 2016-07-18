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

package us.bridgeses.minder_tasks.viewtranslators;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Map;

import us.bridgeses.minder_tasks.R;
import us.bridgeses.minder_tasks.adapters.TaskRecyclerAdapter;
import us.bridgeses.minder_tasks.factories.LocalContextImpl;
import us.bridgeses.minder_tasks.interfaces.TaskListViewTranslator;
import us.bridgeses.minder_tasks.listener.ContextMenuHandler;
import us.bridgeses.minder_tasks.listener.RecyclerMenuListener;
import us.bridgeses.minder_tasks.presenters.TaskListPresenter;
import us.bridgeses.minder_tasks.startup.StartupFactory;
import us.bridgeses.minder_tasks.storage.PersistenceHelperImpl;
import us.bridgeses.minder_tasks.storage.TasksContract;
import us.bridgeses.minder_tasks.storage.TasksLoader;
import us.bridgeses.minder_tasks.theme.DarkDefaultTheme;
import us.bridgeses.minder_tasks.theme.DefaultTheme;
import us.bridgeses.minder_tasks.theme.Theme;

// TODO: Create preference class to manage saving and loading preferences

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class TaskActivity extends FragmentActivity implements TaskListViewTranslator {

    private View mainFrame;
    private View header;
    private ImageView logo;
    private TextView title;
    private TextView caption;
    private ContextMenuHandler menuHandler;
    private FloatingActionButton newButton;
    private RecyclerView taskList;
    private Spinner categorySpinner;
    private Spinner sortSpinner;
    private TaskListPresenter presenter;

    //<editor-fold desc="Listener Accessors">
    @Override
    public void setOnNewListener(View.OnClickListener onNewListener) {
        if (newButton != null) {
            newButton.setOnClickListener(onNewListener);
        }
    }

    @Override
    public void setMenuListener(RecyclerMenuListener contextMenuListener) {
        if (menuHandler != null) {
            menuHandler.setListener(contextMenuListener);
        }
    }

    @Override
    public void setOnCategorySelectedListener(
            AdapterView.OnItemSelectedListener onCategorySelectedListener) {
        if (categorySpinner != null) {
            categorySpinner.setOnItemSelectedListener(onCategorySelectedListener);
        }
    }

    @Override
    public void setOnSortSelectedListener(
            AdapterView.OnItemSelectedListener onSortSelectedListener) {
        if (sortSpinner != null) {
            sortSpinner.setOnItemSelectedListener(onSortSelectedListener);
        }
    }

    @Override
    public void setTasksAdapter(RecyclerView.Adapter adapter) {
        if (taskList != null) {
            taskList.setAdapter(adapter);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Lifecycle Callbacks">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.default_layout);

        mainFrame = findViewById(R.id.contentPanel);
        header = findViewById(R.id.header);
        title = (TextView) findViewById(R.id.title_bar);
        caption = (TextView) findViewById(R.id.title_caption);
        logo = (ImageView) findViewById(R.id.logo);
        newButton = (FloatingActionButton) findViewById(R.id.add_button);
        sortSpinner = (Spinner) findViewById(R.id.sort_spinner);
        taskList = (RecyclerView) findViewById(R.id.test_tasks);
        taskList.setItemAnimator(new DefaultItemAnimator());
        taskList.setLayoutManager(new LinearLayoutManager(this));

        final Theme theme = new DarkDefaultTheme();
        final TaskRecyclerAdapter taskAdapter = new TaskRecyclerAdapter(null, null, theme);
        final TasksLoader taskCallback = new TasksLoader(this, taskAdapter, -1,
                TasksContract.TaskViewEntry.COLUMN_CREATION_TIME, true);



        presenter = new TaskListPresenter(this,
                new LocalContextImpl(this),
                taskAdapter,
                taskCallback,
                theme);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.initialize();
    }

    @Override
    protected void onStop() {
        presenter.tearDown();
        if (menuHandler != null) {
            menuHandler.dismiss();
            menuHandler = null;
        }
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (!presenter.onBackPressed()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() != 0) {
                fragmentManager.popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }
    //</editor-fold>

    @Override
    public void displayFragment(DialogFragment fragment) {
        fragment.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public FragmentActivity getActivityContext() {
        return this;
    }

    @Override
    public void applyTheme(@NonNull Theme theme) {
        mainFrame.setBackgroundColor(theme.getBackgroundColor());
        header.setBackgroundColor(theme.getPrimaryColor());
        newButton.setBackgroundTintList(ColorStateList.valueOf(theme.getHighlightColor()));
        title.setText(theme.getTitle());
        title.setTextSize(theme.getHeadlineSize());
        title.setTextColor(theme.getHighlightFontColor());
        caption.setText(theme.getCaption());
        caption.setTextSize(theme.getTextSize());
        caption.setTextColor(theme.getHighlightFontColor());

    }
}
