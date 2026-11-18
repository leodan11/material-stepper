package com.example.sample.scenario

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sample.R
import com.example.sample.databinding.FragmentBlockingStepBinding
import com.github.leodan11.stepper.BlockingStep
import com.github.leodan11.stepper.StepperLayout

/**
 * Exercises the asynchronous BlockingStep callbacks for Next and Back.
 */
class BlockingStepFragment : Fragment(), BlockingStep {

    private var _binding: FragmentBlockingStepBinding? = null
    private val binding get() = _binding!!

    private val handler = Handler(Looper.getMainLooper())
    private var pendingNext: StepperLayout.OnNextClickedCallback? = null
    private var pendingBack: StepperLayout.OnBackClickedCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBlockingStepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.blockingStatus.setText(R.string.blocking_idle)
    }

    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback) {
        if (pendingNext != null) {
            return
        }
        pendingNext = callback
        _binding?.blockingStatus?.setText(R.string.blocking_next_wait)
        handler.postDelayed({
            pendingNext = null
            _binding?.blockingStatus?.setText(R.string.blocking_next_done)
            callback.goToNextStep()
        }, NEXT_DELAY_MS)
    }

    override fun onBackClicked(callback: StepperLayout.OnBackClickedCallback) {
        if (pendingBack != null) {
            return
        }
        pendingBack = callback
        _binding?.blockingStatus?.setText(R.string.blocking_back_wait)
        handler.postDelayed({
            pendingBack = null
            callback.goToPrevStep()
        }, BACK_DELAY_MS)
    }

    override fun onDestroyView() {
        handler.removeCallbacksAndMessages(null)
        pendingNext = null
        pendingBack = null
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val NEXT_DELAY_MS = 1_500L
        private const val BACK_DELAY_MS = 1_000L
    }
}
