package com.darwin.aigus.darwin.AndroidUltils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.darwin.aigus.darwin.R;

public class DarwinLisenca{
    private Context context;
    private DialogInterface.OnClickListener dialogInterface;

    public DarwinLisenca(Context context, DialogInterface.OnClickListener dialogInterface) {
        this.context = context;
        this.dialogInterface = dialogInterface;
    }

    public void onCreateDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(R.string.noTools);
        alertDialog.setMessage(R.string.noMsnTools);
        alertDialog.setPositiveButton(R.string.licenciar, dialogInterface);
        alertDialog.setNegativeButton(R.string.noThanks, dialogInterface);
        alertDialog.show();
    }
}
