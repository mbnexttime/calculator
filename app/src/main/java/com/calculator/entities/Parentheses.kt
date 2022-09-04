package com.calculator.entities

sealed interface Parentheses: EvaluationToken {
    object Back: Parentheses
    object Forward: Parentheses
}