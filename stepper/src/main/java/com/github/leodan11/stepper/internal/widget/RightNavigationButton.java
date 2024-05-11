package com.github.leodan11.stepper.internal.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.RestrictTo;
import androidx.appcompat.widget.AppCompatButton;

import com.github.leodan11.stepper.R;
import com.github.leodan11.stepper.StepperLayout;

/**
 * A button with an extra state to distinguish if the user can go to the next step.<br>
 * There is a custom state (<i>app:state_verification_failed</i>) which should be set to true
 * in the color state list if the tint color of the button should change in this scenario.<br>
 * The state of the button can be toggled via this view's {@link #setVerificationFailed(boolean)}
 * or in {@link StepperLayout}'s
 * {@link StepperLayout#setCompleteButtonVerificationFailed(boolean)} and
 * {@link StepperLayout#setCompleteButtonVerificationFailed(boolean)}.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class RightNavigationButton extends AppCompatButton {

    private static final int[] STATE_VERIFICATION_FAILED = {R.attr.state_verification_failed};

    private boolean mVerificationFailed = false;

    public RightNavigationButton(Context context) {
        this(context, null);
    }

    public RightNavigationButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RightNavigationButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        if (mVerificationFailed) {
            final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
            mergeDrawableStates(drawableState, STATE_VERIFICATION_FAILED);
            return drawableState;
        } else {
            return super.onCreateDrawableState(extraSpace);
        }
    }

    public void setVerificationFailed(boolean verificationFailed) {
        if (this.mVerificationFailed != verificationFailed) {
            this.mVerificationFailed = verificationFailed;
            refreshDrawableState();
        }
    }

}
