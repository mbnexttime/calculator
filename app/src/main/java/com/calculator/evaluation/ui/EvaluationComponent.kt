package com.calculator.evaluation.ui

import androidx.compose.runtime.Composable
import com.calculator.entities.EvaluationToken

interface EvaluationComponent {
    @Composable
    fun EvaluationContent()

    fun getCurrentEvaluation(): List<EvaluationToken>
}