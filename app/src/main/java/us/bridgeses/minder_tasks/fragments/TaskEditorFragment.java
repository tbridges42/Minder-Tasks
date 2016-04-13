package us.bridgeses.minder_tasks.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        return view;
    }
    
    public void setHandles(View view) {
        inputTitle = (EditText) view.findViewById(R.id.input_title);
        durationSpinner = (Spinner) view.findViewById(R.id.input_duration);
        categorySpinner = (Spinner) view.findViewById(R.id.input_category);
        inputTime = (TextView) view.findViewById(R.id.input_time);
        inputDate = (TextView) view.findViewById(R.id.input_date);
    }
    
    public void initHandles(Bundle savedInstanceState) {
        if ((savedInstanceState == null) || (savedInstanceState.isEmpty())) {
            return;
        }
        inputTitle.setText(savedInstanceState.getString("Title"));
        durationSpinner.setSelection(savedInstanceState.getInt("Duration ID"), false);
        categorySpinner.setSelection(savedInstanceState.getInt("Duration ID"), false);
        inputTime.setText(savedInstanceState.getString("Time"));
        inputDate.setText(savedInstanceState.getString("Date"));
    }
}
