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

package us.bridgeses.minder_tasks.factories;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import us.bridgeses.minder_tasks.fragments.TaskEditorFragment;
import us.bridgeses.minder_tasks.interfaces.TaskEditor;
import us.bridgeses.minder_tasks.interfaces.TaskEditorViewTranslator;
import us.bridgeses.minder_tasks.models.Task;
import us.bridgeses.minder_tasks.presenters.TaskEditorPresenter;
import us.bridgeses.minder_tasks.theme.Theme;

/**
 * Created by tbrid on 7/4/2016.
 */
public class TaskEditorFactory {

    public TaskEditor getEditor(@NonNull Activity activity,
                                @Nullable Task task, @Nullable Theme theme) {
        TaskEditorViewTranslator viewTranslator = TaskEditorFragment.newInstance(task, theme);
        TaskEditor editor = new TaskEditorPresenter(viewTranslator);
        return editor;
    }
}
