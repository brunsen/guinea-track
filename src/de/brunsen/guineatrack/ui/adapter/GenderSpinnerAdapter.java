package de.brunsen.guineatrack.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import de.brunsen.guineatrack.model.Gender;

public class GenderSpinnerAdapter extends ArrayAdapter<Gender> {
    private Context context;
    private List<Gender> items;
    private int resource;

    public GenderSpinnerAdapter(Context context, int resource,
                                List<Gender> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        items = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(resource, parent, false);
        }
        TextView textView = (TextView) row.findViewById(android.R.id.text1);
        textView.setText(items.get(position).toString());
        return row;
    }
}
