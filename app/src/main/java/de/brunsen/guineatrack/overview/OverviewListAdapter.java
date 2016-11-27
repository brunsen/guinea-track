package de.brunsen.guineatrack.overview;

import android.Manifest;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.util.ImageService;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class OverviewListAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private OverViewPresenter mPresenter;
    private LayoutInflater mInflater;
    private Context mContext;

    public OverviewListAdapter(Context context, OverViewPresenter presenter) {
        mInflater = LayoutInflater.from(context);
        mPresenter = presenter;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mPresenter.getItemCount();
    }

    @Override
    public Object getItem(int position) {
        return mPresenter.getGuineaPig(position);
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

        GuineaPig guineaPig = mPresenter.getGuineaPig(position);
        String limitationText = guineaPig.getOptionalData().getLimitations();
        if(limitationText.equals("")){
            limitationText = mContext.getString(R.string.no_limitations);
        }
        String subInfoText = guineaPig.getType().getText(mContext) + ", " + limitationText;

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
        String headerText = "" + mPresenter.getGuineaPig(position).getBreed();
        holder.text.setText(headerText);
        return convertView;
    }

    private void setListImage(RoundedImageView imageView, String filePath) {
        File file = new File(filePath);
        RxPermissions permissions = RxPermissions.getInstance(mContext);
        boolean permissionGranted = permissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (!filePath.equals("") && file.exists() && permissionGranted) {
            ImageService.getInstance().setListImage(imageView, file);
        } else {
            Drawable defaultImage = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.unknown_guinea_pig, null);
            imageView.setImageDrawable(defaultImage);
        }
    }

    @Override
    public long getHeaderId(int position) {
        long id = 0;
        char[] chars = mPresenter.getGuineaPig(position).getBreed().toUpperCase().toCharArray();
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