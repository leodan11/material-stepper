package com.github.leodan11.stepper.internal.feedback;

import static com.github.leodan11.stepper.internal.util.AnimationUtil.ALPHA_OPAQUE;

import android.view.View;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import com.github.leodan11.stepper.R;
import com.github.leodan11.stepper.StepperLayout;

/**
 * Feedback stepper type which partially fades the content out.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class ContentFadeStepperFeedbackType implements StepperFeedbackType {

    @NonNull
    private final View mPager;

    @FloatRange(from = 0.0f, to = 1.0f)
    private final float mFadeOutAlpha;

    public ContentFadeStepperFeedbackType(@NonNull StepperLayout stepperLayout) {
        mPager = stepperLayout.findViewById(R.id.ms_stepPager);
        mFadeOutAlpha = stepperLayout.getContentFadeAlpha();
    }

    @Override
    public void showProgress(@NonNull String progressMessage) {
        mPager.animate()
                .alpha(mFadeOutAlpha)
                .setDuration(PROGRESS_ANIMATION_DURATION);
    }

    @Override
    public void hideProgress() {
        mPager.animate()
                .alpha(ALPHA_OPAQUE)
                .setDuration(PROGRESS_ANIMATION_DURATION);
    }
}
