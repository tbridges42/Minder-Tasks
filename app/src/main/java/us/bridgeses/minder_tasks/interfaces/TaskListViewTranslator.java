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

package us.bridgeses.minder_tasks.interfaces;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import us.bridgeses.minder_tasks.listener.RecyclerMenuListener;

/**
 * Created by tbrid on 6/24/2016.
 */
public interface TaskListViewTranslator extends Themeable {
    void setOnNewListener(View.OnClickListener onNewListener);

    void setMenuListener(RecyclerMenuListener contextMenuListener);

    void setOnCategorySelectedListener(
            AdapterView.OnItemSelectedListener onCategorySelectedListener);

    void setOnSortSelectedListener(
            AdapterView.OnItemSelectedListener onSortSelectedListener);

    void setTasksAdapter(RecyclerView.Adapter adapter);

    void displayFragment(DialogFragment fragment);

    FragmentActivity getActivityContext();
}
