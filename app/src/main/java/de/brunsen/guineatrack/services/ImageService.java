package de.brunsen.guineatrack.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
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
        CircleImageViewTarget viewTarget = new CircleImageViewTarget(imageView);
        Glide.with(context)
                .load(file)
                .asBitmap()
                .centerCrop()
                .placeholder(R.drawable.unknown_guinea_pig)
                .into(viewTarget);
    }

    public void setDefaultImage(ImageView iv) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable defaultImage = iv.getResources().getDrawable(R.drawable.unknown_guinea_pig);
            iv.setImageDrawable(defaultImage);
        } else {
            iv.setImageResource(R.drawable.unknown_guinea_pig);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            iv.setBackgroundColor(iv.getResources().getColor(R.color.primary_color));
        } else {
            iv.setBackgroundColor(iv.getResources().getColor(R.color.primary_color, null));
        }
    }

    private class CircleImageViewTarget extends BitmapImageViewTarget {
        private Context context;
        private RoundedImageView imageView;

        public CircleImageViewTarget(RoundedImageView imageView) {
            super(imageView);
            this.imageView = imageView;
            context = imageView.getContext();
        }

        @Override
        protected void setResource(Bitmap resource) {
            RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
            circularBitmapDrawable.setCircular(true);
            imageView.setImageDrawable(circularBitmapDrawable);
        }
    }
}
