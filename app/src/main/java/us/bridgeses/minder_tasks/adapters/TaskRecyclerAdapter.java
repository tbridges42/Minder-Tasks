package us.bridgeses.minder_tasks.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

import us.bridgeses.minder_tasks.R;
import us.bridgeses.minder_tasks.storage.TasksContract;

/**
 * Created by Tony on 4/17/2016.
 */
public class TaskRecyclerAdapter
        extends CursorRecyclerViewAdapter<TaskRecyclerAdapter.ViewHolder>
        implements TasksContract {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView badStuff;
        public String badStuffString;
        // TODO: Add checkmark


        public ViewHolder(View itemView, String badStuffString) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.task);
            badStuff = (TextView) itemView.findViewById(R.id.bad_stuff);
            this.badStuffString = badStuffString;
        }
    }

    private final Cursor c;
    private final String[] badStuff;

    public TaskRecyclerAdapter (Context context, Cursor c, String[] badStuff) {
        super(context, c);
        this.c = c;
        this.badStuff = badStuff;
    }

    @Override
    public TaskRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.default_row, parent, false);
        ViewHolder vh = new ViewHolder(view, badStuff[new Random().nextInt()]);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        Log.d("TaskRecyclerAdapter", cursor.getString(cursor.getColumnIndex(TasksEntry.COLUMN_NAME)));
        viewHolder.badStuff.setText(viewHolder.badStuffString);
        viewHolder.name.setText(cursor.getString(cursor.getColumnIndex(TasksEntry.COLUMN_NAME)));
    }
}
