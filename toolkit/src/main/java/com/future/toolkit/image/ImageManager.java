package com.future.toolkit.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.future.toolkit.image.interfaces.IImageLoader;
import com.future.toolkit.image.interfaces.ImageLoaderCallback;

/**
 * Created by yangqc on 2019/10/28
 *
 * @Author yangqc
 */

public class ImageManager implements IImageLoader {
    private final String TAG = this.getClass().getSimpleName();

    private static ImageManager instance;

    private ImageLoaderHelper mImageLoaderHelper;

    private ImageManager() {
        mImageLoaderHelper = ImageLoaderHelper.getInstance();
    }

    public static ImageManager getInstance() {
        if (instance == null) {
            synchronized (ImageManager.class) {
                if (instance == null) {
                    instance = new ImageManager();
                }
            }
        }
        return instance;
    }

    @Override
    public void loadImage(Context context, String url, ImageView imageView) {
        mImageLoaderHelper.loadImage(context, url, imageView);
    }

    @Override
    public void loadNoCropImage(Context context, String url, ImageView imageView) {
        mImageLoaderHelper.loadNoCropImage(context, url, imageView);
    }

    @Override
    public void loadNoPlaceholderImage(Context context, String url, ImageView imageView) {
        mImageLoaderHelper.loadNoPlaceholderImage(context, url, imageView);
    }

    @Override
    public void loadImage(Context context, String url, ImageView imageView, int width, int height) {
        mImageLoaderHelper.loadImage(context, url, imageView, width, height);
    }

    @Override
    public void loadImage(Context context, String url, ImageView imageView, int width, int height, ImageLoaderCallback callback) {
        mImageLoaderHelper.loadImage(context, url, imageView, width, height,callback);
    }

    @Override
    public void loadImageWithoutPlaceholder(Context context, String url, ImageView imageView, int width, int height) {
        mImageLoaderHelper.loadImageWithoutPlaceholder(context, url, imageView, width, height);
    }

    @Override
    public void loadGrayImage(Context context, String url, ImageView imageView) {
        mImageLoaderHelper.loadGrayImage(context, url, imageView);
    }

    @Override
    public void loadBlurImage(Context context, String url, ImageView imageView) {
        mImageLoaderHelper.loadBlurImage(context, url, imageView);
    }

    @Override
    public void loadRoundImage(Context context, String url, ImageView imageView, int radius) {
        mImageLoaderHelper.loadRoundImage(context, url, imageView, radius);
    }

    @Override
    public void loadCircleImage(Context context, String url, ImageView imageView, int placeHolder, int error) {
        mImageLoaderHelper.loadCircleImage(context,url,imageView,placeHolder,error);
    }

    @Override
    public Drawable getCircleDrawable(Context context, String url) {
        return mImageLoaderHelper.getCircleDrawable(context,url);
    }

    @Override
    public Bitmap getBitmap(Context context, String url) {
        return mImageLoaderHelper.getBitmap(context, url);
    }

    @Override
    public void clearMemoryCache(Context context) {
        mImageLoaderHelper.clearMemoryCache(context);
    }

    @Override
    public void resume(Context context) {
        mImageLoaderHelper.resume(context);
    }

    @Override
    public void pause(Context context) {
        mImageLoaderHelper.pause(context);
    }
}
