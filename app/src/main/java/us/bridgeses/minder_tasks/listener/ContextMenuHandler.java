package us.bridgeses.minder_tasks.listener;

import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import us.bridgeses.minder_tasks.R;

/**
 * Created by Tony on 4/24/2016.
 */
public class ContextMenuHandler implements PopupMenu.OnMenuItemClickListener{

    private long id;
    private final RecyclerMenuListener listener;
    private final int menuLayout;
    private PopupMenu popupMenu;

    public ContextMenuHandler(RecyclerMenuListener listener, int menuLayout) {
        this.listener = listener;
        this.menuLayout = menuLayout;
    }

    public void create(long id, View v) {
        this.id = id;
        if (popupMenu != null) {
            popupMenu.dismiss();
        }
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
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return listener.onMenuItemClick(item, id);
    }
}
