package com.github.leodan11.stepper;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

/**
 * A {@link Step} which can block clicking on the next button/tab
 * and perform some operations before switching to the next step.
 */

public interface BlockingStep extends Step {

    /**
     * Notifies this step that the next button/tab was clicked, the step was verified,
     * and the user can proceed to the next step. This allows the current step to perform
     * last-minute operations (e.g., a network call) before switching to the next step.
     * <p>
     * The {@link StepperLayout.OnNextClickedCallback#goToNextStep()} method
     * must be called once these operations finish.
     *
     * @param callback callback to invoke once ready to switch to the next step
     */
    @UiThread
    default void onNextClicked(@NonNull StepperLayout.OnNextClickedCallback callback) {
        callback.goToNextStep();
    }

    /**
     * Notifies this step that the complete button/tab was clicked, the step was verified,
     * and the user intends to complete the flow. This allows the current step to perform
     * last-minute operations (e.g., a network call) before finishing the flow.
     * <p>
     * The {@link StepperLayout.OnCompleteClickedCallback#complete()} method
     * must be called once these operations finish.
     *
     * @param callback callback to invoke once ready to complete the flow
     */
    @UiThread
    default void onCompleteClicked(@NonNull StepperLayout.OnCompleteClickedCallback callback) {
        callback.complete();
    }

    /**
     * Notifies this step that the previous button/tab was clicked. This allows the current step
     * to perform last-minute operations (e.g., a network call) before switching to the previous step.
     * <p>
     * The {@link StepperLayout.OnBackClickedCallback#goToPrevStep()} method
     * must be called once these operations finish.
     *
     * @param callback callback to invoke once ready to switch to the previous step
     */
    @UiThread
    default void onBackClicked(@NonNull StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

}
