package com.calculator.ui.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.calculator.R
import com.calculator.entities.Operation
import com.calculator.entities.Parentheses
import com.calculator.input.api.CalculatorInputComponent
import com.calculator.input.api.CalculatorInputListener
import com.calculator.util.Digit

class CalculatorKeyboard : CalculatorInputComponent {
    data class KeyBoardButtonParams(
        val id: Int,
        val size: Dp,
        val color: Color,
        val buttonType: CalculatorButton
    )

    sealed interface CalculatorButton {
        data class DigitButton(val digit: Char) : CalculatorButton
        object ClearAllButton : CalculatorButton
        object EraseButton : CalculatorButton
        object DivisionButton : CalculatorButton
        object MultiplicationButton : CalculatorButton
        object AdditionButton : CalculatorButton
        object SubtractButton : CalculatorButton
        object EvalButton : CalculatorButton
        object ForwardParenthesesButton : CalculatorButton
        object BackParenthesesButton : CalculatorButton
    }

    val digitsColor = Color(18, 10, 143)
    private var inputListener: CalculatorInputListener? = null

    @Composable
    override fun CalculatorInput() {
        val buttonsParams = listOf(
            KeyBoardButtonParams(
                R.drawable.ic_icons8_clear_symbol_100,
                32.dp,
                Color.Black,
                CalculatorButton.EraseButton
            ),
            KeyBoardButtonParams(
                R.drawable.ic_left_p,
                32.dp,
                Color.Black,
                CalculatorButton.ForwardParenthesesButton
            ), KeyBoardButtonParams(
                R.drawable.ic_right_p,
                32.dp,
                Color.Black,
                CalculatorButton.BackParenthesesButton
            ),
            KeyBoardButtonParams(
                R.drawable.ic_icons8_divide_100,
                32.dp,
                Color.Black,
                CalculatorButton.DivisionButton
            ),
            KeyBoardButtonParams(
                R.drawable.ic_icons8_circled_7_100,
                48.dp,
                digitsColor,
                CalculatorButton.DigitButton('7')
            ),
            KeyBoardButtonParams(
                R.drawable.ic_icons8_circled_8_100,
                48.dp,
                digitsColor,
                CalculatorButton.DigitButton('8')
            ),
            KeyBoardButtonParams(
                R.drawable.ic_icons8_circled_9_100,
                48.dp,
                digitsColor,
                CalculatorButton.DigitButton('9')
            ),
            KeyBoardButtonParams(
                R.drawable.ic_icons8_multiply_100,
                32.dp,
                Color.Black,
                CalculatorButton.MultiplicationButton
            ),
            KeyBoardButtonParams(
                R.drawable.ic_icons8_circled_4_100,
                48.dp,
                digitsColor,
                CalculatorButton.DigitButton('4')
            ),
            KeyBoardButtonParams(
                R.drawable.ic_icons8_circled_5_100,
                48.dp,
                digitsColor,
                CalculatorButton.DigitButton('5')
            ),
            KeyBoardButtonParams(
                R.drawable.ic_icons8_circled_6_100,
                48.dp,
                digitsColor,
                CalculatorButton.DigitButton('6')
            ),
            KeyBoardButtonParams(
                R.drawable.ic_icons8_subtract_100,
                32.dp,
                Color.Black,
                CalculatorButton.SubtractButton
            ),
            KeyBoardButtonParams(
                R.drawable.ic_icons8_1st_100,
                48.dp,
                digitsColor,
                CalculatorButton.DigitButton('1')
            ),
            KeyBoardButtonParams(
                R.drawable.ic_icons8_circled_2_100,
                48.dp,
                digitsColor,
                CalculatorButton.DigitButton('2')
            ),
            KeyBoardButtonParams(
                R.drawable.ic_icons8_circled_3_100,
                48.dp,
                digitsColor,
                CalculatorButton.DigitButton('3')
            ),
            KeyBoardButtonParams(
                R.drawable.ic_icons8_plus_math_100,
                32.dp,
                Color.Black,
                CalculatorButton.AdditionButton
            ),
            KeyBoardButtonParams(
                R.drawable.ic_icons8_c_96,
                24.dp,
                Color.Black,
                CalculatorButton.ClearAllButton
            ),
            KeyBoardButtonParams(
                R.drawable.ic_icons8_circled_0_100,
                48.dp,
                digitsColor,
                CalculatorButton.DigitButton('0')
            ),
            null,
            KeyBoardButtonParams(
                R.drawable.ic_icons8_equal_sign_100,
                32.dp,
                Color.Black,
                CalculatorButton.EvalButton
            )
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(buttonsParams) { params ->
                params?.let { KeyBoardButton(it) }
            }
        }
    }

    override fun addListener(listener: CalculatorInputListener) {
        inputListener = listener
    }

    override fun removeListener(listener: CalculatorInputListener) {
        inputListener = null
    }

    @Composable
    private fun KeyBoardButton(params: KeyBoardButtonParams) {
        IconButton(
            onClick = { handleTouchEvent(params.buttonType) },
            Modifier.size(48.dp),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = params.color
            )

        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = params.id),
                contentDescription = "KeyBoardButton",
                Modifier.size(params.size),
            )
        }
    }

    private fun handleTouchEvent(button: CalculatorButton) {
        when (button) {
            CalculatorButton.ClearAllButton -> inputListener?.onClearAllClick()
            CalculatorButton.EraseButton -> inputListener?.onEraseClick()
            is CalculatorButton.DigitButton -> inputListener?.onDigitClick(Digit(button.digit))
            CalculatorButton.AdditionButton -> inputListener?.onOperationClick(Operation.Addition())
            CalculatorButton.SubtractButton -> inputListener?.onOperationClick(Operation.Subtract())
            CalculatorButton.MultiplicationButton -> inputListener?.onOperationClick(Operation.Multiplication())
            CalculatorButton.DivisionButton -> inputListener?.onOperationClick(Operation.Division())
            CalculatorButton.EvalButton -> inputListener?.onEvaluateClick()
            CalculatorButton.ForwardParenthesesButton -> inputListener?.onParenthesesClick(
                Parentheses.Forward()
            )
            CalculatorButton.BackParenthesesButton -> inputListener?.onParenthesesClick(
                Parentheses.Back()
            )
        }
    }
}