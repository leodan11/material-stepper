package com.example.sample.step

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sample.databinding.FragmentFirstBinding
import com.github.leodan11.stepper.Step
import com.github.leodan11.stepper.VerificationError
import kotlin.random.Random

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), Step {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            val random = Random

            textViewFirst.text = random.nextDouble().toString()

            button.setOnClickListener { textViewFirst.text = random.nextDouble().toString() }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun verifyStep(): VerificationError?  = null

    override fun onSelected()  = Unit

    override fun onError(error: VerificationError) = Unit

    companion object {

        @JvmStatic
        fun newInstance() =
            FirstFragment().apply {
                arguments = Bundle().apply {
                }
            }

    }

}