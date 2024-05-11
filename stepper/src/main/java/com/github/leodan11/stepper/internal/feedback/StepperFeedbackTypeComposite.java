package com.github.leodan11.stepper.internal.feedback;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import java.util.ArrayList;
import java.util.List;

/**
 * A stepper feedback type which is a composition of other feedback type, which allows to select only a group of feedback types.
 * See Stepper feedback section in <a href="https://material.io/guidelines/components/steppers.html#steppers-types-of-steppers">...</a>
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class StepperFeedbackTypeComposite implements StepperFeedbackType {

    @NonNull
    private final List<StepperFeedbackType> mChildren = new ArrayList<>();

    @Override
    public void showProgress(@NonNull String progressMessage) {
        for (StepperFeedbackType child : mChildren) {
            child.showProgress(progressMessage);
        }
    }

    @Override
    public void hideProgress() {
        for (StepperFeedbackType child : mChildren) {
            child.hideProgress();
        }
    }

    /**
     * Adds a child component to this composite.
     * @param component child to add
     */
    public void addComponent(@NonNull StepperFeedbackType component) {
        mChildren.add(component);
    }

    public List<StepperFeedbackType> getChildComponents() {
        return mChildren;
    }
}