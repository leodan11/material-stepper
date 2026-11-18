package com.example.sample.scenario

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.example.sample.R
import com.example.sample.ScenarioActivity
import com.example.sample.databinding.FragmentVerifyStepBinding
import com.github.leodan11.stepper.Step
import com.github.leodan11.stepper.VerificationError

/**
 * Exercises verifyStep(), onError(), onSelected() and state persistence.
 */
class VerifyStepFragment : Fragment(), Step {

    private var _binding: FragmentVerifyStepBinding? = null
    private val binding get() = _binding!!

    private var name: String = ""
    private var selectedCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        name = savedInstanceState?.getString(KEY_NAME).orEmpty()
        selectedCount = savedInstanceState?.getInt(KEY_SELECTED_COUNT) ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerifyStepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nameInput.setText(name)
        updateSelectedCount()
        binding.nameInput.doAfterTextChanged {
            name = it?.toString().orEmpty()
            binding.nameInputLayout.error = null
            (activity as? ScenarioActivity)?.clearStepError()
        }
    }

    override fun verifyStep(): VerificationError? {
        val currentBinding = _binding ?: return null
        return if (currentBinding.nameInput.text.isNullOrBlank()) {
            VerificationError(getString(R.string.verify_name_required))
        } else {
            null
        }
    }

    override fun onError(error: VerificationError) {
        _binding?.nameInputLayout?.error = error.errorMessage
        _binding?.nameInput?.requestFocus()
    }

    override fun onSelected() {
        selectedCount++
        Log.d(TAG, "onSelected: $selectedCount")
        if (_binding != null) {
            updateSelectedCount()
        }
    }

    private fun updateSelectedCount() {
        binding.selectedCount.text = getString(R.string.selected_count, selectedCount)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_NAME, name)
        outState.putInt(KEY_SELECTED_COUNT, selectedCount)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val TAG = "VerifyStepFragment"
        private const val KEY_NAME = "name"
        private const val KEY_SELECTED_COUNT = "selectedCount"
    }
}
