package com.example.varun.vesica;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by varun on 25/9/16.
 */
public class ClearHistoryDialogFragment extends DialogFragment {
    View view;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_clear_history, null);
        ButterKnife.bind(this,view);
        builder.setView(view);

        return builder.create();
    }

    public ClearHistoryDialogFragment() {
        super();
    }
}
