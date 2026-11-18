package com.github.leodan11.stepper.internal.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.util.TypedValue;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

/**
 * Resolves the colors of the stepper from the current theme so that the widget follows both
 * Material 2 and Material 3 themes (including their dark variants).<br>
 * Every method takes a fallback color which is used when the theme does not define the attribute,
 * so the widget still works with themes that are not Material based.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class ThemeUtils {

    /**
     * Opacity used for the inactive step indicators and for the tabs divider.
     */
    public static final float ALPHA_INACTIVE_STEP = 0.24f;

    /**
     * Opacity used for the step subtitle.
     */
    public static final float ALPHA_STEP_SUBTITLE = 0.6f;

    /**
     * Opacity used for the disabled navigation buttons.
     */
    public static final float ALPHA_DISABLED_BUTTON = 0.38f;

    // colorPrimary and colorError are declared by AppCompat while colorSurface and colorOnSurface
    // are declared by the Material Components library. Both attributes are resolved from the theme
    // at runtime, so they work with Material 2 and Material 3 themes.
    private static final int ATTR_COLOR_PRIMARY = androidx.appcompat.R.attr.colorPrimary;
    private static final int ATTR_COLOR_ERROR = androidx.appcompat.R.attr.colorError;
    private static final int ATTR_COLOR_SURFACE = com.google.android.material.R.attr.colorSurface;
    private static final int ATTR_COLOR_ON_SURFACE = com.google.android.material.R.attr.colorOnSurface;

    private ThemeUtils() {
    }

    /**
     * @return {@code ?attr/colorPrimary} of the current theme or {@code fallbackColor} when it is not defined
     */
    @ColorInt
    public static int getPrimaryColor(@NonNull Context context, @ColorInt int fallbackColor) {
        return resolveColor(context, ATTR_COLOR_PRIMARY, fallbackColor);
    }

    /**
     * @return {@code ?attr/colorSurface} of the current theme or {@code fallbackColor} when it is not defined
     */
    @ColorInt
    public static int getSurfaceColor(@NonNull Context context, @ColorInt int fallbackColor) {
        return resolveColor(context, ATTR_COLOR_SURFACE, fallbackColor);
    }

    /**
     * @return {@code ?attr/colorOnSurface} of the current theme or {@code fallbackColor} when it is not defined
     */
    @ColorInt
    public static int getOnSurfaceColor(@NonNull Context context, @ColorInt int fallbackColor) {
        return resolveColor(context, ATTR_COLOR_ON_SURFACE, fallbackColor);
    }

    /**
     * @return {@code ?attr/colorError} of the current theme or {@code fallbackColor} when it is not defined
     */
    @ColorInt
    public static int getErrorColor(@NonNull Context context, @ColorInt int fallbackColor) {
        return resolveColor(context, ATTR_COLOR_ERROR, fallbackColor);
    }

    /**
     * Resolves a color attribute of the current theme.
     *
     * @param context       context used to resolve the attribute
     * @param attrResId     color attribute to resolve, e.g. {@code R.attr.colorPrimary}
     * @param fallbackColor color returned when the attribute is not defined by the theme
     * @return the resolved color or {@code fallbackColor}
     */
    @ColorInt
    public static int resolveColor(@NonNull Context context, @AttrRes int attrResId, @ColorInt int fallbackColor) {
        final Resources.Theme theme = context.getTheme();
        if (theme == null) {
            return fallbackColor;
        }

        final TypedValue value = new TypedValue();
        if (!theme.resolveAttribute(attrResId, value, true)) {
            return fallbackColor;
        }

        if (value.resourceId == 0) {
            return value.type >= TypedValue.TYPE_FIRST_COLOR_INT && value.type <= TypedValue.TYPE_LAST_COLOR_INT
                    ? value.data
                    : fallbackColor;
        }

        try {
            return ContextCompat.getColor(context, value.resourceId);
        } catch (Resources.NotFoundException e) {
            return fallbackColor;
        }
    }

    /**
     * @return {@code color} with the given alpha applied
     */
    @ColorInt
    public static int withAlpha(@ColorInt int color, float alpha) {
        return ColorUtils.setAlphaComponent(color, Math.round(255f * alpha));
    }

    /**
     * Creates a color state list for the navigation buttons where the disabled state is dimmed.
     *
     * @param enabledColor  color used when the button is enabled
     * @param disabledColor color used when the button is disabled
     * @return color state list with an enabled and a disabled state
     */
    @NonNull
    public static ColorStateList createTextButtonColorStateList(@ColorInt int enabledColor, @ColorInt int disabledColor) {
        return new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_enabled},
                        new int[0]
                },
                new int[]{
                        disabledColor,
                        enabledColor
                });
    }

}
