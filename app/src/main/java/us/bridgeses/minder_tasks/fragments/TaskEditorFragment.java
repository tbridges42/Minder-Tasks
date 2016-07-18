package us.bridgeses.minder_tasks.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import us.bridgeses.dateview.DateView;
import us.bridgeses.minder_tasks.R;
import us.bridgeses.minder_tasks.adapters.CategorySpinnerAdapter;
import us.bridgeses.minder_tasks.interfaces.TaskEditor;
import us.bridgeses.minder_tasks.interfaces.TaskEditorViewTranslator;
import us.bridgeses.minder_tasks.models.Category;
import us.bridgeses.minder_tasks.models.Task;
import us.bridgeses.minder_tasks.storage.PersistenceHelper;
import us.bridgeses.minder_tasks.storage.PersistenceHelperImpl;
import us.bridgeses.minder_tasks.theme.DefaultTheme;
import us.bridgeses.minder_tasks.theme.Theme;
import us.bridgeses.minder_tasks.views.ColorEditText;
import us.bridgeses.slidedatetimepicker.SlideDateTimeListener;
import us.bridgeses.slidedatetimepicker.SlideDateTimePicker;

/**
 * Allows user to create new tasks or edit existing one passed in through newInstance
 */
public class TaskEditorFragment extends DialogFragment implements TaskEditorViewTranslator {

    private static final String CAT_DIALOG_TAG = "cat_dialog_tag";
    private static final String TAG = "TaskEditorFragment";

    private Task.Builder taskBuilder;
    private View container;
    private AlertDialog alertDialog;
    private ColorEditText inputTitle;
    private ColorEditText inputDuration;
    private Spinner categorySpinner;
    private DateView inputTime;
    private Theme theme = new DefaultTheme();
    private TaskEditor presenter;

    private void showCategoryDialog() {
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        final Fragment prev = getFragmentManager().findFragmentByTag(CAT_DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        final CategoryEditorFragment newFragment = CategoryEditorFragment.newInstance(null);
        newFragment.setListener(new SaveCategoryListener());
        newFragment.applyTheme(theme);
        newFragment.show(ft, CAT_DIALOG_TAG);
    }

    public static TaskEditorFragment newInstance(Task task, Theme theme) {
        TaskEditorFragment fragment = new TaskEditorFragment();
        Bundle bundle = new Bundle();
        if (task != null) {
            bundle.putParcelable("task", task);
        }
        if (theme != null) {
            bundle.putParcelable("theme", theme);
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if ((args != null) && (!args.isEmpty())) {
            Task passedTask = args.getParcelable("task");
            if (passedTask != null) {
                taskBuilder = new Task.Builder(passedTask);
            }
            else {
                taskBuilder = new Task.Builder("");
            }
            final Theme passedTheme = args.getParcelable("theme");
            if (passedTheme != null) {
                theme = passedTheme;
            }
        }
        else {
            taskBuilder = new Task.Builder("");
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    saveTask();
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();
                });
        @SuppressLint("InflateParams")
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.task_editor_layout, null);
        setHandles(v);
        initHandles(savedInstanceState);
        dialogBuilder.setView(v);
        alertDialog = dialogBuilder.create();
        return alertDialog;
    }

    /**
     * Initialize all the views that need to be modified
     * @param view the parent view of the fragment
     */
    public void setHandles(View view) {
        inputTitle = (ColorEditText) view.findViewById(R.id.input_title);
        inputDuration = (ColorEditText) view.findViewById(R.id.input_duration);
        categorySpinner = (Spinner) view.findViewById(R.id.input_category);
        inputTime = (DateView) view.findViewById(R.id.input_time);
        container = view.findViewById(R.id.task_container);
    }
    
    private void initHandles(Bundle savedInstanceState) {
        initSpinner(categorySpinner);

        inputTitle.setText(taskBuilder.getName());
        inputDuration.setText(taskBuilder.getDuration() + "");
        inputTime.setDate(taskBuilder.getDueTime());
        inputTime.setTag("date");
        inputTime.setClickable(true);
        inputTime.setOnClickListener(new DateClickListener());
        applyTheme(theme);
    }

    private void initSpinner(Spinner spinner) {
        CursorAdapter cAdapter = new CategorySpinnerAdapter(getActivity(),
                new PersistenceHelperImpl(getActivity().getContentResolver()).loadAllCategories(),
                0, new NewCategoryListener());
        spinner.setAdapter(cAdapter);
        if (taskBuilder.getCategory() == null) {
            spinner.setSelection(0);
        }
        else {
            long id = taskBuilder.getCategory().getId();
            int position = ((CategorySpinnerAdapter)spinner.getAdapter()).getPosition(id);
            spinner.setSelection(position);
        }
    }

    @Override
    public void applyTheme(@NonNull Theme theme) {
        this.theme = theme;
        List<View> children = getViews(container);
        Dialog dialog = getDialog();
        if (dialog != null) {
            children.add(((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE));
            children.add(((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE));
        }
        for (View child : children) {
            if (child == null) {
                continue;
            }
            child.setBackgroundColor(theme.getBackgroundColor());
            if (child instanceof TextView) {
                ((TextView)child).setTextColor(theme.getPrimaryFontColor());
                if (child instanceof ColorEditText) {
                    ((ColorEditText)child).setColor(theme.getHighlightColor());
                }
            }
        }
    }

    private List<View> getViews(View top) {
        List<View> visited = new ArrayList<>();
        List<View> unvisited = new ArrayList<>();
        unvisited.add(top);

        while (!unvisited.isEmpty()) {
            View child = unvisited.remove(0);
            visited.add(child);
            if (!(child instanceof ViewGroup)) continue;
            ViewGroup group = (ViewGroup) child;
            final int childCount = group.getChildCount();
            for (int i=0; i<childCount; i++) unvisited.add(group.getChildAt(i));
        }

        return visited;
    }

    private void saveTask() {
        PersistenceHelper helper = new PersistenceHelperImpl(getActivity().getContentResolver());
        long id = categorySpinner.getSelectedItemId();
        if (id != Spinner.INVALID_ROW_ID) {
            final Category category = helper.loadCategory(id);
            taskBuilder.setCategory(category);
        }
        taskBuilder.setDuration(Integer.parseInt(inputDuration.getText().toString()));
        taskBuilder.setName(inputTitle.getText().toString());
        helper.saveTask(taskBuilder.build());
        presenter.saveTask();
    }

    private void editDate() {
        SlideDateTimePicker.Builder dateTimePickerBuilder = new SlideDateTimePicker.Builder(
                getActivity().getFragmentManager()
        );
        dateTimePickerBuilder.setListener(new SetDateTimeListener());
        if (taskBuilder.getDueTime() != -1) {
            dateTimePickerBuilder.setInitialDate(new Date(taskBuilder.getDueTime()));
        }
        dateTimePickerBuilder.setIndicatorColor(theme.getHighlightColor());
        dateTimePickerBuilder.setTheme(theme.isDark() ?
                SlideDateTimePicker.HOLO_DARK :
                SlideDateTimePicker.HOLO_LIGHT);
        dateTimePickerBuilder.build().show();
    }

    @Override
    public void setPresenter(TaskEditor presenter) {
        this.presenter = presenter;
    }

    @Override
    public void show(Activity activityContext) {
        Log.d(TAG, "Showing fragment");
        this.show(activityContext.getFragmentManager(), TAG);
    }

    @Override
    public void setConfirmListener(Button.OnClickListener confirmListener) {

    }

    @Override
    public void setCancelListener(Button.OnClickListener cancelListener) {

    }

    @Override
    public void setNewCategoryListener(View.OnClickListener newCategoryListener) {

    }

    @Override
    public void setCategorySavedListener(CategoryEditorFragment.SaveListener savedListener) {

    }

    //<editor-fold desc="Listener Classes">
    private class SetDateTimeListener extends SlideDateTimeListener {

        @Override
        public void onDateTimeSet(Date date) {
            taskBuilder.setDueTime(date);
            inputTime.setDate(date);
        }

        @Override
        public void onDateTimeNone() {
            taskBuilder.setDueTime(0);
            inputTime.setDate(0);
        }
    }

    private class DateClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            editDate();
        }
    }

    private class NewCategoryListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showCategoryDialog();
        }
    }

    private class SaveCategoryListener implements CategoryEditorFragment.SaveListener {

        @Override
        public void handleSave(long id) {
            final CursorAdapter adapter = (CursorAdapter) categorySpinner.getAdapter();
            final Cursor newCursor = new PersistenceHelperImpl(getContext().getContentResolver())
                    .loadAllCategories();
            adapter.swapCursor(newCursor);
            // ID counts from 1, but selection counts from 0
            categorySpinner.setSelection((int)id - 1);
        }
    }
    //</editor-fold>
}
