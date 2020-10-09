package com.example.testwork;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class CustomDialogFragment extends DialogFragment implements View.OnClickListener {
    MainActivity activity;
    String bs;
    AlertDialog dialogs;
    int pose;




    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final View v1 = View.inflate(getActivity(), R.layout.dialog, null);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                .setView(v1);
        dialogs = dialog.create();
        dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btnReopenId = v1.findViewById(R.id.button2);
        Button btnCancelId = v1.findViewById(R.id.button4);
        TextView attem = v1.findViewById(R.id.textView7);
        String f = getString(R.string.attempts);
        attem.setText(f+" "+MainActivity.loses);
        TextView tex = v1.findViewById(R.id.record);
        tex.setVisibility(View.GONE);
        if(MainActivity.los >= MainActivity.loses){
            tex.setVisibility(View.VISIBLE);
            MainActivity.los = MainActivity.loses;

        }

        btnReopenId.setOnClickListener(this);
        btnCancelId.setOnClickListener(this);
        activity = ((MainActivity) getActivity());
        return dialogs;
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button2:
                dialogs.dismiss();
                break;
            case  R.id.button4:
                Game.adapter.update();
                dialogs.dismiss();
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dialogs = null;
        activity = null;
    }
}
