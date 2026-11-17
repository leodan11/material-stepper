package com.github.leodan11.stepper.internal.feedback;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import com.github.leodan11.stepper.R;
import com.github.leodan11.stepper.StepperLayout;

/**
 * Feedback stepper type which intercepts touch events on the steps' content and ignores them.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class DisabledContentInteractionStepperFeedbackType implements StepperFeedbackType {

    @NonNull
    private final View mContentTouchBlocker;

    public DisabledContentInteractionStepperFeedbackType(@NonNull StepperLayout stepperLayout) {
        mContentTouchBlocker = stepperLayout.findViewById(R.id.ms_stepPagerTouchBlocker);
    }

    @Override
    public void showProgress(@NonNull String progressMessage) {
        setContentInteractionEnabled(false);
    }

    @Override
    public void hideProgress() {
        setContentInteractionEnabled(true);
    }

    private void setContentInteractionEnabled(boolean enabled) {
        mContentTouchBlocker.setVisibility(enabled ? View.GONE : View.VISIBLE);
    }

}
