package com.future.toolkit.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Preconditions;

/**
 * Created by yangqc on 2019/9/23
 *
 * @Author yangqc
 */
public class BitmapUtils {


    /**
     * 是否为合法的bitmap
     *
     * @param bitmap
     * @return
     */
    public static boolean isAvailable(@Nullable Bitmap bitmap) {
        return bitmap != null && !bitmap.isRecycled();
    }

    /**
     * recycler bitmap
     *
     * @param bitmap
     */
    public static void safelyRecycle(@Nullable Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    /**
     * 居中缩放并裁剪图片
     *
     * @param src
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    @Nullable
    public static Bitmap scaleCenterCrop(@Nullable Bitmap src, int targetWidth, int targetHeight, boolean recycle) {
        if (!isAvailable(src)) {
            return null;
        }

        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        if (srcWidth == targetWidth && srcHeight == targetHeight) {
            return src;
        }

        //取小的缩放比例，这样画布缩放后才能有一边比原图小
        float scaleW = srcWidth * 1.0f / targetWidth;
        float scaleH = srcHeight * 1.0f / targetHeight;
        float scale = Math.min(scaleW, scaleH);
        float dx = (srcWidth - targetWidth * scale) / 2;
        float dy = (srcHeight - targetHeight * scale) / 2;
        Bitmap bitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        Canvas canvas = new Canvas(bitmap);
        canvas.scale(1 / scale, 1 / scale);//这里应用是1/scale才是缩放图片以适应原图大小
        //canvas.drawBitmap(src, -dx, -dy, paint); //这是指从坐标轴的哪里开始画
        canvas.translate(-dx, -dy);//此处是移动原点坐标为负值, 绘图的位置不变，只移动的画布
        canvas.drawBitmap(src, 0, 0, paint);
        canvas.setBitmap(null);
        if (recycle) {
            safelyRecycle(src);
        }
        return bitmap;
    }

    @Nullable
    public static Bitmap scaleCenterCrop(@Nullable Bitmap src, int targetWidth,
                                         int targetHeight) {
        return scaleCenterCrop(src, targetWidth, targetHeight, false);
    }

    /**
     * @param drawable
     * @return
     */
    @NonNull
    public static Bitmap toBitmap(@NonNull Drawable drawable) {
        int width = drawable.getIntrinsicWidth();   // 取 drawable 的长宽
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ?
                Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;// 取 drawable 的颜色格式
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);// 建立对应 bitmap
        Canvas canvas = new Canvas(bitmap); // 建立对应 bitmap 的画布
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);      // 把 drawable 内容画到画布中
        return bitmap;
    }


    /**
     * 居中缩放并裁剪图片
     * 图片只缩小不放大
     *
     * @param src
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    @Nullable
    public static Bitmap scaleCenterCrop2(@Nullable Bitmap src, int targetWidth, int targetHeight, boolean recycle) {
        if (!isAvailable(src)) {
            return null;
        }

        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        if (srcWidth * 1.0f / srcHeight == targetWidth * 1.0f / targetHeight) {
            return src;
        }

        //取小的缩放比例，这样画布缩放后才能有一边比原图小
        float scaleW = srcWidth * 1.0f / targetWidth;
        float scaleH = srcHeight * 1.0f / targetHeight;
        float scale = Math.min(scaleW, scaleH);
        float targetScale = Math.min(scale, 1f);
        return scaleCenterCrop(src, (int) (targetWidth * targetScale),
                (int) (targetHeight * targetScale));
    }

    /**
     * 居中缩放并裁剪图片
     * 图片只缩小不放大
     *
     * @param src
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap scaleCenterCrop2(@Nullable Bitmap src, int targetWidth, int targetHeight) {
        return scaleCenterCrop2(src, targetWidth, targetHeight, false);
    }

    /**
     * @param src        源图片
     * @param targetRect 需要裁切的大小，left和top表示偏移量
     * @param scale      缩放大小，原图与目标的缩放比，如果为0，则自动计算
     * @return
     */
    public static Bitmap cropToBitmap(@NonNull Drawable src, @NonNull Rect targetRect, float scale) {
        Preconditions.checkNotNull(src, "BitmapUtils crop src should not null");
        Preconditions.checkNotNull(targetRect, "BitmapUtils targetRect should not null");
        int srcWidth = src.getIntrinsicWidth();
        int srcHeight = src.getIntrinsicHeight();

        final int targetWidth = targetRect.width();
        final int targetHeight = targetRect.height();
        if (srcWidth == targetWidth && srcHeight == targetHeight
                && targetRect.left == 0 && targetRect.top == 0) {
            return toBitmap(src);
        }
        if (srcWidth <= 0 || srcHeight <= 0) {
            srcWidth = targetWidth;
            srcHeight = targetHeight;
        }
        src.setBounds(0, 0, srcWidth, srcHeight);

        if (scale <= 0) {//自动铺满
            if (targetRect.left == 0) {
                scale = srcWidth * 1f / targetWidth;
            } else {
                scale = srcHeight * 1f / targetHeight;
            }
        }

        final Bitmap result = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.translate(-targetRect.left, -targetRect.top);
        canvas.scale(1 / scale, 1 / scale);
        src.draw(canvas);
        canvas.setBitmap(null);

        return result;
    }

    /**
     * 居中裁切
     * @param src
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    @Nullable
    public static Bitmap scaleCenterCrop(@Nullable Drawable src, int targetWidth,
                                         int targetHeight) {
        if (src == null){
            return null;
        }

        int srcWidth = src.getIntrinsicWidth();
        int srcHeight = src.getIntrinsicHeight();
        if (srcWidth <= 0 && srcHeight <= 0){
            return cropToBitmap(src, new Rect(0, 0, targetWidth, targetHeight), 0);
        }

        float scaleW = srcWidth * 1.0f / targetWidth;
        float scaleH = srcHeight * 1.0f / targetHeight;
        float scale = Math.min(scaleW, scaleH);

        int dx = (int) ((srcWidth / scale - targetWidth) / 2);
        int dy = (int) ((srcHeight / scale - targetHeight) / 2);
        return cropToBitmap(src, new Rect(dx, dy,dx + targetWidth,dy + targetHeight), scale);
    }

    /**
     * 裁切，只缩小不放大
     * @param src
     * @param targetRect
     * @param scale
     * @return
     */
    public static Bitmap cropToBitmap2(@Nullable Drawable src, @NonNull Rect targetRect, float scale){
        if (src == null){
            return null;
        }
        int srcWidth = src.getIntrinsicWidth();
        int srcHeight = src.getIntrinsicHeight();
        if (srcWidth <= 0 && srcHeight <= 0){
            return cropToBitmap(src, targetRect, scale);
        }

        if (scale <= 0){
            if (targetRect.left == 0){
                scale = srcWidth * 1f / targetRect.width();
            } else {
                scale = srcHeight * 1f / targetRect.height();
            }
        }
        Rect rect = new Rect();
        rect.left = (int) (targetRect.left * scale);
        rect.top = (int) (targetRect.top * scale);
        rect.right = (int) (rect.left + targetRect.width() * scale);
        rect.bottom = (int) (rect.top + targetRect.height() * scale);
        return cropToBitmap(src, rect, 1f);
    }
}
