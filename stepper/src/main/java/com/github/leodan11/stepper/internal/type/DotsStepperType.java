package com.github.leodan11.stepper.internal.type;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import com.github.leodan11.stepper.R;
import com.github.leodan11.stepper.StepperLayout;
import com.github.leodan11.stepper.adapter.StepAdapter;
import com.github.leodan11.stepper.internal.widget.DottedProgressBar;

/**
 * Stepper type which displays mobile step dots.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class DotsStepperType extends AbstractStepperType {

    private static final int EDIT_MODE_DOT_COUNT = 3;

    private final DottedProgressBar mDottedProgressBar;

    public DotsStepperType(StepperLayout stepperLayout) {
        super(stepperLayout);
        mDottedProgressBar = stepperLayout.findViewById(R.id.ms_stepDottedProgressBar);

        mDottedProgressBar.setSelectedColor(getSelectedColor());
        mDottedProgressBar.setUnselectedColor(getUnselectedColor());

        if (stepperLayout.isInEditMode()) {
            mDottedProgressBar.setDotCount(EDIT_MODE_DOT_COUNT);
            mDottedProgressBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStepSelected(int newStepPosition, boolean userTriggeredChange) {
        mDottedProgressBar.setCurrent(newStepPosition, userTriggeredChange);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNewAdapter(@NonNull StepAdapter stepAdapter) {
        super.onNewAdapter(stepAdapter);
        final int stepCount = stepAdapter.getCount();
        mDottedProgressBar.setDotCount(stepCount);
        mDottedProgressBar.setVisibility(stepCount > 1 ? View.VISIBLE : View.GONE);
    }
}
