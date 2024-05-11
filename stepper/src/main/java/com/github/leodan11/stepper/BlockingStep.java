package com.github.leodan11.stepper;

import androidx.annotation.UiThread;

/**
 * A {@link Step} which can block clicking on the next button/tab
 * and perform some operations before switching to the next step.
 */

public interface BlockingStep extends Step {

    /**
     * Notifies this step that the next button/tab was clicked, the step was verified
     * and the user can go to the next step. This is so that the current step might perform
     * some last minute operations e.g. a network call before switching to the next step.
     * {@link StepperLayout.OnNextClickedCallback#goToNextStep()} must be called once these operations finish.
     *
     * @param callback callback to call once the user wishes to finally switch to the next step
     */
    @UiThread
    void onNextClicked(StepperLayout.OnNextClickedCallback callback);

    /**
     * Notifies this step that the complete button/tab was clicked, the step was verified
     * and the user can complete the flow. This is so that the current step might perform
     * some last minute operations e.g. a network call before completing the flow.
     * {@link StepperLayout.OnCompleteClickedCallback#complete()} must be called once these operations finish.
     *
     * @param callback callback to call once the user wishes to complete the flow
     */
    @UiThread
    void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback);

    /**
     * Notifies this step that the previous button/tab was clicked. This is so that the current step might perform
     * some last minute operations e.g. a network call before switching to previous step.
     * {@link StepperLayout.OnBackClickedCallback#goToPrevStep()} must be called once these operations finish.
     *
     * @param callback callback to call once the user wishes to finally switch to the previous step
     */
    @UiThread
    void onBackClicked(StepperLayout.OnBackClickedCallback callback);

}
