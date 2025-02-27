package com.github.leodan11.stepper.viewmodel;

import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.github.leodan11.stepper.R;

/**
 * Contains view information about the step.
 */
public class StepViewModel {

    /**
     * Drawable resource ID to be used for back/next navigation button compound drawables when we do not want to show them.
     *
     * @see #mNextButtonEndDrawableResId
     * @see #mBackButtonStartDrawableResId
     */
    public static final int NULL_DRAWABLE = -1;

    private StepViewModel(@Nullable CharSequence title, @Nullable CharSequence subtitle,
                          @Nullable CharSequence endButtonLabel, @Nullable CharSequence backButtonLabel,
                          @DrawableRes int nextButtonEndDrawableResId, @DrawableRes int backButtonStartDrawableResId,
                          boolean endButtonVisible, boolean backButtonVisible) {
        mTitle = title;
        mSubtitle = subtitle;
        mEndButtonLabel = endButtonLabel;
        mBackButtonLabel = backButtonLabel;
        mNextButtonEndDrawableResId = nextButtonEndDrawableResId;
        mBackButtonStartDrawableResId = backButtonStartDrawableResId;
        mEndButtonVisible = endButtonVisible;
        mBackButtonVisible = backButtonVisible;
    }

    /**
     * The displayable name of the step.
     */
    @Nullable
    private final CharSequence mTitle;

    /**
     * The optional displayable subtitle of the step.
     */
    @Nullable
    private final CharSequence mSubtitle;

    /**
     * Allows to override the text on the Complete/Next button for this step.
     * To do so you need to return a non-null String of the label.
     * If you wish to change the text for selected steps only (and leave the default for the rest)
     * then return {@code null} for the default ones.
     * By default this is {@code null}.
     */
    @Nullable
    private final CharSequence mEndButtonLabel;

    /**
     * Allows to override the text on the Back button for this step.
     * To do so you need to return a non-null String of the label.
     * If you wish to change the text for selected steps only (and leave the default for the rest)
     * then return {@code null} for the default ones.
     * By default this is {@code null}.
     */
    @Nullable
    private final CharSequence mBackButtonLabel;

    /**
     * Drawable resource ID to be used for next button's end compound drawable.
     * {@link R.drawable#ms_ic_chevron_end} is the default.
     */
    @DrawableRes
    private final int mNextButtonEndDrawableResId;

    /**
     * Drawable resource ID to be used for back button's start compound drawable.
     * {@link R.drawable#ms_ic_chevron_start} is the default.
     */
    @DrawableRes
    private final int mBackButtonStartDrawableResId;

    /**
     * Flag indicating if the Next/Complete button should be shown on the bottom bar.
     * {@code true} by default.
     */
    private final boolean mEndButtonVisible;

    /**
     * Flag indicating if the Back button should be shown on the bottom bar.
     * {@code true} by default.
     */
    private final boolean mBackButtonVisible;

    @Nullable
    public CharSequence getTitle() {
        return mTitle;
    }

    @Nullable
    public CharSequence getSubtitle() {
        return mSubtitle;
    }

    @Nullable
    public CharSequence getEndButtonLabel() {
        return mEndButtonLabel;
    }

    @Nullable
    public CharSequence getBackButtonLabel() {
        return mBackButtonLabel;
    }

    @DrawableRes
    public int getNextButtonEndDrawableResId() {
        return mNextButtonEndDrawableResId;
    }

    @DrawableRes
    public int getBackButtonStartDrawableResId() {
        return mBackButtonStartDrawableResId;
    }

    public boolean isEndButtonVisible() {
        return mEndButtonVisible;
    }

    public boolean isBackButtonVisible() {
        return mBackButtonVisible;
    }

    public static class Builder {

        @Nullable
        private final Context mContext;

        @Nullable
        private CharSequence mTitle;

        @Nullable
        private CharSequence mSubtitle;

        @Nullable
        private CharSequence mEndButtonLabel;

        @Nullable
        private CharSequence mBackButtonLabel;

        @DrawableRes
        private int mNextButtonEndDrawableResId = R.drawable.ms_ic_chevron_end;

        @DrawableRes
        private int mBackButtonStartDrawableResId = R.drawable.ms_ic_chevron_start;

        private boolean mEndButtonVisible = true;

        private boolean mBackButtonVisible = true;

        /**
         * Creates a builder for the step info.
         */
        public Builder() {
            this.mContext = null;
        }

        /**
         * Creates a builder for the step info.
         *
         * @param context the parent context
         */
        public Builder(@NonNull Context context) {
            this.mContext = context;
        }

        /**
         * Set the title using the given resource id.
         * You must add the current <code>Context</code> when using this method.
         *
         * @param titleId string resource ID for the title
         * @return This Builder object to allow for chaining of calls to set methods
         * @see #Builder(Context)
         */
        public Builder setTitle(@StringRes int titleId) {
            if (mContext != null) {
                mTitle = mContext.getString(titleId);
            }
            return this;
        }

        /**
         * Set the title using the given characters.
         *
         * @param title CharSequence to be used as a title
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTitle(@NonNull CharSequence title) {
            mTitle = title;
            return this;
        }

        /**
         * Set the subtitle using the given resource id.
         * You must add the current <code>Context</code> when using this method.
         *
         * @param subtitleId string resource ID for the subtitle
         * @return This Builder object to allow for chaining of calls to set methods
         * @see #Builder(Context)
         */
        public Builder setSubtitle(@StringRes int subtitleId) {
            if (mContext != null) {
                mSubtitle = mContext.getString(subtitleId);
            }
            return this;
        }

        /**
         * Set the subtitle using the given characters.
         *
         * @param subtitle CharSequence to be used as a subtitle
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setSubtitle(@NonNull CharSequence subtitle) {
            mSubtitle = subtitle;
            return this;
        }

        /**
         * Set the label of the Complete/Next button.
         *
         * @param endButtonLabel CharSequence to be used as a Complete/Next button label
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setEndButtonLabel(@NonNull CharSequence endButtonLabel) {
            mEndButtonLabel = endButtonLabel;
            return this;
        }

        /**
         * Set the label of the Complete/Next button using the given resource id.
         * You must add the current <code>Context</code> when using this method.
         *
         * @param endButtonLabelId string resource ID for the Complete/Next button
         * @return This Builder object to allow for chaining of calls to set methods
         * @see #Builder(Context)
         */
        public Builder setEndButtonLabel(@StringRes int endButtonLabelId) {
            if (mContext != null) {
                mEndButtonLabel = mContext.getString(endButtonLabelId);
            }
            return this;
        }

        /**
         * Set the label of the back button using the given resource id.
         * You must add the current <code>Context</code> when using this method.
         *
         * @param backButtonLabelId string resource ID for the Back button
         * @return This Builder object to allow for chaining of calls to set methods
         * @see #Builder(Context)
         */
        public Builder setBackButtonLabel(@StringRes int backButtonLabelId) {
            if (mContext != null) {
                mBackButtonLabel = mContext.getString(backButtonLabelId);
            }
            return this;
        }

        /**
         * Set the label of the back button.
         *
         * @param backButtonLabel CharSequence to be used as a Back button label
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setBackButtonLabel(@NonNull CharSequence backButtonLabel) {
            mBackButtonLabel = backButtonLabel;
            return this;
        }

        /**
         * Set the drawable resource ID to be used for next button's end compound drawable.
         *
         * @param nextButtonEndDrawableResId drawable resource ID to be used for next button's end compound drawable.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNextButtonEndDrawableResId(@DrawableRes int nextButtonEndDrawableResId) {
            mNextButtonEndDrawableResId = nextButtonEndDrawableResId;
            return this;
        }

        /**
         * Set the drawable resource ID to be used for back button's start compound drawable.
         *
         * @param backButtonStartDrawableResId drawable resource ID to be used for back button's start compound drawable.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setBackButtonStartDrawableResId(@DrawableRes int backButtonStartDrawableResId) {
            mBackButtonStartDrawableResId = backButtonStartDrawableResId;
            return this;
        }

        /**
         * Set the flag indicating if the Next/Complete button should be shown on the bottom bar.
         *
         * @param endButtonVisible {@code true} if Next/Complete button should be shown, {@code false} otherwise
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setEndButtonVisible(boolean endButtonVisible) {
            mEndButtonVisible = endButtonVisible;
            return this;
        }

        /**
         * Set the flag indicating if the Back button should be shown on the bottom bar.
         *
         * @param backButtonVisible {@code true} if Back button should be shown, {@code false} otherwise
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setBackButtonVisible(boolean backButtonVisible) {
            mBackButtonVisible = backButtonVisible;
            return this;
        }

        /**
         * Creates a {@link StepViewModel} with the arguments supplied to this
         * builder.
         *
         * @return created StepViewModel
         */
        public StepViewModel create() {
            return new StepViewModel(mTitle, mSubtitle,
                    mEndButtonLabel, mBackButtonLabel,
                    mNextButtonEndDrawableResId, mBackButtonStartDrawableResId,
                    mEndButtonVisible, mBackButtonVisible);
        }

    }

}
