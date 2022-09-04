package com.calculator.evaluation.ui

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.calculator.entities.EmptyField
import com.calculator.entities.ListItem
import com.calculator.entities.Numeric
import com.calculator.entities.Operation
import com.calculator.entities.Parentheses

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUnitApi::class)
@Composable
fun EvaluationContent(
    itemsState: MutableState<List<ListItem>>,
    onValueChanged: (value: TextFieldValue, item: ListItem) -> Unit,
) {
    val items by remember {
        itemsState
    }
    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    LazyRow(
        state = state,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .focusable(enabled = true)
            .focusRequester(focusRequester),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        content = {
            items(items) { item ->
                val customTextSelectionColors = TextSelectionColors(
                    handleColor = Color.Transparent,
                    backgroundColor = Color.Transparent,
                )
                CompositionLocalProvider(
                    LocalTextInputService provides null,
                    LocalTextSelectionColors provides customTextSelectionColors,
                ) {
                    DisableSelection {
                        LocalFocusManager.current.clearFocus(force = true)
                        val modifier = Modifier
                            .width(IntrinsicSize.Min)
                            .height(IntrinsicSize.Min)
                            .padding(start = 2.dp, end = 2.dp)
                        val textStyle = TextStyle(
                            fontSize = TextUnit(20.0f, TextUnitType.Sp),
                            textAlign = TextAlign.Center,
                        )
                        when (item) {
                            is Numeric -> BasicTextField(
                                textStyle = textStyle,
                                modifier = modifier,
                                value = TextFieldValue(item.value),
                                onValueChange = { onValueChanged.invoke(it, item) },
                                cursorBrush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Transparent
                                    )
                                )
                            )
                            is Operation.Addition -> BasicTextField(
                                textStyle = textStyle,
                                modifier = modifier,
                                value = TextFieldValue("+"),
                                onValueChange = { onValueChanged.invoke(it, item) },
                                cursorBrush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Transparent
                                    )
                                )
                            )
                            is Operation.Division -> BasicTextField(
                                textStyle = textStyle,
                                modifier = modifier,
                                value = TextFieldValue("/"),
                                onValueChange = { onValueChanged.invoke(it, item) },
                                cursorBrush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Transparent
                                    )
                                )
                            )
                            is Operation.Multiplication -> BasicTextField(
                                textStyle = textStyle,
                                modifier = modifier,
                                value = TextFieldValue("*"),
                                onValueChange = { onValueChanged.invoke(it, item) },
                                cursorBrush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Transparent
                                    )
                                )
                            )
                            is Operation.Subtract -> BasicTextField(
                                textStyle = textStyle,
                                modifier = modifier,
                                value = TextFieldValue("-"),
                                onValueChange = { onValueChanged.invoke(it, item) },
                                cursorBrush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Transparent
                                    )
                                )
                            )
                            is Parentheses.Back -> BasicTextField(
                                textStyle = textStyle,
                                modifier = modifier,
                                value = TextFieldValue(")"),
                                onValueChange = { onValueChanged.invoke(it, item) },
                                cursorBrush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Transparent
                                    )
                                )
                            )
                            is Parentheses.Forward -> BasicTextField(
                                textStyle = textStyle,
                                modifier = modifier,
                                value = TextFieldValue("("),
                                onValueChange = { onValueChanged.invoke(it, item) },
                                cursorBrush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Transparent
                                    )
                                )
                            )
                            is EmptyField -> BasicTextField(
                                textStyle = textStyle.copy(color = Color.Red),
                                modifier = modifier,
                                value = TextFieldValue("_"),
                                onValueChange = { onValueChanged.invoke(it, item) },
                                cursorBrush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Transparent
                                    )
                                )
                            )
                            else -> throw RuntimeException()
                        }
                    }
                }
            }
        },
    )
}