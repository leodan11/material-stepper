package com.github.leodan11.stepper.internal.feedback;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import com.github.leodan11.stepper.StepperLayout;

/**
 * Factory class for creating feedback stepper types.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class StepperFeedbackTypeFactory {

    private StepperFeedbackTypeFactory() {}

    /**
     * Creates a stepper feedback type for provided arguments.
     * It can be a composition of several feedback types depending on the provided flags.
     *
     * @param feedbackTypeMask step feedback type mask, should contain one or more from {@link StepperFeedbackType}
     * @param stepperLayout    stepper layout to use with the chosen stepper feedback type(s)
     * @return a stepper feedback type
     */
    @NonNull
    public static StepperFeedbackType createType(int feedbackTypeMask, @NonNull StepperLayout stepperLayout) {

        StepperFeedbackTypeComposite stepperFeedbackTypeComposite = new StepperFeedbackTypeComposite();

        if ((feedbackTypeMask & StepperFeedbackType.NONE) != 0) {
            //Add no more components if NONE type is selected
            return stepperFeedbackTypeComposite;
        }

        if ((feedbackTypeMask & StepperFeedbackType.TABS) != 0) {
            stepperFeedbackTypeComposite.addComponent(new TabsStepperFeedbackType(stepperLayout));
        }

        if ((feedbackTypeMask & StepperFeedbackType.CONTENT_PROGRESS) != 0) {
            stepperFeedbackTypeComposite.addComponent(new ContentProgressStepperFeedbackType(stepperLayout));
        }

        if ((feedbackTypeMask & StepperFeedbackType.CONTENT_FADE) != 0) {
            stepperFeedbackTypeComposite.addComponent(new ContentFadeStepperFeedbackType(stepperLayout));
        }

        if ((feedbackTypeMask & StepperFeedbackType.CONTENT_OVERLAY) != 0) {
            stepperFeedbackTypeComposite.addComponent(new ContentOverlayStepperFeedbackType(stepperLayout));
        }

        if ((feedbackTypeMask & StepperFeedbackType.DISABLED_BOTTOM_NAVIGATION) != 0) {
            stepperFeedbackTypeComposite.addComponent(new DisabledBottomNavigationStepperFeedbackType(stepperLayout));
        }

        if ((feedbackTypeMask & StepperFeedbackType.DISABLED_CONTENT_INTERACTION) != 0) {
            stepperFeedbackTypeComposite.addComponent(new DisabledContentInteractionStepperFeedbackType(stepperLayout));
        }

        return stepperFeedbackTypeComposite;
    }
}
