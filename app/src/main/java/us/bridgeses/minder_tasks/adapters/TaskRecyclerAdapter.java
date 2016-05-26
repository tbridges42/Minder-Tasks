/*
 * Copyright 2016 Tony Bridges
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package us.bridgeses.minder_tasks.adapters;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

import us.bridgeses.minder_tasks.R;
import us.bridgeses.minder_tasks.interfaces.Swappable;
import us.bridgeses.minder_tasks.storage.TasksContract;
import us.bridgeses.minder_tasks.theme.DefaultTheme;
import us.bridgeses.minder_tasks.theme.Theme;

/**
 * Created by Tony on 4/17/2016.
 *
 * This is a concrete {@link CursorRecyclerViewAdapter} for working with Tasks
 */

// TODO: How can this be generalized?
public class TaskRecyclerAdapter
        extends CursorRecyclerViewAdapter<TaskRecyclerAdapter.ViewHolder>
        implements TasksContract, Swappable {

    /**
     * This {@link ViewHolder} holds the views in the row layout and binds listeners to it
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        public final View itemView;
        public final TextView name;
        public final TextView badStuff;
        public final int badStuffIndex;
        public final TaskListener listener;
        // TODO: Add checkmark


        public ViewHolder(View itemView, int badStuffIndex, TaskListener listener) {
            super(itemView);
            this.itemView = itemView;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.findViewById(R.id.complete).setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.task);
            badStuff = (TextView) itemView.findViewById(R.id.bad_stuff);
            this.badStuffIndex = badStuffIndex;
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

    private final TaskListener listener;
    private Theme mTheme = new DefaultTheme();

    public TaskRecyclerAdapter (Cursor c, TaskListener listener) {
        this(c, listener, null);
    }

    public TaskRecyclerAdapter (Cursor c,
                                TaskListener listener, Theme theme) {
        super(c);
        this.listener = listener;
        if (theme != null) {
            mTheme = theme;
        }
    }

    @Override
    public TaskRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.default_row, parent, false);
        int index;
        if ((mTheme.getAllBadStuff() == null) || (mTheme.getAllBadStuff().length == 0)) {
            index = INVALID_ID;
        }
        else {
            index = new Random().nextInt(mTheme.getAllBadStuff().length);
        }
        return new ViewHolder(view, index, listener);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        if (viewHolder.badStuffIndex != INVALID_ID) {
            viewHolder.badStuff.setText(mTheme.getBadStuff(viewHolder.badStuffIndex));
        }
        else {
            viewHolder.badStuff.setText("");
        }
        viewHolder.badStuff.setTextColor(mTheme.getPrimaryFontColor());
        viewHolder.name.setText(cursor.getString(cursor.getColumnIndex(TasksEntry.COLUMN_NAME)));
        viewHolder.name.setTextColor(mTheme.getPrimaryFontColor());
        if (cursor.getInt(cursor.getColumnIndex(TasksEntry.COLUMN_COMPLETED)) == 1) {
            // Grey out completed task
            viewHolder.itemView.setAlpha(0.8f);
            viewHolder.itemView.setBackgroundColor(Color.GRAY);
        }
        else {
            // Ungrey task if not completed
            viewHolder.itemView.setAlpha(1f);
            viewHolder.itemView.setBackgroundColor(mTheme.getPrimaryColor());
        }
    }
}
