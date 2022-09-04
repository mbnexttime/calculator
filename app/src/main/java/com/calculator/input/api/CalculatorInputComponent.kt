package com.calculator.input.api

import androidx.compose.runtime.Composable

interface CalculatorInputComponent : CalculatorInputObserver {

    @Composable
    fun CalculatorInput()
}