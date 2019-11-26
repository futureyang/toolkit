package com.future.toolkit.utils.blur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.renderscript.RSRuntimeException;

import androidx.annotation.NonNull;

import com.future.toolkit.utils.BitmapUtils;


/**
 * Created by yangqc on 2019/9/23
 *
 * @Author yangqc
 */
public class BlurUtil {
    private static final String TAG = "BlurUtil";

    public static final int DEFAULT_BLUR_RADIUS = 25;
    public static final float DEFAULT_BLUR_SCALE = 1 / 8f;

    public static Bitmap blur(@NonNull Context context, Bitmap bitmap) {
        return blur(context, bitmap, bitmap.getWidth(), bitmap.getHeight(), DEFAULT_BLUR_RADIUS, DEFAULT_BLUR_SCALE);
    }

    public static Bitmap blur(@NonNull Context context, Drawable drawable) {
        return blur(context, drawable, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                DEFAULT_BLUR_RADIUS, DEFAULT_BLUR_SCALE);
    }

    public static Bitmap blur(@NonNull Context context, Bitmap bitmap, int radius, float scaleFactor) {
        return blur(context, bitmap, bitmap.getWidth(), bitmap.getHeight(), radius, scaleFactor);
    }

    /**
     * 高斯模糊
     *
     * @param context
     * @param bitmap
     * @param width
     * @param height
     * @param radius      虚化半径
     * @param scaleFactor 缩放比例
     * @return
     */
    public static Bitmap blur(Context context, Bitmap bitmap, int width, int height, int radius,
                              float scaleFactor) {
        if (bitmap == null) return null;
        if (radius <= 0) {
            return bitmap;
        }
        Bitmap overlay = Bitmap.createBitmap((int) (width * scaleFactor),
                (int) (height * scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        cropCanvas(canvas, bitmap, overlay);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        //overlay = doBlur(overlay,radius);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            try {
                overlay = RSBlur.blur(context, overlay, radius);
            } catch (RSRuntimeException e) {
                overlay = FastBlur.blur(overlay, radius, true);
            }
        } else {
            overlay = FastBlur.blur(overlay, radius, true);
        }

        return overlay;
    }

    private static void cropCanvas(Canvas canvas, Bitmap bitmap, Bitmap overlay) {
        int srcWidth = bitmap.getWidth();
        int srcHeight = bitmap.getHeight();

        //目标长宽
        int targetWidth = overlay.getWidth();
        int targetHeight = overlay.getHeight();
        cropCanvas(canvas, srcWidth, srcHeight, targetWidth, targetHeight);
    }

    private static void cropCanvas(Canvas canvas, int srcWidth, int srcHeight, int targetWidth, int targetHeight){
        int scaledWidth = srcWidth * targetHeight / srcHeight;//按高度缩放后的宽度值
        int scaledHeight = srcHeight * targetWidth / srcWidth;//按宽度缩放后的高度值

        if (scaledWidth <= targetWidth) {//按宽度进行缩放
            scaledWidth = targetWidth;
        } else {
            scaledHeight = targetHeight;
        }

        float scale = (float) scaledWidth / srcWidth;

        int x = scaledWidth > targetWidth ? (scaledWidth - targetWidth) / 2 : 0;
        int y = scaledHeight > targetHeight ? (scaledHeight - targetHeight) / 2 : 0;
        canvas.translate(-x, -y);
        canvas.scale(scale, scale);
    }


    public static Bitmap blur(Context context, Drawable drawable, int width, int height, int radius,
                              float scaleFactor) {
        if (drawable == null) return null;
        if (radius <= 0) {
            return BitmapUtils.toBitmap(drawable);
        }
        int srcWidth = drawable.getIntrinsicWidth();
        int srcHeight = drawable.getIntrinsicHeight();
        if (srcHeight <= 0 || srcWidth < 0){
            return null;
        }

        Bitmap overlay = Bitmap.createBitmap((int) (width * scaleFactor),
                (int) (height * scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        cropCanvas(canvas, srcWidth, srcHeight, overlay.getWidth(), overlay.getHeight());
        drawable.setBounds(0, 0, srcWidth, srcHeight);
        drawable.draw(canvas);
        //overlay = doBlur(overlay,radius);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            try {
                overlay = RSBlur.blur(context, overlay, radius);
            } catch (RSRuntimeException e) {
                overlay = FastBlur.blur(overlay, radius, true);
            }
        } else {
            overlay = FastBlur.blur(overlay, radius, true);
        }

        return overlay;
    }
}
