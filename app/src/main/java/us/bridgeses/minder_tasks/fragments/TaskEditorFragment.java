package us.bridgeses.minder_tasks.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import us.bridgeses.minder_tasks.R;
import us.bridgeses.minder_tasks.models.Task;
import us.bridgeses.minder_tasks.storage.PersistenceHelper;

/**
 * Allows user to create new tasks
 */
public class TaskEditorFragment extends Fragment {

    Task.Builder taskBuilder;
    EditText inputTitle;
    EditText inputDuration;
    Spinner categorySpinner;
    TextView inputTime;
    TextView inputDate;
    Button save;
    Button cancel;
    CloseListener callback;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_editor_layout, container, false);
        setHandles(view);
        initHandles(savedInstanceState);
        Log.d("onCreateView", "View created");
        return view;
    }
    
    public void setHandles(View view) {
        inputTitle = (EditText) view.findViewById(R.id.input_title);
        inputDuration = (EditText) view.findViewById(R.id.input_duration);
        categorySpinner = (Spinner) view.findViewById(R.id.input_category);
        inputTime = (TextView) view.findViewById(R.id.input_time);
        inputDate = (TextView) view.findViewById(R.id.input_date);
        save = (Button) view.findViewById(R.id.save);
        cancel = (Button) view.findViewById(R.id.cancel);
    }
    
    public void initHandles(Bundle savedInstanceState) {
        initSpinner(categorySpinner, R.array.array_input_category_default,
                android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item);
        save.setOnClickListener(new saveListener());
        cancel.setOnClickListener(new cancelListener());
        if ((savedInstanceState == null) || (savedInstanceState.isEmpty())) {
            return;
        }
        inputTitle.setText(taskBuilder.getName());
        inputDuration.setText(taskBuilder.getDuration());
        inputTime.setText(savedInstanceState.getString("Time"));
        inputDate.setText(savedInstanceState.getString("Date"));
    }

    public void initSpinner(Spinner spinner, int array, int layout, int dropdownLayout) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                array, layout);
        adapter.setDropDownViewResource(dropdownLayout);
        spinner.setAdapter(adapter);
    }

    public void save() {
        PersistenceHelper helper = new PersistenceHelper(getActivity());
        taskBuilder.setName(inputTitle.getText().toString());
        helper.saveTask(taskBuilder.build());
        callback.close();
    }

    public void cancel() {
        callback.close();
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
}
