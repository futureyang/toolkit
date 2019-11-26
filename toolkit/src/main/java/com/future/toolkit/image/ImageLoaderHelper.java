package com.future.toolkit.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.future.toolkit.R;
import com.future.toolkit.image.interfaces.IImageLoader;
import com.future.toolkit.image.interfaces.ImageLoaderCallback;
import com.future.toolkit.image.transformations.GrayscaleTransformation;
import com.future.toolkit.image.transformations.RSBlurTransformation;

import java.util.concurrent.ExecutionException;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by yangqc on 2019/10/28
 *
 * @Author yangqc
 */

public class ImageLoaderHelper implements IImageLoader {

    private final String TAG = this.getClass().getSimpleName();

    private static ImageLoaderHelper instance;

    private static int TIME_OUT = 4000;

    private RequestOptions mCommonOpts;
    private RequestOptions mNoPlaceholderOpts;
    private RequestOptions mNoCacheOpts;

    private ImageLoaderHelper() {
        mCommonOpts = new RequestOptions().timeout(TIME_OUT)
                .format(DecodeFormat.PREFER_RGB_565)
                //缩放，保持原始宽高比，使图像的尺寸之一*完全等于请求的尺寸，而另一个尺寸大于或等于*请求的尺寸。
                .centerCrop()
                .placeholder(R.drawable.blank_loading)
                .error(R.drawable.blank_nothing);

        mNoPlaceholderOpts = new RequestOptions().timeout(TIME_OUT)
                .format(DecodeFormat.PREFER_ARGB_8888)
                //缩放，保持原始宽高比，使图像的尺寸之一*完全等于请求的尺寸，而另一个尺寸大于或等于*请求的尺寸。
                .centerCrop()
                .error(R.drawable.blank_nothing);

        mNoCacheOpts = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
    }

    public static ImageLoaderHelper getInstance() {
        if (instance == null) {
            synchronized (ImageLoaderHelper.class) {
                if (instance == null) {
                    instance = new ImageLoaderHelper();
                }
            }
        }
        return instance;
    }

    @Override
    public void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).apply(mCommonOpts).into(imageView);
    }

    @Override
    public void loadNoCropImage(Context context, String url, ImageView imageView) {
        RequestOptions rops = new RequestOptions().timeout(TIME_OUT);
        Glide.with(context).load(url).apply(rops).into(imageView);
    }

    @Override
    public void loadNoPlaceholderImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).apply(mNoPlaceholderOpts)
                .transition(withCrossFade()).into(imageView);
    }


    @Override
    public void loadImage(Context context, String url, ImageView imageView, int width, int height) {
        RequestOptions rops = new RequestOptions().timeout(TIME_OUT)
                .format(DecodeFormat.PREFER_RGB_565)
                .centerCrop()
                .dontAnimate()
                .placeholder(R.drawable.blank_loading)
                .error(R.drawable.blank_nothing)
                .override(width, height);
        Glide.with(context).load(url).apply(rops).into(imageView);
    }

    @Override
    public void loadImage(Context context, String url, ImageView imageView, int width, int height, ImageLoaderCallback callback) {
        RequestOptions rops = new RequestOptions().timeout(TIME_OUT)
                .format(DecodeFormat.PREFER_RGB_565)
                .centerCrop()
                .dontAnimate()
                .error(R.drawable.blank_nothing)
                .override(width, height);
        Glide.with(context)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        callback.isSuccess(false);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        callback.isSuccess(true);
                        return false;
                    }
                })
                .apply(rops)
                .transition(withCrossFade())
                .into(imageView);
    }


    @Override
    public void loadImageWithoutPlaceholder(Context context, String url, ImageView imageView, int width, int height) {
        RequestOptions opts = new RequestOptions().timeout(TIME_OUT)
                .format(DecodeFormat.PREFER_RGB_565)
                .dontAnimate()
                .override(width, height);
        Glide.with(context).load(url).apply(opts).into(imageView);
    }

    @Override
    public void loadGrayImage(Context context, String url, ImageView imageView) {
        RequestOptions rops = new RequestOptions().timeout(TIME_OUT)
                .transforms(new GrayscaleTransformation());
        Glide.with(context).load(url).apply(rops).into(imageView);
    }

    @Override
    public void loadBlurImage(Context context, String url, ImageView imageView) {
        RequestOptions rops = new RequestOptions().timeout(TIME_OUT)
                .transforms(new RSBlurTransformation(context));
        Glide.with(context).load(url).apply(rops).into(imageView);
    }


    @Override
    public void loadRoundImage(Context context, String url, ImageView imageView, int radius) {
        RequestOptions opts = new RequestOptions().timeout(TIME_OUT)
                .format(DecodeFormat.PREFER_RGB_565)
                .centerCrop()
                .dontAnimate()
                .placeholder(R.drawable.blank_loading)
                .error(R.drawable.blank_nothing)
                .transform(new RoundedCorners(radius));
        Glide.with(context).load(url).apply(opts).into(imageView);
    }

    @Override
    public void loadCircleImage(Context context, String url, ImageView imageView, int placeHolder, int error) {
        RequestOptions opts = new RequestOptions().timeout(TIME_OUT)
                .format(DecodeFormat.PREFER_RGB_565)
                .dontAnimate()
                .circleCrop()
                .placeholder(placeHolder)
                .error(error);
        Glide.with(context).load(url).apply(opts).into(imageView);
    }

    @Override
    public Drawable getCircleDrawable(Context context, String url) {
        RequestOptions opts = new RequestOptions().timeout(TIME_OUT)
                .format(DecodeFormat.PREFER_RGB_565)
                .dontAnimate()
                .circleCrop();
        try {
            return Glide.with(context).asDrawable().load(url).apply(opts).apply(opts).submit().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void clearMemoryCache(Context context) {
        Glide.get(context).clearMemory();
    }

    @Override
    public void resume(Context context) {
        Glide.with(context).resumeRequests();
    }

    @Override
    public Bitmap getBitmap(Context context, String url) {
        Bitmap bitmap = null;
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        try {
            bitmap = Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .apply(mNoCacheOpts)
                    .submit()
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    @Override
    public void pause(Context context) {
        Glide.with(context).pauseRequests();
    }
}
