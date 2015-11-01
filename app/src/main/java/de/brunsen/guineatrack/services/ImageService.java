package de.brunsen.guineatrack.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;

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

    public Drawable getDefaultListImage(Context context) {
        Drawable drawable;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.getResources().getDrawable(R.drawable.unknown_guineapig);
        } else {
            drawable = context.getDrawable(R.drawable.unknown_guineapig);
        }
        
        return drawable;
    }

    public void setDefaultImage(ImageView iv) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable defaultImage = iv.getResources().getDrawable(R.drawable.unknown_guineapig);
            iv.setImageDrawable(defaultImage);
        } else {
            iv.setImageResource(R.drawable.unknown_guineapig);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            iv.setBackgroundColor(iv.getResources().getColor(R.color.primary_color));
        } else {
            iv.setBackgroundColor(iv.getResources().getColor(R.color.primary_color, null));
        }
    }
}
