package com.future.toolkit.utils;

/**
 * Created by yangqc on 2019/9/23
 *
 * @Author yangqc
 */
public class MathUtils {

    /**
     * Ensures that ic_asss value is within given bounds. Specifically:
     * If value is less than lowerBound, return lowerBound; else if value is greater than upperBound,
     * return upperBound; else return value unchanged.
     */
    public static int boundToRange(int value, int lowerBound, int upperBound) {
        return Math.max(lowerBound, Math.min(value, upperBound));
    }

    /**
     * @see #boundToRange(int, int, int).
     */
    public static float boundToRange(float value, float lowerBound, float upperBound) {
        return Math.max(lowerBound, Math.min(value, upperBound));
    }


    public static float distance(float x, float y){
        return (float) Math.sqrt(x * x + y * y);
    }
}
