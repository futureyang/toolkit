package com.future.toolkit.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Created by yangqc on 2019/9/23
 *
 * @Author yangqc
 */
public class ListUtils {

    public interface ItemFilter<T> {
        public boolean filter(Iterator iterator, T item);
    }

    /**
     * 过滤数组等工具方法
     *
     * @param iterable
     * @param filter
     * @return
     */
    public static <T> T filter(@NonNull Iterable<T> iterable,
                               @NonNull ItemFilter<T> filter) {
        Iterator<T> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            T obj = iterator.next();
            if (filter.filter(iterator, obj)) {
                return obj;
            }
        }
        return null;
    }

    /**
     * 比较两个数组是否相等
     *
     * @param list1
     * @param list2
     * @return
     */
    public static boolean isSameList(@Nullable List<? extends Object> list1,
                                     @Nullable List<? extends Object> list2) {
        if (list1 == null || list2 == null) {
            return false;
        }
        if (list1.size() != list2.size()) {
            return false;
        }
        int size = list1.size();
        for (int i = 0; i < size; i++) {
            Object o1 = list1.get(i);
            Object o2 = list2.get(i);
            if (o1 != o2 && !(o1 != null && o1.equals(o2))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(@Nullable Collection c) {
        return c == null || c.isEmpty();
    }

    /**
     * 查找位置
     * @param iterable
     * @param target
     * @return
     */
    public static <T> int indexOf(@NonNull Iterable<T> iterable, T target) {
        int index = -1;
        Iterator<T> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            T obj = iterator.next();
            index++;
            if (Objects.equals(obj, target)) {
                return index;
            }
        }
        return -1;
    }
}
