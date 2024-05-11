package com.github.leodan11.stepper.internal.type;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import com.github.leodan11.stepper.R;
import com.github.leodan11.stepper.StepperLayout;
import com.github.leodan11.stepper.adapter.StepAdapter;
import com.github.leodan11.stepper.internal.widget.ColorableProgressBar;

/**
 * Stepper type which displays a mobile step progress bar.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class ProgressBarStepperType extends AbstractStepperType {

    private final ColorableProgressBar mProgressBar;

    public ProgressBarStepperType(StepperLayout stepperLayout) {
        super(stepperLayout);
        mProgressBar = stepperLayout.findViewById(R.id.ms_stepProgressBar);
        mProgressBar.setProgressColor(getSelectedColor());
        mProgressBar.setProgressBackgroundColor(getUnselectedColor());
        if (stepperLayout.isInEditMode()) {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setProgressCompat(1, false);
            mProgressBar.setMax(3);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStepSelected(int newStepPosition, boolean userTriggeredChange) {
        mProgressBar.setProgressCompat(newStepPosition + 1, userTriggeredChange);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNewAdapter(@NonNull StepAdapter stepAdapter) {
        super.onNewAdapter(stepAdapter);
        final int stepCount = stepAdapter.getCount();
        mProgressBar.setMax(stepAdapter.getCount());
        mProgressBar.setVisibility(stepCount > 1 ? View.VISIBLE : View.GONE);
    }
}
