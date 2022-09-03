package com.calculator.entities

sealed interface Operation: EvaluationToken {
    class Addition : Operation
    class Multiplication : Operation
    class Division : Operation
    class Subtract : Operation
    class Percent : Operation
    class SquareRoot : Operation
}