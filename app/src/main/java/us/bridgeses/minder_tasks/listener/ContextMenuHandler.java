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

package us.bridgeses.minder_tasks.listener;

import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

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

    public void setListener(RecyclerMenuListener listener) {
        this.listener = listener;
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
