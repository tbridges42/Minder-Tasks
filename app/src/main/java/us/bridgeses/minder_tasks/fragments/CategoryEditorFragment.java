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

package us.bridgeses.minder_tasks.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import petrov.kristiyan.colorpicker.ColorPicker;
import us.bridgeses.minder_tasks.R;
import us.bridgeses.minder_tasks.interfaces.Themeable;
import us.bridgeses.minder_tasks.models.Category;
import us.bridgeses.minder_tasks.storage.PersistenceHelper;
import us.bridgeses.minder_tasks.theme.DefaultTheme;
import us.bridgeses.minder_tasks.theme.Theme;

/**
 * Fragment for creating and modifying categories.
 */
public class CategoryEditorFragment extends DialogFragment
        implements View.OnClickListener, ColorPicker.OnFastChooseColorListener, Themeable {

    private static final String CATEGORY_TAG = "category";

    private Category category;
    private EditText nameInput;
    private LinearLayout colorBlock;
    private SaveListener listener;
    private Theme mTheme = new DefaultTheme();

    @Override
    public void applyTheme(@NonNull Theme theme) {
        if (mTheme == theme) {
            return;
        }
        mTheme = theme;
        applyTheme();
    }

    public interface SaveListener {
        void handleSave(long id);
    }

    public static CategoryEditorFragment newInstance(Category category) {
        CategoryEditorFragment frag = new CategoryEditorFragment();

        Bundle args  = new Bundle();
        args.putParcelable(CATEGORY_TAG, category);
        frag.setArguments(args);

        return frag;
    }

    public void setListener(SaveListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if ((args != null) && (!args.isEmpty())) {
            category = args.getParcelable(CATEGORY_TAG);
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        save();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.category_editor_layout, null);
        v.setBackgroundColor(mTheme.getPrimaryColor());
        nameInput = (EditText) v.findViewById(R.id.input_category_name);
        nameInput.setTextColor(mTheme.getPrimaryFontColor());
        nameInput.getBackground()
                .setColorFilter(mTheme.getHighlightColor(), PorterDuff.Mode.SRC_IN);
        colorBlock = (LinearLayout) v.findViewById(R.id.color_selector_block);
        colorBlock.setOnClickListener(this);
        if (category != null) {
            nameInput.setText(category.getName());
            colorBlock.setBackgroundColor(category.getColor());
            dialogBuilder.setTitle(R.string.edit_category);
        }
        else {
            dialogBuilder.setTitle(R.string.new_category);
        }
        dialogBuilder.setView(v);
        return dialogBuilder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        applyTheme();
    }

    private void applyTheme() {
        Dialog dialog = getDialog();
        LinearLayout titleLayout = (LinearLayout) getProtectedView(dialog,"android:id/topPanel");
        TextView title = (TextView) getProtectedView(dialog, "android:id/alertTitle");
        View bar = getProtectedView(dialog, "android:id/titleDivider");
        Button ok = (Button) getProtectedView(dialog, "android:id/button1");
        title.setTextColor(mTheme.getPrimaryFontColor());
        titleLayout.getBackground()
                .setColorFilter(mTheme.getPrimaryColor(), PorterDuff.Mode.MULTIPLY);
        bar.setBackgroundColor(mTheme.getHighlightColor());
        ok.setTextColor(mTheme.getPrimaryFontColor());
    }

    private View getProtectedView(Dialog dialog, String id) {
        return dialog.findViewById(dialog.getContext().getResources()
                    .getIdentifier(id, null, null));
    }

    private void save() {
        long id = -1;
        if (category != null) {
            id = category.getId();
        }
        Drawable background = colorBlock.getBackground();
        int color = ((ColorDrawable) background).getColor();
        category = new Category(id, nameInput.getText().toString(), color);
        PersistenceHelper helper = new PersistenceHelper(getContext());
        id = helper.saveCategory(category);
        if (listener != null) {
            listener.handleSave(id);
        }
    }

    @Override
    public void onClick(View v) {
        ColorPicker newFragment = new ColorPicker(getActivity());
        newFragment.setOnFastChooseColorListener(this);
        newFragment.show();
    }

    @Override
    public void setOnFastChooseColorListener(int position, int color) {
        colorBlock.setBackgroundColor(color);
    }
}
