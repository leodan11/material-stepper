package com.github.leodan11.stepper;

/**
 * An error which may be returned by any given step.
 * It contains the message explaining the cause.
 */
public class VerificationError {

    /**
     * A message explaining the cause of the error.
     */
    private final String mErrorMessage;

    public VerificationError(String errorMessage) {
        this.mErrorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

}
