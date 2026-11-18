package com.example.sample.adapters

import androidx.lifecycle.ViewModel

/**
 * Owns the plain-View step models so their counters survive configuration
 * changes. The adapter is recreated with the activity, the model is not.
 */
class ViewStepsViewModel : ViewModel() {

    val steps: List<ViewStep> = List(STEP_COUNT) { position -> ViewStep(position) }

    companion object {
        const val STEP_COUNT = 3
    }
}
