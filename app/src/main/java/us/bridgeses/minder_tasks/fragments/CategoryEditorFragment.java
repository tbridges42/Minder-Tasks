package us.bridgeses.minder_tasks.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import petrov.kristiyan.colorpicker.ColorPicker;
import us.bridgeses.minder_tasks.R;
import us.bridgeses.minder_tasks.models.Category;
import us.bridgeses.minder_tasks.storage.PersistenceHelper;

/**
 * Created by tbrid on 5/22/2016.
 */
public class CategoryEditorFragment extends DialogFragment implements View.OnClickListener, ColorPicker.OnFastChooseColorListener {

    private Category category;
    private EditText nameInput;
    private LinearLayout colorBlock;
    private SaveListener listener;

    public interface SaveListener {
        void handleSave(long id);
    }

    public static CategoryEditorFragment newInstance(Category category) {
        CategoryEditorFragment frag = new CategoryEditorFragment();

        Bundle args  = new Bundle();
        args.putParcelable("category", category);
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
            category = args.getParcelable("category");
        }
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.category_editor_layout, parent, false);

        return v;
    }*/

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
                })
                .setTitle("Title");
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.category_editor_layout, null);
        nameInput = (EditText) v.findViewById(R.id.input_category_name);
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

    public void save() {
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
