package com.github.leodan11.stepper.internal.type;

import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import com.github.leodan11.stepper.R;
import com.github.leodan11.stepper.StepperLayout;
import com.github.leodan11.stepper.adapter.StepAdapter;
import com.github.leodan11.stepper.internal.widget.TabsContainer;
import com.github.leodan11.stepper.viewmodel.StepViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Stepper type which displays horizontal stepper with tabs.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class TabsStepperType extends AbstractStepperType {

    private final TabsContainer mTabsContainer;

    public TabsStepperType(StepperLayout stepperLayout) {
        super(stepperLayout);
        mTabsContainer = stepperLayout.findViewById(R.id.ms_stepTabsContainer);
        mTabsContainer.setSelectedColor(stepperLayout.getSelectedColor());
        mTabsContainer.setUnselectedColor(stepperLayout.getUnselectedColor());
        mTabsContainer.setErrorColor(stepperLayout.getErrorColor());
        mTabsContainer.setDividerWidth(stepperLayout.getTabStepDividerWidth());
        mTabsContainer.setListener(stepperLayout);

        if (stepperLayout.isInEditMode()) {
            //noinspection ConstantConditions
            mTabsContainer.setSteps(Arrays.asList(
                    new StepViewModel.Builder().setTitle("Step 1").create(),
                    new StepViewModel.Builder().setTitle("Step 2").setSubtitle("Optional").create())
            );
            mTabsContainer.updateSteps(0, new SparseArray<>(), false);
            mTabsContainer.setVisibility(View.VISIBLE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStepSelected(int newStepPosition, boolean userTriggeredChange) {
        if (!mStepperLayout.isShowErrorStateEnabled()) {
            mStepErrors.clear();
        }

        mTabsContainer.updateSteps(newStepPosition, mStepErrors, mStepperLayout.isShowErrorMessageEnabled());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNewAdapter(@NonNull StepAdapter stepAdapter) {
        super.onNewAdapter(stepAdapter);
        List<StepViewModel> stepViewModels = new ArrayList<>();
        final int stepCount = stepAdapter.getCount();
        for (int i = 0; i < stepCount; i++) {
            stepViewModels.add(stepAdapter.getViewModel(i));
        }
        mTabsContainer.setSteps(stepViewModels);
        mTabsContainer.setVisibility(stepCount > 1 ? View.VISIBLE : View.GONE);
    }
}
