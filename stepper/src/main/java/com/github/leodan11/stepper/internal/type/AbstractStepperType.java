package com.github.leodan11.stepper.internal.type;

import android.util.SparseArray;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

import com.github.leodan11.stepper.StepperLayout;
import com.github.leodan11.stepper.VerificationError;
import com.github.leodan11.stepper.adapter.StepAdapter;

/**
 * A base stepper type all stepper types must extend.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public abstract class AbstractStepperType {

    /**
     * Mobile step dots
     */
    public static final int DOTS = 0x01;

    /**
     * Mobile step progress bar
     */
    public static final int PROGRESS_BAR = 0x02;

    /**
     * Horizontal stepper with tabs
     */
    public static final int TABS = 0x03;

    /**
     * No indicator
     */
    public static final int NONE = 0x04;

    final StepperLayout mStepperLayout;

    final SparseArray<VerificationError> mStepErrors = new SparseArray<>();

    public AbstractStepperType(StepperLayout stepperLayout) {
        this.mStepperLayout = stepperLayout;
    }

    /**
     * Called when a step gets selected as the new current step.
     * @param newStepPosition new current step position
     * @param userTriggeredChange <code>true</code> if current step position changed as a direct result of user interaction
     */
    public abstract void onStepSelected(int newStepPosition, boolean userTriggeredChange);

    /**
     * Called to set whether the stepPosition has an error or not, changing it's appearance.
     * @param stepPosition the step to set the error
     * @param error error instance or null if no error
     */
    public void setError(int stepPosition, @Nullable VerificationError error) {
        mStepErrors.put(stepPosition, error);
    }

    /**
     * Checks if there's an error for the step.
     *
     * @param stepPosition the step to check for error
     * @return an error for this step or null if no error
     */
    @Nullable
    public VerificationError getErrorAtPosition(int stepPosition) {
        return mStepErrors.get(stepPosition);
    }

    /**
     * Called when {@link StepperLayout}'s adapter gets changed
     * @param stepAdapter new stepper adapter
     */
    @CallSuper
    public void onNewAdapter(@NonNull StepAdapter stepAdapter) {
        mStepErrors.clear();
    }

    @ColorInt
    protected int getSelectedColor() {
        return mStepperLayout.getSelectedColor();
    }

    @ColorInt
    protected int getUnselectedColor() {
        return mStepperLayout.getUnselectedColor();
    }

}