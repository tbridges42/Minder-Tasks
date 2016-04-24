package us.bridgeses.minder_tasks.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
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
 *
 * This is a concrete @see CursorRecyclerViewAdapter for working with Tasks
 */

// TODO: How can this be generalized?
public class TaskRecyclerAdapter
        extends CursorRecyclerViewAdapter<TaskRecyclerAdapter.ViewHolder>
        implements TasksContract, Swappable {

    /**
     * This @see ViewHolder holds the views in the row layout and binds listeners to it
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        public final View itemView;
        public final TextView name;
        public final TextView badStuff;
        public final String badStuffString;
        public final TaskListener listener;
        // TODO: Add checkmark


        public ViewHolder(View itemView, String badStuffString, TaskListener listener) {
            super(itemView);
            this.itemView = itemView;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.findViewById(R.id.complete).setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.task);
            badStuff = (TextView) itemView.findViewById(R.id.bad_stuff);
            this.badStuffString = badStuffString;
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.complete) {
                listener.onItemComplete(TaskRecyclerAdapter.this.getItemId(getAdapterPosition()),
                        itemView);
            }
            else {
                listener.onItemClick(TaskRecyclerAdapter.this.getItemId(getAdapterPosition()), itemView);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            listener.onItemLongClick(TaskRecyclerAdapter.this.getItemId(getAdapterPosition()), itemView);
            return true;
        }
    }

    /**
     * This is a listener interface for receiving view actions
     */
    // TODO: Should this be broken out? Cleaned up?
    public interface TaskListener {
        void onItemClick(long id, View v);
        void onItemLongClick(long id, View v);
        void onItemDismiss(long id, View v);
        void onItemComplete(long id, View v);
    }

    private final String[] badStuff;
    private final TaskListener listener;

    public TaskRecyclerAdapter (Cursor c, String[] badStuff,
                                TaskListener listener) {
        super(c);
        this.badStuff = badStuff;
        this.listener = listener;
    }

    @Override
    public TaskRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.default_row, parent, false);
        ViewHolder vh = new ViewHolder(view, badStuff[new Random().nextInt(98)+1], listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        Log.d("TaskRecyclerAdapter", cursor.getString(cursor.getColumnIndex(TasksEntry.COLUMN_NAME)));
        viewHolder.badStuff.setText(viewHolder.badStuffString);
        viewHolder.name.setText(cursor.getString(cursor.getColumnIndex(TasksEntry.COLUMN_NAME)));
        if (cursor.getInt(cursor.getColumnIndex(TasksEntry.COLUMN_COMPLETED)) == 1) {
            // Grey out completed task
            viewHolder.itemView.setAlpha(0.8f);
            viewHolder.itemView.setBackgroundColor(Color.GRAY);

        }
        else {
            // Ungrey task if not completed
            viewHolder.itemView.setAlpha(1f);
            // TODO: How to set this to layout default?
            viewHolder.itemView.setBackgroundColor(Color.WHITE);
        }
    }
}
