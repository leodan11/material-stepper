package com.example.sample.step

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.sample.R
import com.github.leodan11.stepper.Step
import com.github.leodan11.stepper.VerificationError
import kotlin.random.Random

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment : Fragment(), Step {
    private var param1: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getLong(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = inflater.inflate(R.layout.fragment_blank, container, false)
        with(binding) {

            val random = Random

            findViewById<TextView>(R.id.textViewBlank).text = random.nextDouble().toString()

            findViewById<Button>(R.id.button).setOnClickListener { findViewById<TextView>(R.id.textViewBlank).text = random.nextFloat().toString() }

        }
        return binding
    }

    override fun verifyStep(): VerificationError?  = null

    override fun onSelected()  = Unit

    override fun onError(error: VerificationError) = Unit

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment BlankFragment.
         */
        @JvmStatic
        fun newInstance(param1: Long = Random.nextLong()) =
            BlankFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PARAM1, param1)
                }
            }
    }

}