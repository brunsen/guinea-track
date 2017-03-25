package de.brunsen.guineatrack.edit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import de.brunsen.guineatrack.edit.BaseEditPresenter;
import de.brunsen.guineatrack.model.Gender;

public class GenderSpinnerAdapter extends ArrayAdapter<Gender> {
    private Context context;
    private BaseEditPresenter presenter;
    private int resource;

    public GenderSpinnerAdapter(Context context, int resource,
                                BaseEditPresenter presenter) {
        super(context, resource, presenter.getGenderList());
        this.presenter = presenter;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        TextView textView = (TextView) row.findViewById(android.R.id.text1);
        Gender gender = presenter.getGenderList().get(position);
        textView.setText(gender.getText(context));
        return row;
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(resource, parent, false);
        }
        TextView textView = (TextView) row.findViewById(android.R.id.text1);
        Gender gender = presenter.getGenderList().get(position);
        textView.setText(gender.getText(context));
        return row;
    }
}
