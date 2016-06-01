package us.bridgeses.minder_tasks.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Date;

import us.bridgeses.dateview.DateView;
import us.bridgeses.minder_tasks.R;
import us.bridgeses.minder_tasks.adapters.CategorySpinnerAdapter;
import us.bridgeses.minder_tasks.adapters.CursorAdapterWithNew;
import us.bridgeses.minder_tasks.models.Category;
import us.bridgeses.minder_tasks.models.Task;
import us.bridgeses.minder_tasks.storage.PersistenceHelper;
import us.bridgeses.minder_tasks.storage.TasksContract;
import us.bridgeses.slidedatetimepicker.SlideDateTimeListener;
import us.bridgeses.slidedatetimepicker.SlideDateTimePicker;

/**
 * Allows user to create new tasks or edit existing one passed in through newInstance
 */
public class TaskEditorFragment extends DialogFragment
                            implements View.OnClickListener,
                            CategoryEditorFragment.SaveListener {

    private Task.Builder taskBuilder;
    private EditText inputTitle;
    private EditText inputDuration;
    private Spinner categorySpinner;
    private DateView inputTime;
    private Button save;
    private Button cancel;
    private CloseListener callback;

    @Override
    public void onClick(View v) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        CategoryEditorFragment newFragment = CategoryEditorFragment.newInstance(null);
        newFragment.setListener(this);
        newFragment.show(ft, "dialog");
    }

    @Override
    public void handleSave(long id) {
        CursorAdapter adapter = (CursorAdapter) categorySpinner.getAdapter();
        final Cursor newCursor = getContext().getContentResolver().query(TasksContract.CategoryEntry.CATEGORY_URI,
                new String[] { TasksContract.CategoryEntry._ID,
                        TasksContract.CategoryEntry.COLUMN_NAME,
                        TasksContract.CategoryEntry.COLUMN_COLOR },
                null, null, null);
        adapter.swapCursor(newCursor);
        // ID counts from 1, but selection counts from 0
        categorySpinner.setSelection((int)id - 1);
    }

    public interface CloseListener {
        void close();
    }

    public static TaskEditorFragment newInstance(Task task) {
        TaskEditorFragment fragment = new TaskEditorFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("task",task);
        fragment.setArguments(bundle);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (CloseListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement CloseListener");
        }
    }

    @SuppressWarnings("deprecation") // This method is necessary for SDK < 23
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        try {
            callback = (CloseListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement CloseListener");
        }
    }

    @Override
    public void onDetach() {
        callback = null;
        super.onDetach();
    }


    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        save();
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
        inputTitle = (EditText) view.findViewById(R.id.input_title);
        inputDuration = (EditText) view.findViewById(R.id.input_duration);
        categorySpinner = (Spinner) view.findViewById(R.id.input_category);
        inputTime = (DateView) view.findViewById(R.id.input_time);
    }
    
    private void initHandles(Bundle savedInstanceState) {
        initSpinner(categorySpinner);

        inputTitle.setText(taskBuilder.getName());
        inputDuration.setText(taskBuilder.getDuration() + "");
        inputTime.setDate(taskBuilder.getDueTime());
        inputTime.setClickable(true);
        inputTime.setOnClickListener(new dateListener());
    }

    private void initSpinner(Spinner spinner) {
        CursorAdapter cAdapter = new CategorySpinnerAdapter(getContext(),
                getContext().getContentResolver().query(TasksContract.CategoryEntry.CATEGORY_URI,
                        new String[] { TasksContract.CategoryEntry._ID,
                                TasksContract.CategoryEntry.COLUMN_NAME,
                                TasksContract.CategoryEntry.COLUMN_COLOR },
                        null, null, null),
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

    public void save() {
        PersistenceHelper helper = new PersistenceHelper(getActivity());
        long id = categorySpinner.getSelectedItemId();
        if (id != Spinner.INVALID_ROW_ID) {
            final Category category = helper.loadCategory(id);
            taskBuilder.setCategory(category);
        }
        taskBuilder.setName(inputTitle.getText().toString());
        helper.saveTask(taskBuilder.build());
        callback.close();
    }

    public void cancel() {
        callback.close();
    }

    public void editDate() {
        SlideDateTimePicker.Builder dateTimePickerBuilder = new SlideDateTimePicker.Builder(
                getActivity().getSupportFragmentManager()
        );
        dateTimePickerBuilder.setListener(new SetDateTimeListener());
        if (taskBuilder.getDueTime() > 0) {
            dateTimePickerBuilder.setInitialDate(new Date(taskBuilder.getDueTime()));
        }
        dateTimePickerBuilder.build().show();
    }

    private class saveListener implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            save();
        }
    }

    private class cancelListener implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            cancel();
        }
    }

    private class dateListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            editDate();
        }
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
