package com.github.leodan11.stepper.adapter;

import android.content.Context;
import android.content.ContextWrapper;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.github.leodan11.stepper.Step;
import com.github.leodan11.stepper.StepperLayout;
import com.github.leodan11.stepper.viewmodel.StepViewModel;

/**
 * A base adapter class which returns step fragments to use inside of the {@link StepperLayout}.
 */
public abstract class AbstractFragmentStepAdapter
        extends FragmentStateAdapter
        implements StepAdapter {

    @NonNull
    private final FragmentManager mFragmentManager;

    @NonNull
    protected final Context context;

    /**
     * @param context context used to obtain the default {@link StepViewModel}s. It must implement
     *                {@link LifecycleOwner} (as `FragmentActivity` does) or wrap one.
     * @param fm      fragment manager used to host the step fragments
     */
    public AbstractFragmentStepAdapter(@NonNull Context context, @NonNull FragmentManager fm) {
        super(fm, requireLifecycle(context));
        this.mFragmentManager = fm;
        this.context = context;
    }

    /**
     * @param fragmentActivity activity hosting the step fragments
     */
    public AbstractFragmentStepAdapter(@NonNull FragmentActivity fragmentActivity) {
        this(fragmentActivity, fragmentActivity.getSupportFragmentManager(), fragmentActivity.getLifecycle());
    }

    /**
     * @param fragment parent fragment hosting the step fragments in its child fragment manager
     */
    public AbstractFragmentStepAdapter(@NonNull Fragment fragment) {
        this(fragment.requireContext(), fragment.getChildFragmentManager(), fragment.getLifecycle());
    }

    private AbstractFragmentStepAdapter(@NonNull Context context, @NonNull FragmentManager fm,
                                        @NonNull Lifecycle lifecycle) {
        super(fm, lifecycle);
        this.mFragmentManager = fm;
        this.context = context;
    }

    @NonNull
    @Override
    public final Fragment createFragment(@IntRange(from = 0) int position) {
        return (Fragment) createStep(position);
    }

    /** {@inheritDoc} */
    @Nullable
    @Override
    public Step findStep(@IntRange(from = 0) int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }
        Fragment fragment = mFragmentManager.findFragmentByTag("f" + getItemId(position));
        return fragment instanceof Step ? (Step) fragment : null;
    }

    /** {@inheritDoc} */
    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        return new StepViewModel.Builder(context).create();
    }

    /** {@inheritDoc} */
    @Override
    public final int getItemCount() {
        return getCount();
    }

    @Override
    public boolean containsItem(long itemId) {
        final int itemCount = getItemCount();
        for (int position = 0; position < itemCount; position++) {
            if (getItemId(position) == itemId) {
                return true;
            }
        }
        return false;
    }

    /** {@inheritDoc} */
    @NonNull
    @Override
    public final RecyclerView.Adapter<?> getPagerAdapter() {
        return this;
    }

    @NonNull
    private static Lifecycle requireLifecycle(@NonNull Context context) {
        LifecycleOwner lifecycleOwner = findLifecycleOwner(context);
        if (lifecycleOwner == null) {
            throw new IllegalArgumentException("The provided Context does not implement LifecycleOwner. "
                    + "Use AbstractFragmentStepAdapter(FragmentActivity) or "
                    + "AbstractFragmentStepAdapter(Fragment) instead.");
        }
        return lifecycleOwner.getLifecycle();
    }

    @Nullable
    private static LifecycleOwner findLifecycleOwner(@NonNull Context context) {
        Context currentContext = context;
        while (currentContext != null) {
            if (currentContext instanceof LifecycleOwner) {
                return (LifecycleOwner) currentContext;
            }
            if (currentContext instanceof ContextWrapper) {
                currentContext = ((ContextWrapper) currentContext).getBaseContext();
            } else {
                return null;
            }
        }
        return null;
    }
}
