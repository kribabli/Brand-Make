package com.festivalbanner.digitalposterhub.Utills;


import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.Nullable;

public class ColorResource extends Resources {

    public ColorResource(Resources original) {
        super(original.getAssets(), original.getDisplayMetrics(), original.getConfiguration());
    }

    @Override
    public int getColor(int id) throws NotFoundException {
        return getColor(id, null);
    }

    @Override
    public int getColor(int id, @Nullable Theme theme) throws NotFoundException {
        switch (getResourceEntryName(id)) {
            case "colorCustomText":
                // You can change the return value to an instance field that loads from SharedPreferences.
                return Color.RED; // used as an example. Change as needed.
            default:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return super.getColor(id, theme);
                }
                return super.getColor(id);
        }
    }
/*public ColorResource(AssetManager assets, DisplayMetrics metrics, Configuration config) {
        super(assets, metrics, config);
    }*/


}
