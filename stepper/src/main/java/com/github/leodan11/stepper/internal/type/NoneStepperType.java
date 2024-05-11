package com.github.leodan11.stepper.internal.type;

import com.github.leodan11.stepper.StepperLayout;

public class NoneStepperType extends AbstractStepperType {
    public NoneStepperType(StepperLayout stepperLayout) {
        super(stepperLayout);
    }

    @Override
    public void onStepSelected(int newStepPosition, boolean userTriggeredChange) {
    }
}
