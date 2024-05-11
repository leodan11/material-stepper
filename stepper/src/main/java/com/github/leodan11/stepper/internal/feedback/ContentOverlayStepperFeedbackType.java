package com.github.leodan11.stepper.internal.feedback;

import static com.github.leodan11.stepper.internal.util.AnimationUtil.ALPHA_INVISIBLE;
import static com.github.leodan11.stepper.internal.util.AnimationUtil.ALPHA_OPAQUE;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import com.github.leodan11.stepper.R;
import com.github.leodan11.stepper.StepperLayout;

/**
 * Feedback stepper type which shows a dimmed overlay over the content.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class ContentOverlayStepperFeedbackType implements StepperFeedbackType {

    @NonNull
    private final View mOverlayView;

    public ContentOverlayStepperFeedbackType(@NonNull StepperLayout stepperLayout) {
        mOverlayView = stepperLayout.findViewById(R.id.ms_stepPagerOverlay);
        mOverlayView.setVisibility(View.VISIBLE);
        mOverlayView.setAlpha(ALPHA_INVISIBLE);
        final int contentOverlayBackground = stepperLayout.getContentOverlayBackground();
        if (contentOverlayBackground != 0)  {
            mOverlayView.setBackgroundResource(contentOverlayBackground);
        }
    }

    @Override
    public void showProgress(@NonNull String progressMessage) {
        mOverlayView.animate()
                .alpha(ALPHA_OPAQUE)
                .setDuration(PROGRESS_ANIMATION_DURATION);
    }

    @Override
    public void hideProgress() {
        mOverlayView.animate()
                .alpha(ALPHA_INVISIBLE)
                .setDuration(PROGRESS_ANIMATION_DURATION);
    }
}
