package com.calculator.entities

data class Numeric(
    val value: String,
    val token: Any,
) : EvaluationToken
