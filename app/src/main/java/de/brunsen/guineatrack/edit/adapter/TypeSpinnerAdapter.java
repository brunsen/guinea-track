package de.brunsen.guineatrack.edit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import de.brunsen.guineatrack.edit.BaseEditPresenter;
import de.brunsen.guineatrack.model.Type;

public class TypeSpinnerAdapter extends ArrayAdapter<Type> {

    private Context context;
    private BaseEditPresenter presenter;
    private int resource;

    public TypeSpinnerAdapter(Context context, int resource,
                              BaseEditPresenter presenter) {
        super(context, resource, presenter.getTypeList());
        this.context = context;
        this.resource = resource;
        this.presenter = presenter;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        TextView textView = (TextView) row.findViewById(android.R.id.text1);
        Type type = presenter.getTypeList().get(position);
        textView.setText(type.getText(context));
        return row;
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(resource, parent, false);
        }
        TextView textView = (TextView) row.findViewById(android.R.id.text1);
        Type type = presenter.getTypeList().get(position);
        textView.setText(type.getText(context));
        return row;
    }
}
