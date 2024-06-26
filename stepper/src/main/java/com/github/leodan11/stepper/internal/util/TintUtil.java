package com.github.leodan11.stepper.internal.util;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 * Utility class for tinting drawables/widgets.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class TintUtil {

    private static final String TAG = TintUtil.class.getSimpleName();

    /**
     * Tints TextView's text color and it's compound drawables
     * @param textview text view to tint
     * @param tintColor color state list to use for tinting
     */
    public static void tintTextView(@NonNull TextView textview, ColorStateList tintColor) {
        textview.setTextColor(tintColor);
        Drawable[] drawables = textview.getCompoundDrawablesRelative();
        textview.setCompoundDrawablesRelative(
                tintDrawable(drawables[0], tintColor),
                tintDrawable(drawables[1], tintColor),
                tintDrawable(drawables[2], tintColor),
                tintDrawable(drawables[3], tintColor));
    }

    /**
     * Tints a drawable with the provided color
     * @param drawable drawable to tint
     * @param color tint color
     * @return tinted drawable
     */
    public static Drawable tintDrawable(@Nullable Drawable drawable, @ColorInt int color) {
        if (drawable != null) {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
        return drawable;
    }

    /**
     * Tints a drawable with the provided color state list
     * @param drawable drawable to tint
     * @param color tint color state list
     * @return tinted drawable
     */
    public static Drawable tintDrawable(@Nullable Drawable drawable, ColorStateList color) {
        if (drawable != null) {
            drawable = DrawableCompat.unwrap(drawable);
            Rect bounds = drawable.getBounds();
            drawable = DrawableCompat.wrap(drawable);
            // bounds can be all set to zeros when inflating vector drawables in Android Support Library 23.3.0...
            if (bounds.right == 0 || bounds.bottom == 0) {
                if (drawable.getIntrinsicHeight() != -1 && drawable.getIntrinsicWidth() != -1) {
                    bounds.right = drawable.getIntrinsicWidth();
                    bounds.bottom = drawable.getIntrinsicHeight();
                } else {
                    Log.w(TAG, "Cannot tint drawable because its bounds cannot be determined!");
                    return DrawableCompat.unwrap(drawable);
                }
            }
            DrawableCompat.setTintList(drawable, color);
            drawable.setBounds(bounds);
        }
        return drawable;
    }

}
