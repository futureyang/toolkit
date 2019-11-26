package com.future.toolkit.image;

import android.text.TextUtils;

/**
 * Created by yangqc on 2019/10/28
 *
 * @Author yangqc
 */

public class OSSURLConvert {


    private static final int IMAGE_DEFAULT_QUALITY = 50;
    /**
     * quality 图片相对原图的质量 q 取值范围[1-100]
     * 格式为webp
     */
    private static final String URL_SUFFIX = "%s?x-oss-process=image/resize,m_fill,w_%s,h_%s/quality,q_%S/format,webp";
    private static final String URL_SUFFIX_ONLY_QUALITY = "%s?x-oss-process=image/quality,q_%S/format,webp";

    public static String convertImageUrl(String sourceUrl, int width, int height){
        return convertImageUrl(sourceUrl, width, height, IMAGE_DEFAULT_QUALITY);
    }

    public static String convertImageUrl(String sourceUrl, int width, int height, int quality){
        if(TextUtils.isEmpty(sourceUrl)){
            return "";
        }
        String url = sourceUrl.replaceAll("\\\\", "/");
        return String.format(URL_SUFFIX, url, width, height, quality);
    }

    public static String convertImageUrl(String sourceUrl, int quality){
        if(TextUtils.isEmpty(sourceUrl)){
            return "";
        }
        String url = sourceUrl.replaceAll("\\\\", "/");
        return String.format(URL_SUFFIX_ONLY_QUALITY, url, quality);
    }
}
