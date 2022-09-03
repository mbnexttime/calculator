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
    private val itemsState: MutableState<List<ListItem>> = mutableStateOf(processItems(tokens))
    private val selectionState = mutableStateOf(SelectionData(itemsState.value.first(), 0))

    private val listener = object : CalculatorInputListener {
        override fun onOperationClick(operation: Operation) {
            val index = getCurrentIndex()
            if (index < 0) {
                return
            }
            val newList = itemsState.value.toMutableList()
            newList.add(
                if (selectionState.value.position == 0) {
                    index
                } else {
                    index + 1
                },
                operation
            )
            selectionState.value = SelectionData(operation, 1)
            itemsState.value = processItems(newList)
        }

        override fun onParenthesesClick(parentheses: Parentheses) = Unit

        override fun onDigitClick(digit: Digit) {
            val index = getCurrentIndex()
            if (index < 0) {
                return
            }
            val newList = itemsState.value.toMutableList()
            if (selectionState.value.position == 0 && index > 0) {
                val prev = newList[index - 1]
                if (prev is Numeric) {
                    newList[index - 1] = prev.copy(value = prev.value + digit.value.toString())
                } else {
                    newList.add(index, Numeric(digit.value.toString(), Any()))
                }
            } else if (selectionState.value.position != 0 && index < newList.lastIndex) {
                val next = newList[index + 1]
                if (next is Numeric) {
                    newList[index + 1] = next.copy(value = digit.value.toString() + next.value)
                } else {
                    newList.add(index + 1, Numeric(digit.value.toString(), Any()))
                }
            } else {
                newList.add(
                    if (selectionState.value.position == 0) index else index + 1, Numeric(digit.value.toString(), Any())
                )
            }
            itemsState.value = newList
        }

        override fun onEraseClick() {
            TODO("Not yet implemented")
        }

        override fun onClearAllClick() = Unit

        override fun onCommaClick() = Unit

        override fun onEvaluateClick() = Unit
    }

    init {
        calculatorInputObserver.addListener(listener)
    }

    @Composable
    override fun EvaluationContent() {
        EvaluationContent(
            itemsState = itemsState,
            selectionState = selectionState,
            onValueChanged = { value, item ->
                if (itemsState.value.contains(item)) {
                    selectionState.value = SelectionData(item, value.selection.start)
                    itemsState.value = processItems(itemsState.value)
                }
            }
        )
    }

    override fun getCurrentEvaluation(): List<EvaluationToken> {
        return tokens
    }

    private fun getCurrentIndex(): Int {
        return itemsState.value.indexOf(selectionState.value.listItem)
    }

    private fun processItems(items: List<ListItem>): List<ListItem> {
        if (items.isEmpty()) {
            return listOf(EmptyField)
        }
        if (selectionState.value.listItem is EmptyField) {
            return items
        }
        val newItems = connectItems(items).toMutableList()
        var index = newItems.indexOf(selectionState.value.listItem)
        if (index == -1) {
            index = items.indexOf(EmptyField)
            val prev = items[index - 1] as Numeric
            val next = items[index + 1] as Numeric
            var position = 0
            if (selectionState.value.listItem === next) {
                position += prev.value.length
            }
            selectionState.value = SelectionData(newItems[index - 1], position + selectionState.value.position)
        }
        val item = selectionState.value.listItem
        index = newItems.indexOf(item)
        if (item !is Numeric
            || selectionState.value.position == 0
            || selectionState.value.position == item.value.length
        ) {
            newItems.add(if (selectionState.value.position == 0) index else index + 1, EmptyField)
            selectionState.value = SelectionData(EmptyField, 0)
            return newItems
        }
        val prev = Numeric(item.value.substring(0 until selectionState.value.position), Any())
        val next = Numeric(item.value.substring(selectionState.value.position), Any())
        newItems.remove(item)
        newItems.add(index, prev)
        newItems.add(index + 1, EmptyField)
        newItems.add(index + 2, next)
        selectionState.value = SelectionData(EmptyField, 0)
        return newItems
    }

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