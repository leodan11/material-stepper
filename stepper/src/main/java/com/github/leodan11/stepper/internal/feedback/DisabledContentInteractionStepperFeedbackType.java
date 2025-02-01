package com.github.leodan11.stepper.internal.feedback;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import com.github.leodan11.stepper.R;
import com.github.leodan11.stepper.StepperLayout;
import com.github.leodan11.stepper.internal.widget.StepViewPager;

/**
 * Feedback stepper type which intercepts touch events on the steps' content and ignores them.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class DisabledContentInteractionStepperFeedbackType implements StepperFeedbackType {

    @NonNull
    private final StepViewPager mStepPager;

    public DisabledContentInteractionStepperFeedbackType(@NonNull StepperLayout stepperLayout) {
        mStepPager = stepperLayout.findViewById(R.id.ms_stepPager);
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
        mStepPager.setBlockTouchEventsFromChildrenEnabled(!enabled);
    }

}
