package com.example.sample.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sample.R
import com.example.sample.databinding.ItemViewStepBinding
import com.github.leodan11.stepper.Step
import com.github.leodan11.stepper.adapter.AbstractStepAdapter
import com.github.leodan11.stepper.viewmodel.StepViewModel

/**
 * State for a plain-View step. It lives outside the view holders and the
 * adapter so the values survive RecyclerView recycling and rotation.
 */
class ViewStep(val position: Int) : Step {

    var tapCount = 0
    var selectedCount = 0
    var onChanged: (() -> Unit)? = null

    override fun onSelected() {
        selectedCount++
        onChanged?.invoke()
    }
}

/**
 * Exercises the View-based [AbstractStepAdapter] contract after the
 * migration to RecyclerView.
 */
class ViewStepAdapter(
    context: Context,
    private val steps: List<ViewStep>
) : AbstractStepAdapter(context) {

    init {
        steps.forEach { step ->
            step.onChanged = { notifyItemChanged(step.position) }
        }
    }

    override fun getCount(): Int = steps.size

    override fun createStep(position: Int): Step = steps[position]

    override fun findStep(position: Int): Step? = steps.getOrNull(position)

    override fun getViewModel(position: Int): StepViewModel {
        return StepViewModel.Builder(context)
            .setTitle(context.getString(R.string.view_step_title, position + 1))
            .create()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemViewStepBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StepViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as StepViewHolder).bind(steps[position])
    }

    /** Drops the callbacks so the model does not keep a destroyed adapter alive. */
    fun release() {
        steps.forEach { it.onChanged = null }
    }

    private class StepViewHolder(
        private val binding: ItemViewStepBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(step: ViewStep) {
            val context = binding.root.context
            binding.viewStepTitle.text = context.getString(R.string.view_step_title, step.position + 1)
            binding.viewStepDescription.text = context.getString(R.string.view_step_body)
            binding.viewStepTaps.text = context.getString(R.string.view_step_taps, step.tapCount)
            binding.viewStepSelected.text = context.getString(R.string.selected_count, step.selectedCount)
            binding.viewStepTapButton.setOnClickListener {
                step.tapCount++
                step.onChanged?.invoke()
            }
        }
    }
}
