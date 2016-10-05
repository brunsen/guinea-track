package de.brunsen.guineatrack.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

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

    public Bitmap getPicture(String filePath, int width, int height) {
        File file = new File(filePath);
        Bitmap picture = null;
        if (file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, width, height);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            picture = BitmapFactory.decodeFile(filePath,
                    options);
        }
        return picture;
    }

    private int calculateInSampleSize(BitmapFactory.Options options,
                                      int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public void setListImage(RoundedImageView imageView, File file) {
        Context context = imageView.getContext();
        Glide.with(context)
                .load(file)
                .asBitmap()
                .centerCrop()
                .placeholder(R.drawable.unknown_guinea_pig)
                .error(R.drawable.unknown_guinea_pig)
                .into(imageView);
    }

    public void setDefaultImage(ImageView iv) {
        Context context = iv.getContext();
        Drawable defaultImage = ContextCompat.getDrawable(context, R.drawable.unknown_guinea_pig);
        iv.setImageDrawable(defaultImage);
    }
}
