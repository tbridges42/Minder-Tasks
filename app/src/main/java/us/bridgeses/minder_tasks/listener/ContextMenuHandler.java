package us.bridgeses.minder_tasks.listener;

import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import us.bridgeses.minder_tasks.R;

/**
 * Created by Tony on 4/24/2016.
 *
 * A handler to manage the lifecycle of a popupmenu.
 */
public class ContextMenuHandler implements PopupMenu.OnMenuItemClickListener{

    private long id;
    private RecyclerMenuListener listener;
    private PopupMenu popupMenu;

    public ContextMenuHandler(RecyclerMenuListener listener, int menuLayout, long id, View v) {
        this.listener = listener;
        this.id = id;
        popupMenu = new PopupMenu(v.getContext(), v);
        popupMenu.inflate(menuLayout);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    public void dismiss() {
        if (popupMenu != null) {
            popupMenu.dismiss();
            popupMenu = null;
        }
        listener = null;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return listener.onMenuItemClick(item, id);
    }
}
