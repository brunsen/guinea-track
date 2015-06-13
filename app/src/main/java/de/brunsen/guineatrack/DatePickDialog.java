package de.brunsen.guineatrack;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

public class DatePickDialog extends DatePickerDialog{
    EditText editText;
    DatePicker datePicker;
    public DatePickDialog(Context context, final EditText editText, int year, int monthOfYear, final int dayOfMonth) {
        super(context, R.style.AlertDialogStyle, new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            }
        }, year, monthOfYear, dayOfMonth);
        this.editText = editText;
        setCancelable(false);
        setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.set), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editText.clearFocus();

                String date = datePicker.getDayOfMonth() + "." + datePicker.getMonth() + "." + datePicker.getYear();
                editText.setText(date);
                dismiss();
            }
        });
        setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(android.R.string.cancel), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editText.clearFocus();
                dismiss();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getButton(DialogInterface.BUTTON_NEUTRAL).setVisibility(View.INVISIBLE);
        datePicker = getDatePicker();
    }
}
