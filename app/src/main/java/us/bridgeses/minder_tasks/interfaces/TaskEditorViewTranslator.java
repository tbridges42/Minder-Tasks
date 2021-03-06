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

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import us.bridgeses.minder_tasks.fragments.CategoryEditorFragment;

/**
 * Created by tbrid on 7/4/2016.
 */
public interface TaskEditorViewTranslator extends Themeable {

    void setPresenter(TaskEditor presenter);

    void show(Activity activityContext);

    void dismiss();

    void setConfirmListener(Button.OnClickListener confirmListener);

    void setCancelListener(Button.OnClickListener cancelListener);

    void setNewCategoryListener(View.OnClickListener newCategoryListener);

    void setCategorySavedListener(CategoryEditorFragment.SaveListener savedListener);
}
