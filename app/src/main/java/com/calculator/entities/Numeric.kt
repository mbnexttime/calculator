package com.calculator.entities

import java.math.BigInteger

data class Numeric(
    val value: BigInteger,
) : EvaluationToken
