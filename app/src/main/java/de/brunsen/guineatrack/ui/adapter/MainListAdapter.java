package de.brunsen.guineatrack.ui.adapter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.util.List;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.services.ImageService;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class MainListAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private List<GuineaPig> guineaPigs;
    private LayoutInflater mInflater;
    private Context mContext;

    public MainListAdapter(Context context, List<GuineaPig> list) {
        mInflater = LayoutInflater.from(context);
        setGuineaPigs(list);
        mContext = context;
    }

    public void setGuineaPigs(List<GuineaPig> list) {
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
        ItemViewHolder holder;
        if (convertView == null) {
            holder = new ItemViewHolder();
            convertView = mInflater.inflate(R.layout.main_list_item, parent, false);
            holder.imageView = (RoundedImageView) convertView.findViewById(R.id.main_list_item_image);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.main_list_item_name);
            holder.subInfoTextView = (TextView) convertView.findViewById(R.id.main_list_item_sub_info);
            convertView.setTag(holder);
        } else {
            holder = (ItemViewHolder) convertView.getTag();
        }

        GuineaPig guineaPig = guineaPigs.get(position);
        String limitationText = guineaPig.getOptionalData().getLimitations();
        if(limitationText.equals("")){
            limitationText = mContext.getString(R.string.no_limitations);
        }
        String subInfoText = guineaPig.getType().getText() + ", " + limitationText;

        holder.nameTextView.setText(guineaPig.getName());
        holder.subInfoTextView.setText(subInfoText);
        setListImage(holder.imageView, guineaPig.getOptionalData().getPicturePath());

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
        //set header text as the race of a guinea pig
        String headerText = "" + guineaPigs.get(position).getBreed();
        holder.text.setText(headerText);
        return convertView;
    }

    private void setListImage(RoundedImageView imageView, String filePath) {
        File file = new File(filePath);
        boolean permissionGranted = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionGranted = ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        }
        if (!filePath.equals("") && file.exists() && permissionGranted) {
            ImageService.getInstance().setListImage(imageView, file);
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                Drawable defaultImage = imageView.getResources().getDrawable(R.drawable.unknown_guinea_pig);
                imageView.setImageDrawable(defaultImage);
            } else {
                imageView.setImageResource(R.drawable.unknown_guinea_pig);
            }
        }
    }

    @Override
    public long getHeaderId(int position) {
        long id = 0;
        char[] chars = guineaPigs.get(position).getBreed().toUpperCase().toCharArray();
        for (char character : chars) {
            id += character;
        }
        return id;
    }

    private class HeaderViewHolder {
        TextView text;
    }

    private class ItemViewHolder {
        RoundedImageView imageView;
        TextView nameTextView;
        TextView subInfoTextView;
    }

}