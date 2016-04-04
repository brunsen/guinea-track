package de.brunsen.guineatrack.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.services.ImageService;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class MainListAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private List<GuineaPig> guineaPigs;
    private List<Bitmap> pigImages;
    private LayoutInflater mInflater;
    private Context mContext;

    public MainListAdapter(Context context, List<GuineaPig> list) {
        mInflater = LayoutInflater.from(context);
        setGuineaPigs(list);
        pigImages = new ArrayList<>();
        mContext = context;
    }

    public void setGuineaPigs(List<GuineaPig> list) {
        guineaPigs = list;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        populatePigImageList();
    }

    private void populatePigImageList() {
        pigImages.clear();
        for (int i = 0; i < guineaPigs.size(); i++) {
            pigImages.add(getImage(guineaPigs.get(i).getOptionalData().getPicturePath()));
        }
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

        GuineaPig pig = guineaPigs.get(position);
        String limitationText = pig.getOptionalData().getLimitations();
        if(limitationText.equals("")){
            limitationText = mContext.getString(R.string.no_limitations);
        }
        String subInfoText = pig.getType().getText() + ", " + limitationText;

        holder.nameTextView.setText(pig.getName());
        holder.subInfoTextView.setText(subInfoText);
        holder.imageView.setImageBitmap(pigImages.get(position));

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

    protected Bitmap getImage(String filePath) {
        Bitmap pigImage;
        if (!filePath.equals("")) {
            int requiredWidth = (int) mContext.getResources().getDimension(R.dimen.list_item_image_width);
            int requiredHeight = (int) mContext.getResources().getDimension(R.dimen.list_item_image_height);
            Bitmap picture = ImageService.getInstance().getPicture(filePath, requiredWidth, requiredHeight);
            if (picture != null) {
                pigImage = picture;
            } else {
                pigImage = ((BitmapDrawable) ImageService.getInstance().getDefaultListImage(mContext)).getBitmap();
            }
        } else {
            pigImage = ((BitmapDrawable) ImageService.getInstance().getDefaultListImage(mContext)).getBitmap();
        }
        return pigImage;
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