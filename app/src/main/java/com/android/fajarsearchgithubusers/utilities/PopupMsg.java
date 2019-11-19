package com.android.fajarsearchgithubusers.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.android.fajarsearchgithubusers.R;

public class PopupMsg {

    public static void msgOk(final Context ctx, String msgTitle, String content) {
        try {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setTitle(msgTitle)
                    .setMessage(content)
                    .setPositiveButton(ctx.getString(R.string.ok), dialogClickListener)
                    .show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void msgCanReturn(final Context ctx, DialogInterface.OnClickListener dialogClickListener, String msgTitle, String content) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setTitle(msgTitle)
                    .setMessage(content)
                    .setPositiveButton(ctx.getString(R.string.ok), dialogClickListener)
                    .show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
