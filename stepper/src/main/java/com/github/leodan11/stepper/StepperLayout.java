package com.github.leodan11.stepper;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.annotation.UiThread;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;

import com.github.leodan11.stepper.adapter.StepAdapter;
import com.github.leodan11.stepper.internal.feedback.StepperFeedbackType;
import com.github.leodan11.stepper.internal.feedback.StepperFeedbackTypeFactory;
import com.github.leodan11.stepper.internal.type.AbstractStepperType;
import com.github.leodan11.stepper.internal.type.StepperTypeFactory;
import com.github.leodan11.stepper.internal.util.AnimationUtil;
import com.github.leodan11.stepper.internal.util.TintUtil;
import com.github.leodan11.stepper.internal.widget.ColorableProgressBar;
import com.github.leodan11.stepper.internal.widget.DottedProgressBar;
import com.github.leodan11.stepper.internal.widget.RightNavigationButton;
import com.github.leodan11.stepper.internal.widget.StepViewPager;
import com.github.leodan11.stepper.internal.widget.TabsContainer;
import com.github.leodan11.stepper.internal.widget.pagetransformer.StepPageTransformerFactory;
import com.github.leodan11.stepper.viewmodel.StepViewModel;

/**
 * Stepper widget implemented according to the <a href="https://www.google.com/design/spec/components/steppers.html">Material documentation</a>.<br>
 * It allows for setting three types of steppers:<br>
 * - mobile dots stepper,<br>
 * - mobile progress bar stepper,<br>
 * - horizontal stepper with tabs.<br>
 * Include this stepper in the layout XML file and choose a stepper type with <code>ms_stepperType</code>.<br>
 * Check out <code>values/attrs.xml - StepperLayout</code> for a complete list of customisable properties.
 */
public class StepperLayout extends LinearLayout implements TabsContainer.TabItemListener {

    public static final int DEFAULT_TAB_DIVIDER_WIDTH = -1;

    /**
     * A listener for events of {@link StepperLayout}.
     */
    public interface StepperListener {

        /**
         * Called when all of the steps were completed successfully
         *
         * @param completeButton the complete button that was clicked to complete the flow
         */
        void onCompleted(View completeButton);

        /**
         * Called when a verification error occurs for one of the steps
         *
         * @param verificationError verification error
         */
        void onError(VerificationError verificationError);

        /**
         * Called when the current step position changes
         *
         * @param newStepPosition new step position
         */
        void onStepSelected(int newStepPosition);

        /**
         * Called when the Previous step button was pressed while on the first step
         * (the button is not present by default on first step).
         */
        void onReturn();

        StepperListener NULL = new StepperListener() {
            @Override
            public void onCompleted(View completeButton) {
            }

            @Override
            public void onError(VerificationError verificationError) {
            }

            @Override
            public void onStepSelected(int newStepPosition) {
            }

            @Override
            public void onReturn() {
            }
        };
    }

    public abstract class AbstractOnButtonClickedCallback {

        public StepperLayout getStepperLayout() {
            return StepperLayout.this;
        }

    }

    public class OnNextClickedCallback extends AbstractOnButtonClickedCallback {

        @UiThread
        public void goToNextStep() {
            final int totalStepCount = mStepAdapter.getCount();

            if (mCurrentStepPosition >= totalStepCount - 1) {
                return;
            }

            mCurrentStepPosition++;
            onUpdate(mCurrentStepPosition, true);
        }

    }

    public class OnCompleteClickedCallback extends AbstractOnButtonClickedCallback {

        @UiThread
        public void complete() {
            invalidateCurrentPosition();
            mListener.onCompleted(mCompleteNavigationButton);
        }

    }

    public class OnBackClickedCallback extends AbstractOnButtonClickedCallback {

        @UiThread
        public void goToPrevStep() {
            if (mCurrentStepPosition <= 0) {
                if (mShowBackButtonOnFirstStep) {
                    mListener.onReturn();
                }
                return;
            }
            mCurrentStepPosition--;
            onUpdate(mCurrentStepPosition, true);
        }

    }

    private class OnBackClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            onBackClicked();
        }
    }

    private class OnNextClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            onNext();
        }
    }

    private class OnCompleteClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            onComplete();
        }
    }

    private ViewPager mPager;

    private Button mBackNavigationButton;

    private RightNavigationButton mNextNavigationButton;

    private RightNavigationButton mCompleteNavigationButton;

    private ViewGroup mStepNavigation;

    private DottedProgressBar mDottedProgressBar;

    private ColorableProgressBar mProgressBar;

    private TabsContainer mTabsContainer;

    private ColorStateList mBackButtonColor;

    private ColorStateList mNextButtonColor;

    private ColorStateList mCompleteButtonColor;

    @ColorInt
    private int mUnselectedColor;

    @ColorInt
    private int mSelectedColor;

    @ColorInt
    private int mErrorColor;

    @DrawableRes
    private int mBottomNavigationBackground;

    @DrawableRes
    private int mBackButtonBackground;

    @DrawableRes
    private int mNextButtonBackground;

    @DrawableRes
    private int mCompleteButtonBackground;

    private int mTabStepDividerWidth = DEFAULT_TAB_DIVIDER_WIDTH;

    private String mBackButtonText;

    private String mNextButtonText;

    private String mCompleteButtonText;

    private boolean mShowBackButtonOnFirstStep;

    private boolean mShowBottomNavigation;

    private int mTypeIdentifier = AbstractStepperType.PROGRESS_BAR;

    private int mFeedbackTypeMask = StepperFeedbackType.NONE;

    private StepAdapter mStepAdapter;

    private AbstractStepperType mStepperType;

    private StepperFeedbackType mStepperFeedbackType;

    @FloatRange(from = 0.0f, to = 1.0f)
    private float mContentFadeAlpha = AnimationUtil.ALPHA_HALF;

    @DrawableRes
    private int mContentOverlayBackground;

    private int mCurrentStepPosition;

    private boolean mShowErrorStateEnabled;

    private boolean mShowErrorStateOnBackEnabled;

    private boolean mShowErrorMessageEnabled;

    private boolean mTabNavigationEnabled;

    private boolean mInProgress;

    @StyleRes
    private int mStepperLayoutTheme;

    @NonNull
    private StepperListener mListener = StepperListener.NULL;

    public StepperLayout(Context context) {
        this(context, null);
    }

    public StepperLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //Fix for issue #60 with AS Preview editor
        init(attrs, isInEditMode() ? 0 : R.attr.ms_stepperStyle);
    }

    public StepperLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    @Override
    public final void setOrientation(@LinearLayoutCompat.OrientationMode int orientation) {
        //only vertical orientation is supported
        super.setOrientation(VERTICAL);
    }

    public void setListener(@NonNull StepperListener listener) {
        this.mListener = listener;
    }

    /**
     * Getter for mStepAdapter
     *
     * @return mStepAdapter
     */
    public StepAdapter getAdapter() {
        return mStepAdapter;
    }

    /**
     * Sets the new step adapter and updates the stepper layout based on the new adapter.
     *
     * @param stepAdapter step adapter
     */
    public void setAdapter(@NonNull StepAdapter stepAdapter) {
        this.mStepAdapter = stepAdapter;

        mPager.setAdapter(stepAdapter.getPagerAdapter());

        mStepperType.onNewAdapter(stepAdapter);

        // this is so that the fragments in the adapter can be created BEFORE the onUpdate() method call
        mPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //noinspection deprecation
                mPager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                onUpdate(mCurrentStepPosition, false);
            }
        });
    }

    /**
     * Sets the new step adapter and updates the stepper layout based on the new adapter.
     *
     * @param stepAdapter         step adapter
     * @param currentStepPosition the initial step position, must be in the range of the adapter item count
     */
    public void setAdapter(@NonNull StepAdapter stepAdapter, @IntRange(from = 0) int currentStepPosition) {
        this.mCurrentStepPosition = currentStepPosition;
        setAdapter(stepAdapter);
    }

    /**
     * Overrides the default page transformer used in the underlying {@link StepViewPager}.
     * If you're supporting RTL make sure your {@link ViewPager.PageTransformer} accounts for it.
     *
     * @param pageTransformer new page transformer
     * @see StepViewPager
     * @see StepPageTransformerFactory
     */
    public void setPageTransformer(@Nullable ViewPager.PageTransformer pageTransformer) {
        mPager.setPageTransformer(false, pageTransformer);
    }

    public int getSelectedColor() {
        return mSelectedColor;
    }

    public int getUnselectedColor() {
        return mUnselectedColor;
    }

    public int getErrorColor() {
        return mErrorColor;
    }

    public int getTabStepDividerWidth() {
        return mTabStepDividerWidth;
    }

    @Override
    @UiThread
    public void onTabClicked(int position) {
        if (mTabNavigationEnabled) {
            if (position > mCurrentStepPosition) {
                onNext();
            } else if (position < mCurrentStepPosition) {
                setCurrentStepPosition(position);
            }
        }
    }

    /**
     * This is an equivalent of clicking the Next/Complete button from the bottom navigation.
     * Unlike {@link #setCurrentStepPosition(int)} this actually verifies the step.
     */
    public void proceed() {
        if (isLastPosition(mCurrentStepPosition)) {
            onComplete();
        } else {
            onNext();
        }
    }

    /**
     * To be called when the user wants to go to the previous step.
     */
    public void onBackClicked() {
        Step step = findCurrentStep();

        updateErrorFlagWhenGoingBack();

        OnBackClickedCallback onBackClickedCallback = new OnBackClickedCallback();
        if (step instanceof BlockingStep) {
            ((BlockingStep) step).onBackClicked(onBackClickedCallback);
        } else {
            onBackClickedCallback.goToPrevStep();
        }
    }

    /**
     * Sets the current step to the one at the provided index.
     * This does not verify the current step.
     *
     * @param currentStepPosition new current step position
     */
    public void setCurrentStepPosition(int currentStepPosition) {
        int previousStepPosition = mCurrentStepPosition;
        if (currentStepPosition < previousStepPosition) {
            updateErrorFlagWhenGoingBack();
        }
        mCurrentStepPosition = currentStepPosition;

        onUpdate(currentStepPosition, true);
    }

    /**
     * Returns the position of the currently selected step.
     *
     * @return position of the currently selected step
     */
    public int getCurrentStepPosition() {
        return mCurrentStepPosition;
    }

    /**
     * Sets whether the Next button in the bottom navigation bar should be in the
     * 'verification failed' state i.e. still clickable but with an option to display it
     * differently to indicate to the user that he cannot go to the next step yet.
     *
     * @param verificationFailed false if verification failed, true otherwise
     */
    public void setNextButtonVerificationFailed(boolean verificationFailed) {
        mNextNavigationButton.setVerificationFailed(verificationFailed);
    }

    /**
     * Sets whether the Complete button in the bottom navigation bar should be in the
     * 'verification failed' state i.e. still clickable but with an option to display it
     * differently to indicate to the user that he cannot finish the process yet.
     *
     * @param verificationFailed false if verification failed, true otherwise
     */
    public void setCompleteButtonVerificationFailed(boolean verificationFailed) {
        mCompleteNavigationButton.setVerificationFailed(verificationFailed);
    }

    /**
     * Sets whether the Next button in the bottom navigation bar should be enabled (clickable).
     * setting this to <i>false</i> will make it unclickable.
     *
     * @param enabled true if the button should be clickable, false otherwise
     */
    public void setNextButtonEnabled(boolean enabled) {
        mNextNavigationButton.setEnabled(enabled);
    }

    /**
     * Sets whether the Complete button in the bottom navigation bar should be enabled (clickable).
     * setting this to <i>false</i> will make it unclickable.
     *
     * @param enabled true if the button should be clickable, false otherwise
     */
    public void setCompleteButtonEnabled(boolean enabled) {
        mCompleteNavigationButton.setEnabled(enabled);
    }

    /**
     * Sets whether the Back button in the bottom navigation bar should be enabled (clickable).
     * setting this to <i>false</i> will make it unclickable.
     *
     * @param enabled true if the button should be clickable, false otherwise
     */
    public void setBackButtonEnabled(boolean enabled) {
        mBackNavigationButton.setEnabled(enabled);
    }

    /**
     * Set whether the bottom navigation bar (with Back/Next/Complete buttons) should be visible.
     * <i>true</i> by default.
     *
     * @param showBottomNavigation true if bottom navigation should be visible, false otherwise
     */
    public void setShowBottomNavigation(boolean showBottomNavigation) {
        mStepNavigation.setVisibility(showBottomNavigation ? View.VISIBLE : View.GONE);
    }

    /**
     * Set whether when going backwards should clear the error state from the Tab. Default is <code>false</code>.
     *
     * @param showErrorStateOnBack true if navigating backwards should keep the error state, false otherwise
     * @deprecated see {@link #setShowErrorStateOnBackEnabled(boolean)}
     */
    @Deprecated
    public void setShowErrorStateOnBack(boolean showErrorStateOnBack) {
        this.mShowErrorStateOnBackEnabled = showErrorStateOnBack;
    }

    /**
     * Set whether when going backwards should clear the error state from the Tab. Default is <code>false</code>.
     *
     * @param showErrorStateOnBackEnabled true if navigating backwards should keep the error state, false otherwise
     */
    public void setShowErrorStateOnBackEnabled(boolean showErrorStateOnBackEnabled) {
        this.mShowErrorStateOnBackEnabled = showErrorStateOnBackEnabled;
    }

    /**
     * Set whether the errors should be displayed when they occur or not. Default is <code>false</code>.
     *
     * @param showErrorState true if the errors should be displayed when they occur, false otherwise
     * @deprecated see {@link #setShowErrorStateEnabled(boolean)}
     */
    @Deprecated
    public void setShowErrorState(boolean showErrorState) {
        setShowErrorStateEnabled(showErrorState);
    }

    /**
     * Set whether the errors should be displayed when they occur or not. Default is <code>false</code>.
     *
     * @param showErrorStateEnabled true if the errors should be displayed when they occur, false otherwise
     */
    public void setShowErrorStateEnabled(boolean showErrorStateEnabled) {
        this.mShowErrorStateEnabled = showErrorStateEnabled;
    }

    /**
     * @return true if errors should be displayed when they occur
     */
    public boolean isShowErrorStateEnabled() {
        return mShowErrorStateEnabled;
    }

    /**
     * @return true if when going backwards the error state from the Tab should be cleared
     */
    public boolean isShowErrorStateOnBackEnabled() {
        return mShowErrorStateOnBackEnabled;
    }

    /**
     * Set whether an error message below step title should appear when an error occurs
     *
     * @param showErrorMessageEnabled true if an error message below step title should appear when an error occurs
     */
    public void setShowErrorMessageEnabled(boolean showErrorMessageEnabled) {
        this.mShowErrorMessageEnabled = showErrorMessageEnabled;
    }

    /**
     * @return true if an error message below step title should appear when an error occurs
     */
    public boolean isShowErrorMessageEnabled() {
        return mShowErrorMessageEnabled;
    }

    /**
     * @return true if step navigation is possible by clicking on the tabs directly, false otherwise
     */
    public boolean isTabNavigationEnabled() {
        return mTabNavigationEnabled;
    }

    /**
     * Sets whether step navigation is possible by clicking on the tabs directly. Only applicable for 'tabs' type.
     *
     * @param tabNavigationEnabled true if step navigation is possible by clicking on the tabs directly, false otherwise
     */
    public void setTabNavigationEnabled(boolean tabNavigationEnabled) {
        mTabNavigationEnabled = tabNavigationEnabled;
    }

    /**
     * Changes the text and compound drawable color of the Next bottom navigation button.
     *
     * @param newButtonColor new color state list
     */
    public void setNextButtonColor(@NonNull ColorStateList newButtonColor) {
        mNextButtonColor = newButtonColor;
        TintUtil.tintTextView(mNextNavigationButton, mNextButtonColor);
    }

    /**
     * Changes the text and compound drawable color of the Complete bottom navigation button.
     *
     * @param newButtonColor new color state list
     */
    public void setCompleteButtonColor(@NonNull ColorStateList newButtonColor) {
        mCompleteButtonColor = newButtonColor;
        TintUtil.tintTextView(mCompleteNavigationButton, mCompleteButtonColor);
    }

    /**
     * Changes the text and compound drawable color of the Back bottom navigation button.
     *
     * @param newButtonColor new color state list
     */
    public void setBackButtonColor(@NonNull ColorStateList newButtonColor) {
        mBackButtonColor = newButtonColor;
        TintUtil.tintTextView(mBackNavigationButton, mBackButtonColor);
    }

    /**
     * Changes the text and compound drawable color of the Next bottom navigation button.
     *
     * @param newButtonColor new color int
     */
    public void setNextButtonColor(@ColorInt int newButtonColor) {
        setNextButtonColor(ColorStateList.valueOf(newButtonColor));
    }

    /**
     * Changes the text and compound drawable color of the Complete bottom navigation button.
     *
     * @param newButtonColor new color int
     */
    public void setCompleteButtonColor(@ColorInt int newButtonColor) {
        setCompleteButtonColor(ColorStateList.valueOf(newButtonColor));
    }

    /**
     * Changes the text and compound drawable color of the Back bottom navigation button.
     *
     * @param newButtonColor new color int
     */
    public void setBackButtonColor(@ColorInt int newButtonColor) {
        setBackButtonColor(ColorStateList.valueOf(newButtonColor));
    }

    /**
     * Updates the error state in the UI.
     * It does nothing if showing error state is disabled.
     * This is used internally to show the error on tabs.
     *
     * @param error not null if error should be shown, null otherwise
     * @see #setShowErrorStateEnabled(boolean)
     */
    public void updateErrorState(@Nullable VerificationError error) {
        updateError(error);
        if (mShowErrorStateEnabled) {
            invalidateCurrentPosition();
        }
    }

    /**
     * Set the number of steps that should be retained to either side of the
     * current step in the view hierarchy in an idle state. Steps beyond this
     * limit will be recreated from the adapter when needed.
     *
     * @param limit How many steps will be kept offscreen in an idle state.
     * @see ViewPager#setOffscreenPageLimit(int)
     */
    public void setOffscreenPageLimit(int limit) {
        mPager.setOffscreenPageLimit(limit);
    }

    /**
     * Shows a progress indicator if not already shown. This does not have to be a progress bar and it depends on chosen stepper feedback types.
     *
     * @param progressMessage optional progress message if supported by the selected types
     */
    public void showProgress(@NonNull String progressMessage) {
        if (!mInProgress) {
            mStepperFeedbackType.showProgress(progressMessage);
            mInProgress = true;
        }
    }

    /**
     * Hides the progress indicator if visible.
     */
    public void hideProgress() {
        if (mInProgress) {
            mInProgress = false;
            mStepperFeedbackType.hideProgress();
        }
    }

    /**
     * Checks if there's an ongoing operation i.e. if {@link #showProgress(String)} was called and not followed by {@link #hideProgress()} yet.
     *
     * @return true if in progress, false otherwise
     */
    public boolean isInProgress() {
        return mInProgress;
    }

    /**
     * Sets the mask for the stepper feedback type.
     *
     * @param feedbackTypeMask step feedback type mask, should contain one or more flags from {@link StepperFeedbackType}
     */
    public void setFeedbackType(int feedbackTypeMask) {
        mFeedbackTypeMask = feedbackTypeMask;
        mStepperFeedbackType = StepperFeedbackTypeFactory.createType(mFeedbackTypeMask, this);
    }

    /**
     * @return An alpha value from 0 to 1.0f to be used for the faded out view if 'content_fade' stepper feedback is set. 0.5f by default.
     */
    @FloatRange(from = 0.0f, to = 1.0f)
    public float getContentFadeAlpha() {
        return mContentFadeAlpha;
    }

    /**
     * @return Background res ID to be used for the overlay on top of the content
     * if 'content_overlay' stepper feedback type is set. 0 if default background should be used.
     */
    @DrawableRes
    public int getContentOverlayBackground() {
        return mContentOverlayBackground;
    }

    @SuppressWarnings("RestrictedApi")
    private void init(AttributeSet attrs, @AttrRes int defStyleAttr) {
        initDefaultValues();
        extractValuesFromAttributes(attrs, defStyleAttr);

        final Context context = getContext();

        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, context.getTheme());
        contextThemeWrapper.setTheme(mStepperLayoutTheme);

        LayoutInflater.from(contextThemeWrapper).inflate(R.layout.ms_stepper_layout, this, true);

        setOrientation(VERTICAL);

        bindViews();

        initNavigation();

        mDottedProgressBar.setVisibility(GONE);
        mProgressBar.setVisibility(GONE);
        mTabsContainer.setVisibility(GONE);
        mStepNavigation.setVisibility(mShowBottomNavigation ? View.VISIBLE : View.GONE);

        mStepperType = StepperTypeFactory.createType(mTypeIdentifier, this);
        mStepperFeedbackType = StepperFeedbackTypeFactory.createType(mFeedbackTypeMask, this);
    }

    private void initNavigation() {
        if (mBottomNavigationBackground != 0) {
            mStepNavigation.setBackgroundResource(mBottomNavigationBackground);
        }

        mBackNavigationButton.setText(mBackButtonText);
        mNextNavigationButton.setText(mNextButtonText);
        mCompleteNavigationButton.setText(mCompleteButtonText);

        setBackgroundIfPresent(mBackButtonBackground, mBackNavigationButton);
        setBackgroundIfPresent(mNextButtonBackground, mNextNavigationButton);
        setBackgroundIfPresent(mCompleteButtonBackground, mCompleteNavigationButton);

        mBackNavigationButton.setOnClickListener(new OnBackClickListener());
        mNextNavigationButton.setOnClickListener(new OnNextClickListener());
        mCompleteNavigationButton.setOnClickListener(new OnCompleteClickListener());
    }

    private void setCompoundDrawablesForNavigationButtons(@DrawableRes int backDrawableResId, @DrawableRes int nextDrawableResId) {
        Drawable chevronStartDrawable = backDrawableResId != StepViewModel.NULL_DRAWABLE
                ? ResourcesCompat.getDrawable(getContext().getResources(), backDrawableResId, null)
                : null;
        Drawable chevronEndDrawable = nextDrawableResId != StepViewModel.NULL_DRAWABLE
                ? ResourcesCompat.getDrawable(getContext().getResources(), nextDrawableResId, null)
                : null;
        mBackNavigationButton.setCompoundDrawablesRelativeWithIntrinsicBounds(chevronStartDrawable, null, null, null);
        mNextNavigationButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, chevronEndDrawable, null);

        TintUtil.tintTextView(mBackNavigationButton, mBackButtonColor);
        TintUtil.tintTextView(mNextNavigationButton, mNextButtonColor);
        TintUtil.tintTextView(mCompleteNavigationButton, mCompleteButtonColor);
    }

    private void setBackgroundIfPresent(@DrawableRes int backgroundRes, View view) {
        if (backgroundRes != 0) {
            view.setBackgroundResource(backgroundRes);
        }
    }

    private void bindViews() {
        mPager = findViewById(R.id.ms_stepPager);

        mBackNavigationButton = findViewById(R.id.ms_stepPrevButton);
        mNextNavigationButton = findViewById(R.id.ms_stepNextButton);
        mCompleteNavigationButton = findViewById(R.id.ms_stepCompleteButton);

        mStepNavigation = findViewById(R.id.ms_bottomNavigation);

        mDottedProgressBar = findViewById(R.id.ms_stepDottedProgressBar);

        mProgressBar = findViewById(R.id.ms_stepProgressBar);

        mTabsContainer = findViewById(R.id.ms_stepTabsContainer);
    }

    private void extractValuesFromAttributes(AttributeSet attrs, @AttrRes int defStyleAttr) {
        try (TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StepperLayout, defStyleAttr, 0)) {

            if (a.hasValue(R.styleable.StepperLayout_ms_backButtonColor)) {
                mBackButtonColor = a.getColorStateList(R.styleable.StepperLayout_ms_backButtonColor);
            }
            if (a.hasValue(R.styleable.StepperLayout_ms_nextButtonColor)) {
                mNextButtonColor = a.getColorStateList(R.styleable.StepperLayout_ms_nextButtonColor);
            }
            if (a.hasValue(R.styleable.StepperLayout_ms_completeButtonColor)) {
                mCompleteButtonColor = a.getColorStateList(R.styleable.StepperLayout_ms_completeButtonColor);
            }
            if (a.hasValue(R.styleable.StepperLayout_ms_activeStepColor)) {
                mSelectedColor = a.getColor(R.styleable.StepperLayout_ms_activeStepColor, mSelectedColor);
            }
            if (a.hasValue(R.styleable.StepperLayout_ms_inactiveStepColor)) {
                mUnselectedColor = a.getColor(R.styleable.StepperLayout_ms_inactiveStepColor, mUnselectedColor);
            }
            if (a.hasValue(R.styleable.StepperLayout_ms_errorColor)) {
                mErrorColor = a.getColor(R.styleable.StepperLayout_ms_errorColor, mErrorColor);
            }
            if (a.hasValue(R.styleable.StepperLayout_ms_bottomNavigationBackground)) {
                mBottomNavigationBackground = a.getResourceId(R.styleable.StepperLayout_ms_bottomNavigationBackground, 0);
            }

            if (a.hasValue(R.styleable.StepperLayout_ms_backButtonBackground)) {
                mBackButtonBackground = a.getResourceId(R.styleable.StepperLayout_ms_backButtonBackground, 0);
            }
            if (a.hasValue(R.styleable.StepperLayout_ms_nextButtonBackground)) {
                mNextButtonBackground = a.getResourceId(R.styleable.StepperLayout_ms_nextButtonBackground, 0);
            }
            if (a.hasValue(R.styleable.StepperLayout_ms_completeButtonBackground)) {
                mCompleteButtonBackground = a.getResourceId(R.styleable.StepperLayout_ms_completeButtonBackground, 0);
            }

            if (a.hasValue(R.styleable.StepperLayout_ms_backButtonText)) {
                mBackButtonText = a.getString(R.styleable.StepperLayout_ms_backButtonText);
            }
            if (a.hasValue(R.styleable.StepperLayout_ms_nextButtonText)) {
                mNextButtonText = a.getString(R.styleable.StepperLayout_ms_nextButtonText);
            }
            if (a.hasValue(R.styleable.StepperLayout_ms_completeButtonText)) {
                mCompleteButtonText = a.getString(R.styleable.StepperLayout_ms_completeButtonText);
            }

            if (a.hasValue(R.styleable.StepperLayout_ms_tabStepDividerWidth)) {
                mTabStepDividerWidth = a.getDimensionPixelOffset(R.styleable.StepperLayout_ms_tabStepDividerWidth, -1);
            }

            mShowBackButtonOnFirstStep = a.getBoolean(R.styleable.StepperLayout_ms_showBackButtonOnFirstStep, false);

            mShowBottomNavigation = a.getBoolean(R.styleable.StepperLayout_ms_showBottomNavigation, true);

            mShowErrorStateEnabled = a.getBoolean(R.styleable.StepperLayout_ms_showErrorState, false);
            mShowErrorStateEnabled = a.getBoolean(R.styleable.StepperLayout_ms_showErrorStateEnabled, mShowErrorStateEnabled);

            if (a.hasValue(R.styleable.StepperLayout_ms_stepperType)) {
                mTypeIdentifier = a.getInt(R.styleable.StepperLayout_ms_stepperType, AbstractStepperType.PROGRESS_BAR);
            }

            if (a.hasValue(R.styleable.StepperLayout_ms_stepperFeedbackType)) {
                mFeedbackTypeMask = a.getInt(R.styleable.StepperLayout_ms_stepperFeedbackType, StepperFeedbackType.NONE);
            }

            if (a.hasValue(R.styleable.StepperLayout_ms_stepperFeedback_contentFadeAlpha)) {
                mContentFadeAlpha = a.getFloat(R.styleable.StepperLayout_ms_stepperFeedback_contentFadeAlpha, AnimationUtil.ALPHA_HALF);
            }

            if (a.hasValue(R.styleable.StepperLayout_ms_stepperFeedback_contentOverlayBackground)) {
                mContentOverlayBackground = a.getResourceId(R.styleable.StepperLayout_ms_stepperFeedback_contentOverlayBackground, 0);
            }

            mShowErrorStateOnBackEnabled = a.getBoolean(R.styleable.StepperLayout_ms_showErrorStateOnBack, false);
            mShowErrorStateOnBackEnabled = a.getBoolean(R.styleable.StepperLayout_ms_showErrorStateOnBackEnabled, mShowErrorStateOnBackEnabled);

            mShowErrorMessageEnabled = a.getBoolean(R.styleable.StepperLayout_ms_showErrorMessageEnabled, false);

            mTabNavigationEnabled = a.getBoolean(R.styleable.StepperLayout_ms_tabNavigationEnabled, true);

            mStepperLayoutTheme = a.getResourceId(R.styleable.StepperLayout_ms_stepperLayoutTheme, R.style.MSDefaultStepperLayoutTheme);

        } catch (Exception e) {
            Log.e(StepperLayout.class.getSimpleName(), "Error while parsing attributes", e);
        }
    }

    private void initDefaultValues() {
        mBackButtonColor = mNextButtonColor = mCompleteButtonColor =
                ContextCompat.getColorStateList(getContext(), R.color.ms_bottomNavigationButtonTextColor);
        mSelectedColor = ContextCompat.getColor(getContext(), R.color.ms_selectedColor);
        mUnselectedColor = ContextCompat.getColor(getContext(), R.color.ms_unselectedColor);
        mErrorColor = ContextCompat.getColor(getContext(), R.color.ms_errorColor);
        mBackButtonText = getContext().getString(R.string.ms_back);
        mNextButtonText = getContext().getString(R.string.ms_next);
        mCompleteButtonText = getContext().getString(R.string.ms_complete);
    }

    private boolean isLastPosition(int position) {
        return position == mStepAdapter.getCount() - 1;
    }

    private Step findCurrentStep() {
        return mStepAdapter.findStep(mCurrentStepPosition);
    }

    private void updateErrorFlagWhenGoingBack() {
        updateError(mShowErrorStateOnBackEnabled ? mStepperType.getErrorAtPosition(mCurrentStepPosition) : null);
    }

    @UiThread
    private void onNext() {
        Step step = findCurrentStep();

        if (verifyCurrentStep(step)) {
            invalidateCurrentPosition();
            return;
        }

        OnNextClickedCallback onNextClickedCallback = new OnNextClickedCallback();
        if (step instanceof BlockingStep) {
            ((BlockingStep) step).onNextClicked(onNextClickedCallback);
        } else {
            onNextClickedCallback.goToNextStep();
        }
    }

    private void invalidateCurrentPosition() {
        mStepperType.onStepSelected(mCurrentStepPosition, false);
    }

    private boolean verifyCurrentStep(Step step) {
        final VerificationError verificationError = step.verifyStep();
        boolean result = false;
        if (verificationError != null) {
            onError(verificationError);
            result = true;
        }

        updateError(verificationError);
        return result;
    }

    private void updateError(@Nullable VerificationError error) {
        mStepperType.setError(mCurrentStepPosition, error);
    }

    private void onError(@NonNull VerificationError verificationError) {
        Step step = findCurrentStep();
        if (step != null) {
            step.onError(verificationError);
        }
        mListener.onError(verificationError);
    }

    private void onComplete() {
        Step step = findCurrentStep();
        if (verifyCurrentStep(step)) {
            invalidateCurrentPosition();
            return;
        }

        OnCompleteClickedCallback onCompleteClickedCallback = new OnCompleteClickedCallback();
        if (step instanceof BlockingStep) {
            ((BlockingStep) step).onCompleteClicked(onCompleteClickedCallback);
        } else {
            onCompleteClickedCallback.complete();
        }
    }

    private void onUpdate(int newStepPosition, boolean userTriggeredChange) {
        mPager.setCurrentItem(newStepPosition);
        final boolean isLast = isLastPosition(newStepPosition);
        final boolean isFirst = newStepPosition == 0;
        final StepViewModel viewModel = mStepAdapter.getViewModel(newStepPosition);

        int backButtonTargetVisibility = (isFirst && !mShowBackButtonOnFirstStep) || !viewModel.isBackButtonVisible() ? View.GONE : View.VISIBLE;
        int nextButtonVisibility = isLast || !viewModel.isEndButtonVisible() ? View.GONE : View.VISIBLE;
        int completeButtonVisibility = !isLast || !viewModel.isEndButtonVisible() ? View.GONE : View.VISIBLE;

        AnimationUtil.fadeViewVisibility(mNextNavigationButton, nextButtonVisibility, userTriggeredChange);
        AnimationUtil.fadeViewVisibility(mCompleteNavigationButton, completeButtonVisibility, userTriggeredChange);
        AnimationUtil.fadeViewVisibility(mBackNavigationButton, backButtonTargetVisibility, userTriggeredChange);

        updateBackButton(viewModel);

        updateEndButton(viewModel.getEndButtonLabel(),
                isLast ? mCompleteButtonText : mNextButtonText,
                isLast ? mCompleteNavigationButton : mNextNavigationButton);

        setCompoundDrawablesForNavigationButtons(viewModel.getBackButtonStartDrawableResId(), viewModel.getNextButtonEndDrawableResId());

        mStepperType.onStepSelected(newStepPosition, userTriggeredChange);
        mListener.onStepSelected(newStepPosition);
        Step step = mStepAdapter.findStep(newStepPosition);
        if (step != null) {
            step.onSelected();
        }
    }

    private void updateEndButton(@Nullable CharSequence endButtonTextForStep,
                                 @Nullable CharSequence defaultEndButtonText,
                                 @NonNull TextView endButton) {
        if (endButtonTextForStep == null) {
            endButton.setText(defaultEndButtonText);
        } else {
            endButton.setText(endButtonTextForStep);
        }
    }

    private void updateBackButton(@NonNull StepViewModel viewModel) {
        CharSequence backButtonTextForStep = viewModel.getBackButtonLabel();
        if (backButtonTextForStep == null) {
            mBackNavigationButton.setText(mBackButtonText);
        } else {
            mBackNavigationButton.setText(backButtonTextForStep);
        }
    }
}
