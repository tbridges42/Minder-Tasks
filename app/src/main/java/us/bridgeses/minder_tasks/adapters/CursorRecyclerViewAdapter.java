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

import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import us.bridgeses.minder_tasks.interfaces.Swappable;
import us.bridgeses.minder_tasks.storage.TasksProvider;

/**
 * Modified by Tony on 4/22/16
 *
 * This is a view adapter for recycler views backed by a cursor.
 */
public abstract class CursorRecyclerViewAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> implements Swappable {

    public static final int INVALID_ID = -1;

    private Cursor mCursor;

    private boolean mDataValid;

    private int mRowIdColumn;

    private int mCount;

    private ContentObserver mContentObserver;

    public CursorRecyclerViewAdapter(Cursor cursor) {
        mCursor = cursor;
        mDataValid = cursor != null;
        mRowIdColumn = mDataValid ? mCursor.getColumnIndex(BaseColumns._ID) : -1;
        mContentObserver = new NotifyingContentObserver(new Handler());
        setHasStableIds(true);
        if (mCursor != null) {
            mCursor.registerContentObserver(mContentObserver);
            mCount= mCursor.getCount();
        }
    }

    public Cursor getCursor() {
        return mCursor;
    }

    @Override
    public int getItemCount() {
        if (mDataValid && mCursor != null) {
            return mCount;
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (mDataValid && mCursor != null && mCursor.moveToPosition(position)) {
            return mCursor.getLong(mRowIdColumn);
        }
        return 0;
    }

    /**
     * Returns the position within the cursor of the selected element
     * @param id The id of the element to be found
     * @return the 0 based position of the element, or {@link #INVALID_ID} if the element is not found
     */
    public int getPosition(long id) {
        int currPos = mCursor.getPosition();
        int index = 0;
        mCursor.moveToFirst();
        do {
            if (mCursor.getInt(mCursor.getColumnIndex(BaseColumns._ID)) == id) {
                break;
            }
            index++;
            if (!mCursor.moveToNext()) {
                index = INVALID_ID;
                break;
            }
        } while (true);
        mCursor.moveToPosition(currPos);
        Log.d("getpos", Integer.toString(index));
        return index;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    /**
     * Binds the data within the cursor to the view in the viewholder. The cursor is already moved
     * to the correct position.
     * {@see android.support.v7.widget.RecyclerView#onBindViewHolder(ViewHolder, int)}
     * @param viewHolder Holds the view to be bound
     * @param cursor Holds the data to be bound
     */
    public abstract void onBindViewHolder(VH viewHolder, Cursor cursor);

    @Override
    public void onBindViewHolder(VH viewHolder, int position) {
        if (!mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            onBindViewHolder(viewHolder, null);
        }
        else {
            onBindViewHolder(viewHolder, mCursor);
        }
    }

    /**
     * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
     * closed.
     */
    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.  Unlike
     * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
     * closed.
     */
    public Cursor swapCursor(Cursor newCursor) {
        Log.d("swapping", "new cursor");
        if (newCursor == mCursor) {
            return null;
        }
        final Cursor oldCursor = mCursor;
        if (oldCursor != null && mContentObserver != null) {
            oldCursor.unregisterContentObserver(mContentObserver);
        }
        mCursor = newCursor;
        if (mCursor != null) {
            if (mContentObserver != null) {
                mCursor.registerContentObserver(mContentObserver);
            }
            mRowIdColumn = newCursor.getColumnIndexOrThrow(BaseColumns._ID);
            mDataValid = true;
            mCount = mCursor.getCount();
            notifyDataSetChanged();
        } else {
            mRowIdColumn = -1;
            mDataValid = false;
            mCount = 0;
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
        return oldCursor;
    }

    public ContentObserver getContentObserver() {
        return mContentObserver;
    }

    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
    }

    private class NotifyingContentObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public NotifyingContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mDataValid = true;
            List<String> sections = null;
            if (uri != null) {
                sections = uri.getPathSegments();
                Log.d("passeduri", uri.toString());
                Log.d("passedparts", sections.toString());
                Log.d("passedcount", Integer.toString(sections.size()));
            }
            if (sections != null && sections.size() == 3) {
                int pos = getPosition(Integer.parseInt(sections.get(sections.size() - 1)));
                Log.d("pos", Integer.toString(pos));
                switch (sections.get(1)) {
                    case TasksProvider.UPDATE:
                        notifyItemChanged(
                                pos
                        );
                        break;
                    case TasksProvider.DELETE:
                        notifyItemRemoved(
                                pos
                        );
                        notifyItemRangeChanged(pos, getItemCount() - pos - 1);
                        break;
                    case TasksProvider.INSERT:
                        notifyItemInserted(
                                getItemCount()
                        );
                        mCount++;
                        break;
                    default:
                        onChange(selfChange);
                }
            }
            else {
                onChange(selfChange);
            }
        }
    }
}