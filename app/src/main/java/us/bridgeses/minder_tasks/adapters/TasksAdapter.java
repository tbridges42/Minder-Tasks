package us.bridgeses.minder_tasks.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.Random;

import us.bridgeses.minder_tasks.R;
import us.bridgeses.minder_tasks.storage.TasksContract;

/**
 * Created by Tony on 3/24/2016.
 */
public class TasksAdapter extends CursorAdapter implements TasksContract.TasksEntry {

    private LayoutInflater layoutInflater;
    private int rowLayout;
    private String[] badStuffs;

    public TasksAdapter(Context context, Cursor c, boolean autoRequery, int rowLayout,
                        String[] badStuffs) {
        super(context, c, autoRequery);
        layoutInflater = (LayoutInflater.from(context));
        this.rowLayout = rowLayout;
        this.badStuffs = badStuffs;
        Log.d("taskAdapter", "Created task adapter");
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View row = layoutInflater.inflate(rowLayout, parent, false);
        row.setTag(badStuffs[new Random().nextInt(badStuffs.length)]);
        return row;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView) view.findViewById(R.id.task);
        TextView badStuff = (TextView) view.findViewById(R.id.bad_stuff);
        name.setText(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
        if (view.getTag() != null) {
            badStuff.setText((String)view.getTag());
        }
        Log.d("taskAdapter", "Bound view");
    }
}
