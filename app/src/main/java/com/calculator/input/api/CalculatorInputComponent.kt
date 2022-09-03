package com.calculator.input.api

import android.content.Context
import android.view.View
import androidx.compose.runtime.Composable

interface CalculatorInputComponent {
    
    @Composable
    fun CalculatorInput()
    
    fun addListener(listener: CalculatorInputListener)
    
    fun removeListener(listener: CalculatorInputListener)
}