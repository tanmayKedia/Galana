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
import android.widget.Toast;

import com.badjatya.ledger.R;
import com.badjatya.ledger.Utils.Validator;
import com.badjatya.ledger.activities.GalanaActivity;

/**
 * Created by Tanmay on 10/18/2016.
 */
public class RateFragment extends DialogFragment{

    private EditText goldEditText;
    private EditText silverEditText;
    private Button doneButton;
    private OnRatesRecieved onRatesRecievedInterface;
    public RateFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public interface OnRatesRecieved
    {
        void onRateRecieved(String goldRate,String silverRate);
    }

    public static RateFragment newInstance(String title) {
        RateFragment frag = new RateFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        onRatesRecievedInterface=(GalanaActivity)getActivity();
        return inflater.inflate(R.layout.dialog_rates, container);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        goldEditText = (EditText) view.findViewById(R.id.dialog_rates_gold_rate_edittext);
        silverEditText=(EditText)view.findViewById(R.id.dialog_rates_silver_rate_edittext);
        doneButton=(Button)view.findViewById(R.id.dialo_rates_done_button);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Rates");
        // Show soft keyboard automatically and request focus to field
        goldEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validator.isParasableDouble(goldEditText.getText().toString().trim())&&
                   Validator.isParasableDouble(silverEditText.getText().toString().trim()))
                {
                    onRatesRecievedInterface.onRateRecieved(goldEditText.getText().toString().trim(),
                            silverEditText.getText().toString().trim());
                    dismiss();
                }
                else
                {
                    Toast.makeText(getActivity(),"Enter Rates Properly!",Toast.LENGTH_LONG).show();
                    dismiss();
                }
            }


        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        /*Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    */

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        return super.onCreateDialog(savedInstanceState);
    }


}
