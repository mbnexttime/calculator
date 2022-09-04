package com.calculator.entities

sealed interface Parentheses: EvaluationToken {
    class Back : Parentheses
    class Forward : Parentheses
}