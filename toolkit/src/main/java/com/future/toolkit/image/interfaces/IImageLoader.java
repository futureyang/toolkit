package com.future.toolkit.image.interfaces;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

/**
 * Created by yangqc on 2019/10/28
 * 图片loader
 * @Author yangqc
 */

public interface IImageLoader {

    void loadImage(Context context, String url, ImageView imageView);

    void loadNoCropImage(Context context, String url, ImageView imageView);

    void loadNoPlaceholderImage(Context context, String url, ImageView imageView);

    void loadImage(Context context, String url, ImageView imageView, int width, int height);

    void loadImage(Context context, String url, ImageView imageView, int width, int height, ImageLoaderCallback callback);

    void loadImageWithoutPlaceholder(Context context, String url, ImageView imageView, int width, int height);

    void loadGrayImage(Context context, String url, ImageView imageView);

    void loadBlurImage(Context context, String url, ImageView imageView);

    void loadRoundImage(Context context, String url, ImageView imageView, int radius);

    void loadCircleImage(Context context, String url, ImageView imageView, @DrawableRes int placeHolder, @DrawableRes int error);

    Drawable getCircleDrawable(Context context, String url);

    Bitmap getBitmap(Context context, String url);

    void clearMemoryCache(Context context);

    void resume(Context context);

    void pause(Context context);
}
