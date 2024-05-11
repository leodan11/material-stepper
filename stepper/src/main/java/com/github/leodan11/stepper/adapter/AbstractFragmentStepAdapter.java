package com.github.leodan11.stepper.adapter;

import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.github.leodan11.stepper.R;
import com.github.leodan11.stepper.Step;
import com.github.leodan11.stepper.StepperLayout;
import com.github.leodan11.stepper.viewmodel.StepViewModel;

/**
 * A base adapter class which returns step fragments to use inside of the {@link StepperLayout}.
 */

@SuppressWarnings("deprecation")
public abstract class AbstractFragmentStepAdapter
        extends FragmentPagerAdapter
        implements StepAdapter {

    @NonNull
    private final FragmentManager mFragmentManager;

    @NonNull
    protected final Context context;

    public AbstractFragmentStepAdapter(@NonNull Context context, @NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mFragmentManager = fm;
        this.context = context;
    }

    @NonNull
    @Override
    public final Fragment getItem(@IntRange(from = 0) int position) {
        return (Fragment) createStep(position);
    }

    /** {@inheritDoc} */
    public Step findStep(@IntRange(from = 0) int position) {
        String fragmentTag =  "android:switcher:" + R.id.ms_stepPager + ":" + this.getItemId(position);
        return (Step) mFragmentManager.findFragmentByTag(fragmentTag);
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
