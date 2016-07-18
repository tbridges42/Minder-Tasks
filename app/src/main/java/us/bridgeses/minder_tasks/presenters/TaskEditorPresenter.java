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

package us.bridgeses.minder_tasks.presenters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import us.bridgeses.minder_tasks.interfaces.TaskEditor;
import us.bridgeses.minder_tasks.interfaces.TaskEditorViewTranslator;
import us.bridgeses.minder_tasks.listener.TaskSaveListener;
import us.bridgeses.minder_tasks.models.Task;
import us.bridgeses.minder_tasks.theme.Theme;

/**
 * Created by tbrid on 7/4/2016.
 */
public class TaskEditorPresenter implements TaskEditor {

    private TaskEditorViewTranslator viewTranslator;

    public TaskEditorPresenter(TaskEditorViewTranslator viewTranslator) {
        this.viewTranslator = viewTranslator;
        viewTranslator.setPresenter(this);
    }

    public void init() {

    }

    @Override
    public void show(Activity activityContext) {
        viewTranslator.show(activityContext);
    }

    @Override
    public void dismiss() {

    }

    @Override
    public void saveTask() {
        Log.d("Presenter","Saving task");
    }

    @Override
    public void setTask(Task task) {

    }

    @Override
    public void setTaskSaveListener(TaskSaveListener listener) {

    }

    @Override
    public void applyTheme(@NonNull Theme theme) {

    }
}
