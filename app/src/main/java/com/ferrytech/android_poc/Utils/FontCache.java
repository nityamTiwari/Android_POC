package com.ferrytech.android_poc.Utils;

import android.content.Context;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

import java.util.HashMap;
import java.util.Map;

public class FontCache {

    private static final Map<Integer, Typeface> cache = new HashMap<>();

    public static Typeface get(Context context, int fontId) {

        if (!cache.containsKey(fontId)) {
            Typeface tf = ResourcesCompat.getFont(context.getApplicationContext(), fontId);
            cache.put(fontId, tf);
        }

        return cache.get(fontId);
    }
}