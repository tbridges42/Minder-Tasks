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

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;

import us.bridgeses.minder_tasks.interfaces.Swappable;
import us.bridgeses.minder_tasks.storage.TasksContract;

/**
 * Created by Tony on 5/21/2016.
 *
 * This is an extension of CursorAdapter that allows for a constant option to always be present in
 * the view, regardless of the data backing of it. The intention is to provide a New button at the
 * bottom of spinners in the vein of Microsoft Dynamics CRM, but any other use could be adapted by
 * using a different {@link #mNewLayout} and implementing {@link #mListener} accordingly.
 */
public abstract class CursorAdapterWithNew extends CursorAdapter
        implements TasksContract.CategoryEntry, Swappable {

    public static final int INVALID_ID = -1;

    protected final Context mContext;
    protected Cursor mCursor;
    private final int mRowLayout;
    private final int mNewLayout;
    private final View mNewRowView;
    private View.OnClickListener mListener;

    public CursorAdapterWithNew(Context context, Cursor c, int flags, int rowLayout,
                                int newLayout, View.OnClickListener listener) {
        super(context, c, flags);
        mContext = context;
        mCursor = c;
        mRowLayout = rowLayout;
        mNewLayout = newLayout;
        mListener =  listener;
        mNewRowView = createNewRowView(null);
    }

    public void setListener(View.OnClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(mRowLayout, parent, false);
    }

    @Override
    public int getCount() {
        // getCount here is equal to the size of the cursor, plus one for the new row.
        return super.getCount() + 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == mNewRowView) {
            convertView = null;
        }
        return super.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        boolean newRow = position == mCursor.getCount();
        if (newRow) {
            return mNewRowView;
        }
        if (convertView == mNewRowView) {
            convertView = null;
        }
        return super.getDropDownView(position, convertView, parent);
    }

    private View createNewRowView(ViewGroup parent) {
        final View v = LayoutInflater.from(mContext).inflate(mNewLayout, parent, false);
        if (mListener != null) {
            v.setClickable(true);
            v.setOnClickListener(mListener);
        }
        return v;
    }

    @Override
    public Cursor swapCursor(Cursor cursor) {
        this.mCursor = cursor;
        return super.swapCursor(cursor);
    }

    /**
     * Get the position within the adapter for the item with {@code id}
     */
    public int getPosition(long id) {
        int currPos = mCursor.getPosition();
        int index = 0;
        mCursor.moveToFirst();
        do {
            if (mCursor.getInt(mCursor.getColumnIndex(_ID)) == id) {
                break;
            }
            index++;
            if (!mCursor.moveToNext()) {
                index = INVALID_ID;
            }
        } while (true);
        mCursor.moveToPosition(currPos);
        return index;
    }
}
