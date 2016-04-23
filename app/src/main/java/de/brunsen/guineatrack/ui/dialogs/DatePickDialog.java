package de.brunsen.guineatrack.ui.dialogs;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;

import de.brunsen.guineatrack.R;

public class DatePickDialog extends DatePickerDialog {
    private String title;
    private DatePicker datePicker;

    public DatePickDialog(Context context, final EditText editText, int year, int monthOfYear, final int dayOfMonth) {
        super(context, R.style.AlertDialogStyle, null, year, monthOfYear, dayOfMonth);
        setCancelable(false);
        setPermanentTitle(context.getString(R.string.date_picker_title));
        setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.set), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editText.clearFocus();
                DateFormat dateFormat = DateFormat.getDateInstance();
                String date = dateFormat.format(datePicker.getCalendarView().getDate());
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
        setButton(DialogInterface.BUTTON_NEUTRAL, context.getString(R.string.reset), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editText.getText().clear();
                editText.clearFocus();
                dismiss();
            }
        });
    }

    public void setPermanentTitle(String title) {
        this.title = title;
        setTitle(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datePicker = getDatePicker();
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        super.onDateChanged(view, year, month, day);
        setTitle(title);
    }
}
