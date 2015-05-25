package de.brunsen.guineatrack.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.services.ImageService;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class MainListAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private List<GuineaPig> guineaPigs;
    private LayoutInflater mInflater;

    public MainListAdapter(Context context, List<GuineaPig> list) {
        mInflater = LayoutInflater.from(context);
        guineaPigs = list;
    }

    @Override
    public int getCount() {
        return guineaPigs.size();
    }

    @Override
    public Object getItem(int position) {
        return guineaPigs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.main_list_item, parent, false);
        }

        TextView nameText = (TextView) convertView.findViewById(R.id.main_list_item_name);
        TextView subInfoTextView = (TextView) convertView.findViewById(R.id.main_list_item_sub_info);
        RoundedImageView pigImageView = (RoundedImageView) convertView.findViewById(R.id.main_list_item_image);

        GuineaPig pig = guineaPigs.get(position);

        String subInfoText = pig.getType().toString() + ", " + pig.getRace();

        nameText.setText(pig.getName());
        subInfoTextView.setText(subInfoText);
        setImage(pig.getPicturePath(), pigImageView);


        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.main_list_header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.textSeparator);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set header text as first char in name
        String headerText = "" + guineaPigs.get(position).getName().subSequence(0, 1).charAt(0);
        holder.text.setText(headerText);
        return convertView;
    }

    protected void setImage(String filePath, RoundedImageView imageView) {
        if (!filePath.equals("")) {
            int requiredWidth = (int) imageView.getResources().getDimension(R.dimen.list_item_image_width);
            int requiredHeight = (int) imageView.getResources().getDimension(R.dimen.list_item_image_height);
            Bitmap picture = ImageService.getInstance().getPicture(filePath, requiredWidth, requiredHeight);
            if (picture != null) {
                imageView.setImageBitmap(picture);
            } else {
                ImageService.getInstance().setDefaultRoundImage(imageView);
            }
        } else {
            ImageService.getInstance().setDefaultRoundImage(imageView);
        }
    }

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        return guineaPigs.get(position).getName().subSequence(0, 1).charAt(0);
    }

    private class HeaderViewHolder {
        TextView text;
    }

}