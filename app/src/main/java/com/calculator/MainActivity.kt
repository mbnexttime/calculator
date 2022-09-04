package com.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import com.calculator.evaluation.ui.EvaluationComponentImpl
import com.calculator.ui.input.CalculatorKeyboard
import com.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inputComponent = CalculatorKeyboard()
        val evaluationComponent = EvaluationComponentImpl(
            tokens = listOf(),
            calculatorInputObserver = inputComponent,
        )
        setContent {
            CalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ConstraintLayout {
                        val (eval, input) = createRefs()
                        Box(
                            Modifier
                                .constrainAs(eval) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(input.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                        ) {
                            evaluationComponent.EvaluationContent()
                        }
                        Box(
                            Modifier
                                .constrainAs(input) {
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                        ) {
                            inputComponent.CalculatorInput()
                        }
                    }
                }
            }
        }
    }
}