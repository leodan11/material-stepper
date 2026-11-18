package com.example.sample.adapters

import androidx.fragment.app.FragmentActivity
import com.github.leodan11.stepper.Step
import com.github.leodan11.stepper.adapter.AbstractFragmentStepAdapter
import com.github.leodan11.stepper.viewmodel.StepViewModel

/**
 * A [Step] plus the title used by the tabs stepper.
 */
data class ScenarioStep(
    val title: String,
    val factory: () -> Step
)

/**
 * Fragment adapter used by the example scenarios. It exercises the
 * [FragmentActivity] constructor of [AbstractFragmentStepAdapter].
 */
class ScenarioFragmentStepAdapter(
    activity: FragmentActivity,
    private val steps: List<ScenarioStep>
) : AbstractFragmentStepAdapter(activity) {

    override fun getCount(): Int = steps.size

    override fun createStep(position: Int): Step = steps[position].factory()

    override fun getViewModel(position: Int): StepViewModel {
        return StepViewModel.Builder(context)
            .setTitle(steps[position].title)
            .create()
    }
}
