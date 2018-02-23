package com.example.dell.mymediaplay.tool;

import android.graphics.Bitmap;

/**
 * Created by DELL on 2016/5/3.
 */
public class LoadedImage {
    public Bitmap mBitmap;

    public LoadedImage(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }
}
