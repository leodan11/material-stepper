package com.github.leodan11.stepper.internal.feedback;

import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import com.github.leodan11.stepper.R;
import com.github.leodan11.stepper.StepperLayout;

/**
 * Feedback stepper type which displays a progress bar on top of the steps' content.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class ContentProgressStepperFeedbackType implements StepperFeedbackType {

    @NonNull
    private final ProgressBar mPagerProgressBar;

    public ContentProgressStepperFeedbackType(@NonNull StepperLayout stepperLayout) {
        mPagerProgressBar = (ProgressBar) stepperLayout.findViewById(R.id.ms_stepPagerProgressBar);
    }

    @Override
    public void showProgress(@NonNull String progressMessage) {
        mPagerProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mPagerProgressBar.setVisibility(View.GONE);
    }
}
