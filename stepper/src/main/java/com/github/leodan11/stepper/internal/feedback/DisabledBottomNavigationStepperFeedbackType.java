package com.github.leodan11.stepper.internal.feedback;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import com.github.leodan11.stepper.StepperLayout;

/**
 * Feedback stepper type which disables the buttons in the bottom navigation when an operation is in progress.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class DisabledBottomNavigationStepperFeedbackType implements StepperFeedbackType {

    @NonNull
    private final StepperLayout mStepperLayout;

    public DisabledBottomNavigationStepperFeedbackType(@NonNull StepperLayout stepperLayout) {
        mStepperLayout = stepperLayout;
    }

    @Override
    public void showProgress(@NonNull String progressMessage) {
        setButtonsEnabled(false);
    }

    @Override
    public void hideProgress() {
        setButtonsEnabled(true);
    }

    private void setButtonsEnabled(boolean enabled) {
        mStepperLayout.setNextButtonEnabled(enabled);
        mStepperLayout.setCompleteButtonEnabled(enabled);
        mStepperLayout.setBackButtonEnabled(enabled);
    }

}
