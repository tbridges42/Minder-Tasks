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

import us.bridgeses.minder_tasks.R;

/**
 * Allows user to create new tasks
 */
public class TaskEditorFragment extends Fragment {

    EditText inputTitle;
    Spinner durationSpinner;
    Spinner categorySpinner;
    TextView inputTime;
    TextView inputDate;
    Button save;
    Button cancel;
    CloseListener callback;

    public interface CloseListener {
        void close();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Title", inputTitle.getText().toString());
        outState.putInt("Duration ID", durationSpinner.getSelectedItemPosition());
        outState.putInt("Category ID", categorySpinner.getSelectedItemPosition());
        outState.putString("Time", inputTime.getText().toString());
        outState.putString("Date", inputDate.getText().toString());
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
        durationSpinner = (Spinner) view.findViewById(R.id.input_duration);
        categorySpinner = (Spinner) view.findViewById(R.id.input_category);
        inputTime = (TextView) view.findViewById(R.id.input_time);
        inputDate = (TextView) view.findViewById(R.id.input_date);
        save = (Button) view.findViewById(R.id.save);
        cancel = (Button) view.findViewById(R.id.cancel);
    }
    
    public void initHandles(Bundle savedInstanceState) {
        initSpinner(durationSpinner, R.array.array_input_duration,
                android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item);
        initSpinner(categorySpinner, R.array.array_input_category_default,
                android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item);
        save.setOnClickListener(new saveListener());
        cancel.setOnClickListener(new cancelListener());
        if ((savedInstanceState == null) || (savedInstanceState.isEmpty())) {
            return;
        }
        inputTitle.setText(savedInstanceState.getString("Title"));
        durationSpinner.setSelection(savedInstanceState.getInt("Duration ID"), false);
        categorySpinner.setSelection(savedInstanceState.getInt("Duration ID"), false);
        inputTime.setText(savedInstanceState.getString("Time"));
        inputDate.setText(savedInstanceState.getString("Date"));
    }

    public void initSpinner(Spinner spinner, int array, int layout, int dropdownLayout) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                array, layout);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(dropdownLayout);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    public void save() {}

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
