package com.github.leodan11.stepper.adapter;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.github.leodan11.stepper.Step;
import com.github.leodan11.stepper.StepperLayout;
import com.github.leodan11.stepper.viewmodel.StepViewModel;

/**
 * Interface to be used as model to {@link StepperLayout}.
 */
public interface StepAdapter {

    /**
     * Create each step of the {@link StepperLayout}.
     * @param position The position of the {@link PagerAdapter} to be used inside the {@link ViewPager}.
     * @return the step to be used inside the {@link StepperLayout}.
     */
    Step createStep(@IntRange(from = 0) int position);

    /**
     * Finds the given step without creating it.
     * @param position step position
     * @return step fragment
     */
    Step findStep(@IntRange(from = 0) int position);

    /**
     * Returns the view information to be used to display the step.
     * @param position step position
     * @return view information
     */
    @NonNull
    StepViewModel getViewModel(@IntRange(from = 0) int position);

    /**
     * Get the step count.
     * @return the quantity of steps
     */
    @IntRange(from = 0)
    int getCount();

    /**
     * Method for internal purpose. Should not be inherited.
     * @return the adapter to be used in the {@link ViewPager}.
     */
    PagerAdapter getPagerAdapter();
}
