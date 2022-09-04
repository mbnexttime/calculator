package com.calculator.input.api

import com.calculator.entities.Operation
import com.calculator.entities.Parentheses
import com.calculator.util.Digit

interface CalculatorInputListener {

    fun onOperationClick(operation: Operation)

    fun onParenthesesClick(parentheses: Parentheses)

    fun onDigitClick(digit: Digit)

    fun onEraseClick()

    fun onClearAllClick()

    fun onEvaluateClick()
}