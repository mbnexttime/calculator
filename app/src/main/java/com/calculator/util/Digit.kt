package com.calculator.util

data class Digit(
    private val digit: Char
) {
    val value = digit.digitToInt()
}