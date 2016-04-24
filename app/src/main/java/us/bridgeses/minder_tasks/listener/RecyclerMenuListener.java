package us.bridgeses.minder_tasks.listener;

import android.view.MenuItem;

/**
 * Created by Tony on 4/24/2016.
 *
 * A menu listener that passes id
 */
public interface RecyclerMenuListener {
    boolean onMenuItemClick(MenuItem menuItem, long id);
}
