package de.brunsen.guineatrack.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import de.brunsen.guineatrack.R;

public class PermissionDialog{
    private AlertDialog mDialog;

    public PermissionDialog(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
        builder.setMessage(message);
        builder.setTitle(context.getString(R.string.required_permission));
        String okButtonText = context.getString(android.R.string.ok);
        builder.setPositiveButton(okButtonText, onClickListener);
        builder.setCancelable(false);
        mDialog = builder.create();
    }

    public void show() {
        mDialog.show();
    }
}
