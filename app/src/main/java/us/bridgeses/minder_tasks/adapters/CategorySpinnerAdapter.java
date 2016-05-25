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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import us.bridgeses.minder_tasks.R;

/**
 * An implementation of {@link CursorAdapterWithNew} where each row represents a
 * {@link us.bridgeses.minder_tasks.models.Category}, with a color and a name.
 */
public class CategorySpinnerAdapter extends CursorAdapterWithNew{

    public CategorySpinnerAdapter(Context context, Cursor c, int flags) {
        this(context, c, flags, null);
    }

    public CategorySpinnerAdapter(Context context, Cursor c, int flags,
                                  View.OnClickListener listener) {
        super(context, c, flags, R.layout.category_row, R.layout.new_row, listener);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final int color = cursor.getInt(cursor.getColumnIndex(COLUMN_COLOR));
        final String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
        final LinearLayout colorBlock = (LinearLayout) view.findViewById(R.id.color_block);
        final TextView nameText = (TextView) view.findViewById(R.id.category_name);
        colorBlock.setBackgroundColor(color);
        nameText.setText(name);
    }
}
