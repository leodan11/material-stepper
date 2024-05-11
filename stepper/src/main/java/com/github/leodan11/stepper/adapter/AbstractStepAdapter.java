package com.github.leodan11.stepper.adapter;

import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.github.leodan11.stepper.StepperLayout;
import com.github.leodan11.stepper.viewmodel.StepViewModel;

/**
 * A base adapter class which returns step to use inside of the {@link StepperLayout}.
 * This class is intended to be inherited if you need to use {@link StepperLayout} without fragments.
 * Otherwise, you should use {@link AbstractFragmentStepAdapter}
 */
public abstract class AbstractStepAdapter
        extends PagerAdapter
        implements StepAdapter {

    @NonNull
    protected final Context context;

    public AbstractStepAdapter(@NonNull Context context) {
        this.context = context;
    }

    /** {@inheritDoc} */
    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        return new StepViewModel.Builder(context).create();
    }

    /** {@inheritDoc} */
    @Override
    public final PagerAdapter getPagerAdapter() {
        return this;
    }
}
