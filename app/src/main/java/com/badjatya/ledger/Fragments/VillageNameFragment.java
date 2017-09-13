package com.badjatya.ledger.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.badjatya.ledger.R;
import com.badjatya.ledger.activities.SearchAndAdd;

/**
 * Created by Tanmay on 10/22/2016.
 */
public class VillageNameFragment extends DialogFragment{

    private EditText villageEditText;
    private TextView tagTextView;
    private Button doneButton;
    OnNameRecieved onNameRecievedInterface;

    public VillageNameFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public interface OnNameRecieved
    {
        public void onNameRecieved(String name);
    }
    public static VillageNameFragment newInstance(String param) {
        VillageNameFragment frag = new VillageNameFragment();
        Bundle args = new Bundle();
        args.putString("param", param);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        onNameRecievedInterface=(SearchAndAdd)getActivity();
        return inflater.inflate(R.layout.dialog_village_name, container);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        String param = getArguments().getString("param", "Name");
        villageEditText = (EditText) view.findViewById(R.id.dialog_village_name_edittext);
        tagTextView=(TextView)view.findViewById(R.id.dialog_village_name_textView);
        doneButton=(Button)view.findViewById(R.id.dialog_village_name_done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(villageEditText.getText().toString().trim().length()>0) {
                    onNameRecievedInterface.onNameRecieved(villageEditText.getText().toString().trim());
                    dismiss();
                }
            }
        });

        tagTextView.setText(param);
        villageEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        /*Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    */
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        return super.onCreateDialog(savedInstanceState);
    }
}
