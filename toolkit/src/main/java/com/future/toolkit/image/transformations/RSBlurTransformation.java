package com.future.toolkit.image.transformations;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.future.toolkit.utils.blur.FastBlur;
import com.future.toolkit.utils.blur.RSBlur;

import java.security.MessageDigest;

/**
 * https://github.com/wasabeef/glide-transformations
 */
public class RSBlurTransformation extends BitmapTransformation {

    private static final int VERSION = 1;
    private static final String ID =
            "jp.wasabeef.glide.transformations.RSBlurTransformation." + VERSION;

    private static int MAX_RADIUS = 20;
    private static int DEFAULT_DOWN_SAMPLING = 4;

    private Context context;
    private int radius;
    private int sampling;

    public RSBlurTransformation(Context context) {
        this(context, MAX_RADIUS, DEFAULT_DOWN_SAMPLING);
    }

    public RSBlurTransformation(Context context, int radius) {
        this(context, radius, DEFAULT_DOWN_SAMPLING);
    }

    public RSBlurTransformation(Context context, int radius, int sampling) {
        this.context = context.getApplicationContext();
        this.radius = radius;
        this.sampling = sampling;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool,
                               @NonNull Bitmap toTransform, int outWidth, int outHeight) {

        int width = toTransform.getWidth();
        int height = toTransform.getHeight();
        int scaledWidth = width / sampling;
        int scaledHeight = height / sampling;

        Bitmap bitmap = pool.get(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.scale(1 / (float) sampling, 1 / (float) sampling);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(toTransform, 0, 0, paint);

        try {
            bitmap = RSBlur.blur(context, bitmap, radius);
        } catch (RuntimeException e) {
            bitmap = FastBlur.blur(bitmap, radius, true);
        }

        return bitmap;
    }

    @Override
    public String toString() {
        return "SupportRSBlurTransformation(radius=" + radius + ", sampling=" + sampling + ")";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RSBlurTransformation &&
                ((RSBlurTransformation) o).radius == radius &&
                ((RSBlurTransformation) o).sampling == sampling;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + radius * 1000 + sampling * 10;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + radius + sampling).getBytes(CHARSET));
    }
}