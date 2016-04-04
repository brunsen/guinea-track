package de.brunsen.guineatrack.services;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.lang.ref.WeakReference;

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

    private Bitmap getDefaultBitmap(Context context) {
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.unknown_guinea_pig);
    }

    public void setListImage(RoundedImageView imageView, File file, int width, int height) {
        if (cancelPotentialWork(file, imageView)) {
            final AsyncImageTask imageTask = new AsyncImageTask(imageView);
            final AsyncDrawable asyncDrawable =
                    new AsyncDrawable(imageView.getContext().getResources(), getDefaultBitmap(imageView.getContext()), imageTask);
            imageView.setImageDrawable(asyncDrawable);
            imageTask.execute(file, width, height);
        }
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

    private boolean cancelPotentialWork(File file, ImageView imageView) {
        final AsyncImageTask asyncImageTask = getBitmapWorkerTask(imageView);

        if (asyncImageTask != null) {
            final File fileData = asyncImageTask.file;
            // If bitmapData is not yet set or it differs from the new data
            if (fileData != null || fileData != file) {
                // Cancel previous task
                asyncImageTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    private AsyncImageTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getAsyncImageTask();
            }
        }
        return null;
    }

    private class AsyncImageTask extends AsyncTask<Object, Void, Bitmap> {

        private final WeakReference<RoundedImageView> imageViewReference;
        private File file;

        public AsyncImageTask(RoundedImageView imageView) {
            imageViewReference = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(Object[] params) {
            file = (File) params[0];
            Integer height = (Integer) params[1];
            Integer width = (Integer) params[2];

            return getPicture(file.getAbsolutePath(), width, height);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null && bitmap != null) {
                final RoundedImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    private class AsyncDrawable extends BitmapDrawable {
        WeakReference<AsyncImageTask> taskWeakReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, AsyncImageTask asyncImageTask) {
            super(res, bitmap);
            taskWeakReference = new WeakReference<>(asyncImageTask);
        }

        public AsyncImageTask getAsyncImageTask() {
            return taskWeakReference.get();
        }
    }
}
