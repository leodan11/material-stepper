package com.github.leodan11.stepper.internal.util;

import android.animation.Animator;
import android.view.View;
import android.view.ViewPropertyAnimator;

import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

/**
 * Util class containing static methods to simplify animation.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class AnimationUtil {

    @Retention(SOURCE)
    @IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
    @interface Visibility {
    }

    public static final float ALPHA_OPAQUE = 1.0f;
    public static final float ALPHA_INVISIBLE = 0.0f;
    public static final float ALPHA_HALF = 0.5f;

    private static final int DEFAULT_DURATION = 300;

    private AnimationUtil() {
        throw new AssertionError("Please do not instantiate this class");
    }

    /**
     * Animate the View's visibility using a fade animation.
     * @param view The View to be animated
     * @param visibility View visibility constant, can be either View.VISIBLE, View.INVISIBLE or View.GONE
     * @param animate true if the visibility should be changed with an animation, false if instantaneously
     */
    public static void fadeViewVisibility(@NonNull final View view, @Visibility final int visibility, boolean animate) {
        ViewPropertyAnimator animator = view.animate();
        animator.cancel();
        animator.alpha(visibility == View.VISIBLE ? ALPHA_OPAQUE : ALPHA_INVISIBLE)
                .setDuration(animate ? DEFAULT_DURATION : 0)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animation) {
                        if (visibility == View.VISIBLE) {
                            view.setVisibility(visibility);
                        }
                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animation) {
                        if (visibility == View.INVISIBLE || visibility == View.GONE) {
                            view.setVisibility(visibility);
                        }
                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animation) {
                        if (visibility == View.INVISIBLE || visibility == View.GONE) {
                            view.setVisibility(visibility);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animation) {
                    }
                }).start();
    }
}
