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
     * <p></p>Note: This method is called to determine whether the step is valid,
     * but it is not tied directly to the user clicking the Next or Complete button.
     *
     * <p></p>If you need to handle cases where the user clicks the Next/Complete button and
     * validation fails, override {@link #onError(VerificationError)}.
     *
     * @return the cause of the validation failure, or {@code null} if validation passed
     */
    @Nullable
    default VerificationError verifyStep() {
        return null;
    }

    /**
     * Called when this step is selected in the stepper layout.
     * Use this to update the UI or perform actions when the step becomes active.
     */
    default void onSelected() {
        // Optional override
    }

    /**
     * Called when validation fails after the user clicks the Next or Complete button.
     * Override this method to display an error message or highlight invalid input.
     *
     * @param verificationError the cause of the validation failure
     */
    default void onError(@NonNull VerificationError verificationError) {
        // Optional override: handle step validation error
    }
}
