package com.github.leodan11.stepper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Represents a single step within a stepper layout.
 * <p></p>
 * This interface defines lifecycle and validation methods that are called
 * by the stepper engine during navigation between steps.
 */
public interface Step {

    /**
     * Validates the current step before allowing navigation to the next one.
     *
     * <p>Note: This method is called to determine whether the step is valid,
     * but it is not directly triggered by the user clicking the Next or Complete button.</p>
     *
     * <p>If you need to handle validation failure triggered by the user clicking Next/Complete,
     * override {@link #onError(VerificationError)}.</p>
     *
     * @return the cause of the validation failure, or {@code null} if the step is valid
     */
    @Nullable
    default VerificationError verifyStep() {
        return null;
    }

    /**
     * Called when this step becomes the currently selected step in the stepper layout.
     * Use this callback to update the UI or perform any necessary actions.
     */
    default void onSelected() {
        // Optional override
    }

    /**
     * Called when validation fails after the user clicks the Next or Complete button.
     * Override this method to handle the validation error, such as showing an error message.
     *
     * @param verificationError the cause of the validation failure
     */
    default void onError(@NonNull VerificationError verificationError) {
        // Optional override: handle validation failure
    }

}
