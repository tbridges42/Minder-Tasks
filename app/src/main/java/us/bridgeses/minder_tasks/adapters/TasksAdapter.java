package us.bridgeses.minder_tasks.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.Random;

import us.bridgeses.minder_tasks.R;

/**
 * Created by Tony on 3/24/2016.
 */
public class TasksAdapter extends CursorAdapter {

    private LayoutInflater layoutInflater;
    private int rowLayout;
    private String[] badStuffs;

    public TasksAdapter(Context context, Cursor c, boolean autoRequery, int rowLayout,
                        String[] badStuffs) {
        super(context, c, autoRequery);
        layoutInflater = (LayoutInflater.from(context));
        this.rowLayout = rowLayout;
        this.badStuffs = badStuffs;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View row = layoutInflater.inflate(rowLayout, parent, false);
        TextView badStuff = (TextView) row.findViewById(R.id.bad_stuff);
        badStuff.setText(badStuffs[new Random().nextInt(badStuffs.length)]);
        return row;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // TODO: populate row from cursor
    }
}
