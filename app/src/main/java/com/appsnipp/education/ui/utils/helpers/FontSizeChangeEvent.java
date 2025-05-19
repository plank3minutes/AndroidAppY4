package com.appsnipp.education.ui.utils.helpers;

public class FontSizeChangeEvent {
    private final int newFontSize;

    public FontSizeChangeEvent(int newFontSize) {
        this.newFontSize = newFontSize;
    }

    public int getNewFontSize() {
        return newFontSize;
    }
} 