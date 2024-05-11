package com.github.leodan11.stepper.internal.widget.pagetransformer;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.viewpager.widget.ViewPager;

import com.github.leodan11.stepper.R;
import com.github.leodan11.stepper.internal.widget.StepViewPager;

/**
 * Creates a page transformer to be used by {@link StepViewPager}.
 *
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class StepPageTransformerFactory {

    private StepPageTransformerFactory() {
    }

    /**
     * Creates a {@link ViewPager.PageTransformer}.
     * If layout direction is in RTL it returns {@link StepperRtlPageTransformer}, <i>null</i> otherwise.
     * @param context context
     * @return page transformer
     */
    @Nullable
    public static ViewPager.PageTransformer createPageTransformer(@NonNull Context context) {
        boolean rtlEnabled = context.getResources().getBoolean(R.bool.ms_rtlEnabled);
        return rtlEnabled ? new StepperRtlPageTransformer() : null;
    }

}
