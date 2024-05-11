package com.github.leodan11.stepper.internal.type;

import android.util.Log;

import androidx.annotation.RestrictTo;

import com.github.leodan11.stepper.StepperLayout;

/**
 * Factory class for creating stepper types.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class StepperTypeFactory {

    private static final String TAG = StepperTypeFactory.class.getSimpleName();

    /**
     * Creates a stepper type for provided arguments.
     * @param stepType step type, one of <code>attrs - ms_stepperType</code>
     * @param stepperLayout stepper layout to use with this stepper type
     * @return a stepper type
     */
    public static AbstractStepperType createType(int stepType, StepperLayout stepperLayout) {
        switch (stepType) {
            case AbstractStepperType.DOTS:
                return new DotsStepperType(stepperLayout);
            case AbstractStepperType.PROGRESS_BAR:
                return new ProgressBarStepperType(stepperLayout);
            case AbstractStepperType.TABS:
                return new TabsStepperType(stepperLayout);
            case AbstractStepperType.NONE:
                return new NoneStepperType(stepperLayout);
            default:
                Log.e(TAG, "Unsupported type: " + stepType);
                throw new IllegalArgumentException("Unsupported type: " + stepType);
        }
    }
}
