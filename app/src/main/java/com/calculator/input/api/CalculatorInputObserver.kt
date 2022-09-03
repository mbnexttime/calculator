package com.calculator.input.api

interface CalculatorInputObserver {

    fun addListener(listener: CalculatorInputListener)

    fun removeListener(listener: CalculatorInputListener)
}