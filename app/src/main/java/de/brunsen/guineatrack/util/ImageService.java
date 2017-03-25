package de.brunsen.guineatrack.util;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import de.brunsen.guineatrack.R;

public class ImageService {

    private static ImageService instance;

    public static ImageService getInstance() {
        if (instance == null) {
            instance = new ImageService();
        }
        return instance;
    }

    public void loadImageIntoView(ImageView imageView, File file) {
        Context context = imageView.getContext();
        Glide.with(context)
                .load(file)
                .asBitmap()
                .centerCrop()
                .placeholder(R.drawable.unknown_guinea_pig)
                .error(R.drawable.unknown_guinea_pig)
                .into(imageView);
    }

    public void loadImageIntoView(ImageView imageView, @DrawableRes int resourceId) {
        Context context = imageView.getContext();
        Glide.with(context)
                .load(resourceId)
                .centerCrop()
                .into(imageView);
    }
}
