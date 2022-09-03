package com.calculator.entities

sealed interface Operation: EvaluationToken {
    object Addition: Operation
    object Multiplication: Operation
    object Division: Operation
    object Subtract: Operation
    object Percent: Operation
    object SquareRoot: Operation
}