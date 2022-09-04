package com.calculator.evaluation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.calculator.entities.EmptyField
import com.calculator.entities.EvaluationToken
import com.calculator.entities.ListItem
import com.calculator.entities.Numeric
import com.calculator.entities.Operation
import com.calculator.entities.Parentheses
import com.calculator.input.api.CalculatorInputListener
import com.calculator.input.api.CalculatorInputObserver
import com.calculator.util.Digit

class EvaluationComponentImpl(
    private val tokens: List<EvaluationToken>,
    calculatorInputObserver: CalculatorInputObserver,
) : EvaluationComponent {
    private val itemsState: MutableState<List<ListItem>> = mutableStateOf(listOf(EmptyField) + tokens)

    private val listener = object : CalculatorInputListener {
        override fun onOperationClick(operation: Operation) {
            val index = getCurrentIndex()
            if (index < 0) {
                return
            }
            val newList = itemsState.value.toMutableList()
            newList.add(index, operation)
            itemsState.value = processItems(newList, SelectionData(operation, 1))
        }

        override fun onParenthesesClick(parentheses: Parentheses) {
            val index = getCurrentIndex()
            if (index < 0) {
                return
            }
            val newList = itemsState.value.toMutableList()
            newList.add(index, parentheses)
            itemsState.value = processItems(newList, SelectionData(parentheses, 1))
        }

        override fun onDigitClick(digit: Digit) {
            val index = getCurrentIndex()
            if (index < 0) {
                return
            }
            val newList = itemsState.value.toMutableList()
            if (index > 0) {
                /**
                 * Слева стоит число, с которым мержим эту цифру
                 */
                val prev = newList[index - 1]
                if (prev is Numeric) {
                    newList[index - 1] = prev.copy(value = prev.value + digit.value.toString())
                } else {
                    newList.add(index, Numeric(digit.value.toString(), Any()))
                }
            } else {
                newList.add(index, Numeric(digit.value.toString(), Any()))
            }
            itemsState.value = newList
        }

        override fun onEraseClick() {
            val index = getCurrentIndex()
            if (index < 0) {
                return
            }
            val newList = itemsState.value.toMutableList()
            if (index > 0) {

                val prev = newList[index - 1]
                if (prev is Numeric) {
                    /**
                     * Слева стоит число, с которого удаляем цифру
                     */
                    val newNumber = prev.value.substring(0 until prev.value.lastIndex)
                    if (newNumber.isEmpty()) {
                        newList.removeAt(index - 1)
                    } else {
                        newList[index - 1] = Numeric(newNumber, Any())
                    }
                } else {
                    newList.removeAt(index - 1)
                }
            }
            itemsState.value = newList
        }

        override fun onClearAllClick() {
            itemsState.value = listOf(EmptyField)
        }

        override fun onEvaluateClick() = Unit
    }

    init {
        calculatorInputObserver.addListener(listener)
    }

    @Composable
    override fun EvaluationContent() {
        EvaluationContent(
            itemsState = itemsState,
            onClick = { position, item ->
                if (itemsState.value.contains(item)) {
                    itemsState.value = processItems(itemsState.value, SelectionData(item, position))
                }
            }
        )
    }

    override fun getCurrentEvaluation(): List<EvaluationToken> {
        return tokens
    }

    private fun getCurrentIndex(): Int {
        return itemsState.value.indexOf(EmptyField)
    }

    private fun processItems(items: List<ListItem>, selectionData: SelectionData): List<ListItem> {
        if (items.isEmpty()) {
            return listOf(EmptyField)
        }
        if (selectionData.item is EmptyField) {
            return items
        }
        val newItems = connectItems(items).toMutableList()
        val index = newItems.indexOf(selectionData.item)
        if (index == -1) {
            /**
             * Единственный случай, когда мы могли потерять то число, в которое кликнули, это когда оно было смержено
             * В таком случае нужно чуть чуть поменять selectionData
             */
            val oldIndex = items.indexOf(EmptyField)
            val prev = items[oldIndex - 1] as Numeric
            val next = items[oldIndex + 1] as Numeric
            val position = if (selectionData.item === next) {
                prev.value.length
            } else {
                0
            } + selectionData.position
            return splitNumerics(newItems, SelectionData(newItems[oldIndex - 1], position))
        }
        return splitNumerics(newItems, data = selectionData)
    }

    /**
     * Разобьем числа по полю ввода
     */
    private fun splitNumerics(items: List<ListItem>, data: SelectionData): List<ListItem> {
        val item = data.item
        val index = items.indexOf(item)
        val newItems = items.toMutableList()
        if (item !is Numeric || data.position == 0 || data.position == item.value.length) {
            newItems.add(if (data.position == 0) index else index + 1, EmptyField)
            return newItems
        }
        val prev = Numeric(item.value.substring(0 until data.position), Any())
        val next = Numeric(item.value.substring(data.position), Any())
        newItems.remove(item)
        newItems.add(index, prev)
        newItems.add(index + 1, EmptyField)
        newItems.add(index + 2, next)
        return newItems
    }

    /**
     * Мержим назад числа, возможно разделенные полем ввода
     */
    private fun connectItems(items: List<ListItem>): List<ListItem> {
        val index = items.indexOf(EmptyField)
        if (index <= 0 || index == items.lastIndex) {
            return items.filter { it !is EmptyField }
        }
        val prev = items[index - 1]
        val next = items[index + 1]
        if (prev !is Numeric || next !is Numeric) {
            return items.filter { it !is EmptyField }
        }
        val merged = Numeric(prev.value + next.value, Any())
        val newList = items.toMutableList()
        newList.add(index, merged)
        newList.remove(prev)
        newList.remove(next)
        newList.remove(EmptyField)
        return newList
    }
}