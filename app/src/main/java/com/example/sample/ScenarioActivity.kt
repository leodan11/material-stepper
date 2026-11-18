package com.example.sample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.sample.adapters.ScenarioFragmentStepAdapter
import com.example.sample.adapters.ScenarioStep
import com.example.sample.adapters.ViewStepAdapter
import com.example.sample.adapters.ViewStepsViewModel
import com.example.sample.scenario.BlockingStepFragment
import com.example.sample.scenario.FeedbackStepFragment
import com.example.sample.scenario.MessageStepFragment
import com.example.sample.scenario.VerifyStepFragment
import com.github.leodan11.stepper.StepperLayout
import com.github.leodan11.stepper.VerificationError
import com.google.android.material.appbar.MaterialToolbar
import kotlin.math.abs

/**
 * Hosts the scenarios used to exercise the ViewPager2 migration and the
 * less common StepperLayout features.
 */
class ScenarioActivity : AppCompatActivity() {

    private lateinit var stepperLayout: StepperLayout
    private var viewStepAdapter: ViewStepAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scenario = intent.getStringExtra(EXTRA_SCENARIO)
            ?.let { runCatching { Scenario.valueOf(it) }.getOrNull() }
            ?: Scenario.TABS

        setContentView(scenario.layoutRes)
        applySystemBarsPadding()

        val toolbar = findViewById<MaterialToolbar>(R.id.scenarioToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(scenario.titleRes)

        stepperLayout = findViewById(R.id.scenarioStepper)
        stepperLayout.setListener(createListener())

        when (scenario) {
            Scenario.TABS -> setupTabsScenario()
            Scenario.FEEDBACK -> setupFeedbackScenario()
            Scenario.VIEWS -> setupViewsScenario()
            Scenario.TRANSFORMER -> setupTransformerScenario()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onDestroy() {
        viewStepAdapter?.release()
        viewStepAdapter = null
        super.onDestroy()
    }

    /** Clears the error shown by the tabs stepper after the user edits the field. */
    fun clearStepError() {
        stepperLayout.updateErrorState(null)
    }

    private fun applySystemBarsPadding() {
        val root = findViewById<View>(R.id.scenarioRoot)
        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
    }

    private fun setupTabsScenario() {
        stepperLayout.setAdapter(
            ScenarioFragmentStepAdapter(
                this,
                listOf(
                    ScenarioStep(getString(R.string.tab_account)) { VerifyStepFragment() },
                    ScenarioStep(getString(R.string.tab_blocking)) { BlockingStepFragment() },
                    ScenarioStep(getString(R.string.tab_summary)) {
                        MessageStepFragment.newInstance(
                            getString(R.string.summary_title),
                            getString(R.string.summary_body),
                            blockComplete = true
                        )
                    }
                )
            )
        )
    }

    private fun setupFeedbackScenario() {
        stepperLayout.setAdapter(
            ScenarioFragmentStepAdapter(
                this,
                listOf(
                    ScenarioStep(getString(R.string.feedback_title)) { FeedbackStepFragment() },
                    ScenarioStep(getString(R.string.feedback_finish_title)) {
                        MessageStepFragment.newInstance(
                            getString(R.string.feedback_finish_title),
                            getString(R.string.feedback_finish_body),
                            blockComplete = false
                        )
                    }
                )
            )
        )
    }

    private fun setupViewsScenario() {
        val steps = ViewModelProvider(this)[ViewStepsViewModel::class.java].steps
        val adapter = ViewStepAdapter(this, steps)
        viewStepAdapter = adapter
        stepperLayout.setAdapter(adapter)
    }

    private fun setupTransformerScenario() {
        stepperLayout.setAdapter(
            ScenarioFragmentStepAdapter(
                this,
                (1..3).map { stepNumber ->
                    ScenarioStep(getString(R.string.transformer_step_title, stepNumber)) {
                        MessageStepFragment.newInstance(
                            getString(R.string.transformer_step_title, stepNumber),
                            getString(R.string.transformer_step_body),
                            blockComplete = false
                        )
                    }
                }
            )
        )
        stepperLayout.setPageTransformer { page, position ->
            val distance = abs(position).coerceAtMost(1f)
            val visibleFraction = 1f - distance
            val scale = 0.85f + 0.15f * visibleFraction
            page.scaleX = scale
            page.scaleY = scale
            page.alpha = 0.4f + 0.6f * visibleFraction
        }
    }

    private fun createListener(): StepperLayout.StepperListener {
        return object : StepperLayout.StepperListener {
            override fun onCompleted(completeButton: View?) {
                Log.d(TAG, "onCompleted")
                Toast.makeText(
                    this@ScenarioActivity,
                    R.string.scenario_completed,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onError(verificationError: VerificationError?) {
                Log.d(TAG, "onError: ${verificationError?.errorMessage}")
            }

            override fun onStepSelected(newStepPosition: Int) {
                Log.d(TAG, "onStepSelected($newStepPosition)")
            }

            override fun onReturn() {
                Log.d(TAG, "onReturn")
                finish()
            }
        }
    }

    enum class Scenario(val titleRes: Int, val layoutRes: Int) {
        TABS(R.string.scenario_tabs, R.layout.activity_scenario_tabs),
        FEEDBACK(R.string.scenario_feedback, R.layout.activity_scenario_feedback),
        VIEWS(R.string.scenario_views, R.layout.activity_scenario_views),
        TRANSFORMER(R.string.scenario_transformer, R.layout.activity_scenario_transformer)
    }

    companion object {
        private const val TAG = "ScenarioActivity"

        const val EXTRA_SCENARIO = "scenario"

        fun createIntent(activity: AppCompatActivity, scenario: Scenario): Intent {
            return Intent(activity, ScenarioActivity::class.java)
                .putExtra(EXTRA_SCENARIO, scenario.name)
        }
    }
}
