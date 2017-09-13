package com.badjatya.ledger.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.badjatya.ledger.Rakam.ViewRakam.ViewRakamTransaction;
import com.badjatya.ledger.activities.ViewGirviTransactionActivity;


/**
 * Created by Tanmay on 10/3/2016.
 */
//This type of fragment is for simple yes no questions

public class AlertFragment extends DialogFragment
{
    OnOkayPressedGirviInterface onOkayPressedGirviInterface;
    OkayPressedRakamInterface okayPressedRakamInterface;
    private static final int BUTTON_CODE_DELETE_BUTTON=001;
    private static final int BUTTON_CODE_MELTED_OKAY_BUTTON=002;
    private static final int BUTTON_CODE_SETTLE_OKAY_BUTTON=003;

    public AlertFragment() {
        // Empty constructor required for DialogFragment
    }

    public interface OnOkayPressedGirviInterface
    {
        public void deleteOkayPressed();
        public void meltOkayPressed();
        public void settleOkayPressed();
    }

    public interface OkayPressedRakamInterface
    {
        public void deleteOkayPressed();
        public void settleOkayPressed();
    }

    public static AlertFragment newInstance(String message,int buttoncode) {
        AlertFragment frag = new AlertFragment();
        //This is done so that message stays in fragment arguments in case of a config change
        Bundle args = new Bundle();
        args.putString("message", message);
        args.putInt("buttoncode",buttoncode);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        if(getActivity() instanceof ViewGirviTransactionActivity)
        {
         onOkayPressedGirviInterface =(ViewGirviTransactionActivity)getActivity();
        }
        if(getActivity() instanceof ViewRakamTransaction)
        {
            okayPressedRakamInterface=(ViewRakamTransaction)getActivity();
        }
        String message = getArguments().getString("message", "Are you sure?");
        final int buttonCode=getArguments().getInt("buttoncode");
        AlertDialog alertDialog=new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(getActivity() instanceof ViewGirviTransactionActivity&&buttonCode==BUTTON_CODE_DELETE_BUTTON)
                        {
                            onOkayPressedGirviInterface.deleteOkayPressed();
                        }
                        if(getActivity() instanceof ViewGirviTransactionActivity&&buttonCode==BUTTON_CODE_MELTED_OKAY_BUTTON)
                        {
                            onOkayPressedGirviInterface.meltOkayPressed();
                        }
                        if(getActivity() instanceof ViewGirviTransactionActivity&&buttonCode==BUTTON_CODE_SETTLE_OKAY_BUTTON)
                        {
                            onOkayPressedGirviInterface.settleOkayPressed();
                        }
                        if(getActivity() instanceof ViewRakamTransaction && buttonCode==BUTTON_CODE_DELETE_BUTTON)
                        {
                            okayPressedRakamInterface.deleteOkayPressed();
                        }
                        if (getActivity() instanceof ViewRakamTransaction && buttonCode==BUTTON_CODE_SETTLE_OKAY_BUTTON)
                        {
                            okayPressedRakamInterface.settleOkayPressed();
                        }

                    }
                })
                .setNegativeButton("No", null)
                .create();
        return alertDialog;
    }
}
