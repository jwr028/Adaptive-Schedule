package com.example.shoppinglist;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.shoppinglist.Utils.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.example.shoppinglist.Adapter.ToDoAdapter;
import com.example.shoppinglist.Model.ToDoModel;
import com.example.shoppinglist.Utils.DatabaseHandler;

import java.util.Objects;

public class EditListName extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText newListName;
    private Button newNameSaveButton;

    private DataBaseHelper db;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_name, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newListName = Objects.requireNonNull(getView()).findViewById(R.id.newNameText);
        newNameSaveButton = getView().findViewById(R.id.newNameButton);

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String name = bundle.getString("name"); // sets current name in text field to edit
            newListName.setText(name);
            assert name != null;
            if(name.length()>0)
                newNameSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.black));
        }

        db = new DataBaseHelper(getActivity());
        db.openDatabase();

        // code to enable and change save button color when task has text entered
        newListName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newNameSaveButton.setEnabled(false);
                    newNameSaveButton.setTextColor(Color.GRAY);
                }
                else{
                    newNameSaveButton.setEnabled(true);
                    newNameSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // EDITING A LIST NAME
        final boolean finalIsUpdate = isUpdate;
        newNameSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = newListName.getText().toString();
                // updating a task
                if(finalIsUpdate){
                    db.updateListName(bundle.getInt("id"), name);
                }
                // inserting a new task
                else {
                    //ToDoModel task = new ToDoModel();
                    // inserting the info for database
                    //task.setTask(text);
                    //task.setStatus(0);
                    //task.setType("task"); //used to distinguish items and tasks

                    //db.insertTask(task);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
            ((DialogCloseListener)activity).handleDialogClose(dialog);
    }
}
