package us.bridgeses.minder_tasks.listener;

import android.view.View;

/**
 * Created by Tony on 4/18/2016.
 */
public interface TaskOnClickListener extends View.OnClickListener{
    public void onTaskClicked(long id);
}
