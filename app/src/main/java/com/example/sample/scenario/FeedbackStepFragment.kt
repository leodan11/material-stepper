package com.example.sample.scenario

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sample.R
import com.example.sample.ScenarioActivity
import com.example.sample.databinding.FragmentFeedbackStepBinding
import com.github.leodan11.stepper.Step
import com.github.leodan11.stepper.StepperLayout

/**
 * Exercises the disabled_content_interaction and disabled_bottom_navigation
 * feedback types during a simulated asynchronous operation.
 */
class FeedbackStepFragment : Fragment(), Step {

    private var _binding: FragmentFeedbackStepBinding? = null
    private val binding get() = _binding!!

    private var tapCount = 0
    private var hideProgressRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tapCount = savedInstanceState?.getInt(KEY_TAP_COUNT) ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedbackStepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateCounter()
        binding.feedbackStatus.setText(R.string.feedback_idle)

        binding.counterButton.setOnClickListener {
            tapCount++
            Log.d(TAG, "content tap accepted: $tapCount")
            updateCounter()
            binding.feedbackStatus.text =
                getString(R.string.feedback_tap_registered, tapCount)
        }

        binding.runAsyncButton.setOnClickListener {
            startAsyncOperation()
        }
    }

    private fun updateCounter() {
        binding.counterButton.text = getString(R.string.feedback_taps, tapCount)
    }

    private fun startAsyncOperation() {
        val currentBinding = _binding ?: return
        val stepper = requireActivity().findViewById<StepperLayout>(R.id.scenarioStepper)
        if (stepper.isInProgress) {
            return
        }

        val tapsAtStart = tapCount
        currentBinding.feedbackStatus.setText(R.string.feedback_running)
        stepper.showProgress(getString(R.string.feedback_progress_message))

        val runnable = Runnable {
            hideProgressRunnable = null
            stepper.hideProgress()
            _binding?.feedbackStatus?.text =
                getString(R.string.feedback_finished, tapCount - tapsAtStart)
        }
        hideProgressRunnable = runnable
        currentBinding.root.postDelayed(runnable, ASYNC_DURATION_MS)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_TAP_COUNT, tapCount)
    }

    override fun onDestroyView() {
        hideProgressRunnable?.let { _binding?.root?.removeCallbacks(it) }
        hideProgressRunnable = null
        (activity as? ScenarioActivity)
            ?.findViewById<StepperLayout>(R.id.scenarioStepper)
            ?.hideProgress()
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val TAG = "FeedbackStepFragment"
        private const val KEY_TAP_COUNT = "tapCount"
        private const val ASYNC_DURATION_MS = 3_000L
    }
}
