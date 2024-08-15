package com.github.leodan11.stepper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A base step interface which all {@link StepperLayout} steps must implement.
 */
public interface Step {

    /**
     * Checks if the stepper can go to the next step after this step.<br>
     * <b>This does not mean the user clicked on the Next/Complete button.</b><br>
     * If the user clicked the Next/Complete button and wants to be informed of that error
     * he should handle this in {@link #onError(VerificationError)}.
     * @return the cause of the validation failure or <i>null</i> if step was validated successfully
     */
    @Nullable
    default VerificationError verifyStep() { return null; }

    /**
     * Called when this step gets selected in the the stepper layout.
     */
    default void onSelected() { }

    /**
     * Called when the user clicked on the Next/Complete button and the step verification failed.
     * @param error the cause of the validation failure
     */
    default void onError(@NonNull VerificationError error) { }

}
