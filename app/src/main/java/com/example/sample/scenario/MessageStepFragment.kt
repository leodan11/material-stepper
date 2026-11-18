package com.example.sample.scenario

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sample.R
import com.example.sample.databinding.FragmentMessageStepBinding
import com.github.leodan11.stepper.BlockingStep
import com.github.leodan11.stepper.StepperLayout

/**
 * Generic message step. It tracks onSelected() calls and can optionally
 * block the completion callback.
 */
class MessageStepFragment : Fragment(), BlockingStep {

    private var _binding: FragmentMessageStepBinding? = null
    private val binding get() = _binding!!

    private val handler = Handler(Looper.getMainLooper())
    private var selectedCount = 0
    private var blockComplete = false
    private var pendingComplete: StepperLayout.OnCompleteClickedCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        blockComplete = arguments?.getBoolean(ARG_BLOCK_COMPLETE) ?: false
        selectedCount = savedInstanceState?.getInt(KEY_SELECTED_COUNT) ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageStepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.messageTitle.text = arguments?.getString(ARG_TITLE)
        binding.messageBody.text = arguments?.getString(ARG_BODY)
        updateSelectedCount()
    }

    override fun onSelected() {
        selectedCount++
        if (_binding != null) {
            updateSelectedCount()
        }
    }

    override fun onCompleteClicked(callback: StepperLayout.OnCompleteClickedCallback) {
        if (!blockComplete) {
            callback.complete()
            return
        }
        if (pendingComplete != null) {
            return
        }
        pendingComplete = callback
        _binding?.messageStatus?.setText(R.string.blocking_complete_wait)
        handler.postDelayed({
            pendingComplete = null
            callback.complete()
        }, COMPLETE_DELAY_MS)
    }

    private fun updateSelectedCount() {
        binding.selectedCount.text = getString(R.string.selected_count, selectedCount)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SELECTED_COUNT, selectedCount)
    }

    override fun onDestroyView() {
        handler.removeCallbacksAndMessages(null)
        pendingComplete = null
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_BODY = "body"
        private const val ARG_BLOCK_COMPLETE = "blockComplete"
        private const val KEY_SELECTED_COUNT = "selectedCount"
        private const val COMPLETE_DELAY_MS = 1_200L

        fun newInstance(title: String, body: String, blockComplete: Boolean): MessageStepFragment {
            return MessageStepFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_BODY, body)
                    putBoolean(ARG_BLOCK_COMPLETE, blockComplete)
                }
            }
        }
    }
}
