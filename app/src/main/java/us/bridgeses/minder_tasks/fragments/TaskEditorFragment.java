package us.bridgeses.minder_tasks.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;

import java.util.Date;

import us.bridgeses.dateview.DateView;
import us.bridgeses.minder_tasks.R;
import us.bridgeses.minder_tasks.adapters.CategorySpinnerAdapter;
import us.bridgeses.minder_tasks.interfaces.Themeable;
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
public class TaskEditorFragment extends DialogFragment
                            implements View.OnClickListener,
                            CategoryEditorFragment.SaveListener, Themeable {

    private static final String CAT_DIALOG_TAG = "cat_dialog_tag";

    private Task.Builder taskBuilder;
    private ColorEditText inputTitle;
    private ColorEditText inputDuration;
    private Spinner categorySpinner;
    private DateView inputTime;
    private Theme theme = new DefaultTheme();

    @Override
    public void onClick(View v) {
        if (v.getTag() == "date") {
            editDate();
        }
        else {
            showCategoryDialog();
        }
    }

    private void showCategoryDialog() {
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        final Fragment prev = getFragmentManager().findFragmentByTag(CAT_DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        final CategoryEditorFragment newFragment = CategoryEditorFragment.newInstance(null);
        newFragment.setListener(this);
        newFragment.show(ft, CAT_DIALOG_TAG);
    }

    @Override
    public void handleSave(long id) {
        final CursorAdapter adapter = (CursorAdapter) categorySpinner.getAdapter();
        final Cursor newCursor = new PersistenceHelperImpl(getContext().getContentResolver())
                .loadAllCategories();
        adapter.swapCursor(newCursor);
        // ID counts from 1, but selection counts from 0
        categorySpinner.setSelection((int)id - 1);
    }

    public static TaskEditorFragment newInstance(Task task) {
        TaskEditorFragment fragment = new TaskEditorFragment();
        if (task != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("task", task);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if ((args != null) && (!args.isEmpty())) {
            taskBuilder = new Task.Builder((Task) args.getParcelable("task"));
            Log.d("onCreate", taskBuilder.getName());
        }
        else {
            taskBuilder = new Task.Builder("");
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        save();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.task_editor_layout, null);
        setHandles(v);
        initHandles(savedInstanceState);
        dialogBuilder.setView(v);
        return dialogBuilder.create();
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
    }
    
    private void initHandles(Bundle savedInstanceState) {
        initSpinner(categorySpinner);

        inputTitle.setText(taskBuilder.getName());
        inputDuration.setText(taskBuilder.getDuration() + "");
        inputTime.setDate(taskBuilder.getDueTime());
        inputTime.setTag("date");
        inputTime.setClickable(true);
        inputTime.setOnClickListener(this);
        applyTheme(theme);
    }

    private void initSpinner(Spinner spinner) {
        CursorAdapter cAdapter = new CategorySpinnerAdapter(getContext(),
                new PersistenceHelperImpl(getContext().getContentResolver()).loadAllCategories(),
                0, this);
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
    public void applyTheme(Theme theme) {
        inputTitle.setColor(theme.getHighlightColor());
        inputDuration.setColor(theme.getHighlightColor());
    }

    private void save() {
        PersistenceHelper helper = new PersistenceHelperImpl(getActivity().getContentResolver());
        long id = categorySpinner.getSelectedItemId();
        if (id != Spinner.INVALID_ROW_ID) {
            final Category category = helper.loadCategory(id);
            taskBuilder.setCategory(category);
        }
        taskBuilder.setDuration(Integer.parseInt(inputDuration.getText().toString()));
        taskBuilder.setName(inputTitle.getText().toString());
        helper.saveTask(taskBuilder.build());
    }

    private void editDate() {
        SlideDateTimePicker.Builder dateTimePickerBuilder = new SlideDateTimePicker.Builder(
                getActivity().getSupportFragmentManager()
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
}
