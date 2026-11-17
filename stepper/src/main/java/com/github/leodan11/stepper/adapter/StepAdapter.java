package com.github.leodan11.stepper.adapter;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.leodan11.stepper.Step;
import com.github.leodan11.stepper.StepperLayout;
import com.github.leodan11.stepper.viewmodel.StepViewModel;

/**
 * Interface to be used as model to {@link StepperLayout}.
 */
public interface StepAdapter {

    /**
     * Create each step of the {@link StepperLayout}.
     * @param position The position of the adapter item to be used inside the ViewPager2.
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
     * @return the RecyclerView adapter to be used in the ViewPager2.
     */
    @NonNull
    RecyclerView.Adapter<?> getPagerAdapter();
}
