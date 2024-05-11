package com.example.sample.adapters

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.example.sample.step.BlankFragment
import com.example.sample.step.FirstFragment
import com.example.sample.step.SecondFragment
import com.github.leodan11.stepper.Step
import com.github.leodan11.stepper.adapter.AbstractFragmentStepAdapter

class SampleFragmentStepAdapter(context: Context, fragmentManager: FragmentManager) : AbstractFragmentStepAdapter(context, fragmentManager) {

    override fun getCount(): Int = 3

    override fun createStep(position: Int): Step {
        when(position) {
            0 -> return FirstFragment.newInstance()
            1 -> return SecondFragment.newInstance()
            2 -> return BlankFragment.newInstance()
            else -> throw IllegalArgumentException("Unsupported position: $position")
        }
    }

}