package com.calculator.input.api

import android.content.Context
import android.view.View
import androidx.compose.runtime.Composable

interface CalculatorInputComponent {
    
    fun createContent(context: Context): Composable
    
    fun addListener(listener: CalculatorInputListener)
    
    fun removeListener(listener: CalculatorInputListener)
}