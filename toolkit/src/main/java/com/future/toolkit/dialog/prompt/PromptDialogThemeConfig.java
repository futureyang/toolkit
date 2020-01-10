package com.future.toolkit.dialog.prompt;

import androidx.annotation.ColorInt;

/**
 * Created by yangqc on 2019/11/8
 *
 * @Author yangqc
 */
public class PromptDialogThemeConfig {
    public static final PromptDialogThemeConfig INSTANCE = new PromptDialogThemeConfig();
    @ColorInt
    public int themeColor;

    public void init(int themeColor){
        this.themeColor = themeColor;
    }

    public static PromptDialogThemeConfig getInstance(){
        return INSTANCE;
    }
}
