package com.github.leodan11.stepper.internal.feedback;

import static com.github.leodan11.stepper.internal.util.AnimationUtil.ALPHA_INVISIBLE;
import static com.github.leodan11.stepper.internal.util.AnimationUtil.ALPHA_OPAQUE;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import com.github.leodan11.stepper.R;
import com.github.leodan11.stepper.StepperLayout;

/**
 * Feedback stepper type which displays a progress message instead of the tabs.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class TabsStepperFeedbackType implements StepperFeedbackType {

    private final float mProgressMessageTranslationWhenHidden;

    private boolean mTabNavigationEnabled;

    @NonNull
    private final TextView mProgressMessageTextView;

    @NonNull
    private final View mTabsScrollingContainer;

    @NonNull
    private final StepperLayout mStepperLayout;

    public TabsStepperFeedbackType(@NonNull StepperLayout stepperLayout) {
        mProgressMessageTranslationWhenHidden = stepperLayout.getResources().getDimension(R.dimen.ms_progress_message_translation_when_hidden);
        mProgressMessageTextView = (TextView) stepperLayout.findViewById(R.id.ms_stepTabsProgressMessage);
        mTabsScrollingContainer = stepperLayout.findViewById(R.id.ms_stepTabsScrollView);
        mStepperLayout = stepperLayout;
        mProgressMessageTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgress(@NonNull String progressMessage) {
        mTabNavigationEnabled = mStepperLayout.isTabNavigationEnabled();
        setTabNavigationEnabled(false);
        mProgressMessageTextView.setText(progressMessage);
        mProgressMessageTextView.animate()
                .setStartDelay(PROGRESS_ANIMATION_DURATION)
                .alpha(ALPHA_OPAQUE)
                .translationY(0.0f)
                .setDuration(PROGRESS_ANIMATION_DURATION);
        mTabsScrollingContainer.animate()
                .alpha(ALPHA_INVISIBLE)
                .setStartDelay(0)
                .setInterpolator(new LinearInterpolator())
                .setDuration(PROGRESS_ANIMATION_DURATION);
    }

    @Override
    public void hideProgress() {
        setTabNavigationEnabled(mTabNavigationEnabled);

        mProgressMessageTextView.animate()
                .setStartDelay(0)
                .alpha(ALPHA_INVISIBLE)
                .translationY(mProgressMessageTranslationWhenHidden)
                .setDuration(PROGRESS_ANIMATION_DURATION);
        mTabsScrollingContainer.animate()
                .alpha(ALPHA_OPAQUE)
                .setStartDelay(PROGRESS_ANIMATION_DURATION)
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(PROGRESS_ANIMATION_DURATION);
    }

    private void setTabNavigationEnabled(boolean tabNavigationEnabled) {
        mStepperLayout.setTabNavigationEnabled(tabNavigationEnabled);
    }
}
