package us.bridgeses.minder_tasks.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import us.bridgeses.minder_tasks.R;
import us.bridgeses.minder_tasks.storage.TasksContract;

/**
 * Created by tbrid on 5/21/2016.
 */
public class CursorSpinnerAdapterWithNew extends CursorAdapter implements View.OnClickListener,
        TasksContract.CategoryEntry {

    private final Context context;
    private Cursor c;
    private final int rowLayout;
    private final int newLayout;
    private View newRowView;
    private final List<NewListener> listeners = new ArrayList<>();

    public interface NewListener {
        void createNew();
    }

    public CursorSpinnerAdapterWithNew(Context context, Cursor c, int flags, int rowLayout,
                            int newLayout) {
        super(context, c, flags);
        this.context = context;
        this.c = c;
        this.rowLayout = rowLayout;
        this.newLayout = newLayout;
    }

    public CursorSpinnerAdapterWithNew(Context context, Cursor c, int flags, int rowLayout,
                                       int newLayout, NewListener listener) {
        super(context, c, flags);
        this.context = context;
        this.c = c;
        this.rowLayout = rowLayout;
        this.newLayout = newLayout;
        this.listeners.add(listener);
    }

    public void addListener(NewListener listener) {
        listeners.add(listener);
    }

    public void removeListener(NewListener listener) {
        listeners.remove(listener);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(rowLayout, parent, false);
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

    @Override
    public int getCount() {
        // getCount here is equal to the size of the cursor, plus one for the new row.
        return super.getCount() + 1;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        boolean newRow = position == c.getCount();
        if (newRow) {
            final View v = LayoutInflater.from(context)
                    .inflate(R.layout.category_row, parent, false);
            return v;
        }
        if (convertView == newRowView) {
            convertView = null;
        }
        return super.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        boolean newRow = position == c.getCount();
        if (newRow) {
            if (newRowView == null) {
                newRowView = createNewRowView(parent);
            }
            return newRowView;
        }
        if (convertView == newRowView) {
            convertView = null;
        }
        return super.getDropDownView(position, convertView, parent);
    }

    private View createNewRowView(ViewGroup parent) {
        final View v = LayoutInflater.from(context).inflate(newLayout, parent, false);
        v.setOnClickListener(this);
        return v;
    }

    @Override
    public Cursor swapCursor(Cursor cursor) {
        this.c = cursor;
        return super.swapCursor(cursor);
    }

    @Override
    public void onClick(View v) {
        for (NewListener listener : listeners) {
            listener.createNew();
        }
    }

    public int getPosition(long id) {
        int currPos = c.getPosition();
        int index = 0;
        c.moveToFirst();
        do {
            if (c.getInt(c.getColumnIndex(_ID)) == id) {
                break;
            }
            index++;
            if (!c.moveToNext()) {
                index = -1;
            }
        } while (true);
        c.moveToPosition(currPos);
        return index;
    }
}
