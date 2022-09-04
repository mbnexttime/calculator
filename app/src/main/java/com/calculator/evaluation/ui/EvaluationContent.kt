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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
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
    onClick: (position: Int, item: ListItem) -> Unit,
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

                val modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .height(IntrinsicSize.Min)
                    .padding(start = 2.dp, end = 2.dp)
                val textStyle = TextStyle(
                    fontSize = TextUnit(20.0f, TextUnitType.Sp),
                    textAlign = TextAlign.Center,
                )
                when (item) {
                    is Numeric -> ClickableText(
                        style = textStyle,
                        modifier = modifier,
                        text = AnnotatedString(item.value),
                        onClick = { onClick.invoke(it, item) },
                    )
                    is Operation.Addition -> ClickableText(
                        style = textStyle,
                        modifier = modifier,
                        text = AnnotatedString("+"),
                        onClick = { onClick.invoke(it, item) },
                    )
                    is Operation.Division -> ClickableText(
                        style = textStyle,
                        modifier = modifier,
                        text = AnnotatedString("/"),
                        onClick = { onClick.invoke(it, item) },
                    )
                    is Operation.Multiplication -> ClickableText(
                        style = textStyle,
                        modifier = modifier,
                        text = AnnotatedString("*"),
                        onClick = { onClick.invoke(it, item) },
                    )
                    is Operation.Subtract -> ClickableText(
                        style = textStyle,
                        modifier = modifier,
                        text = AnnotatedString("-"),
                        onClick = { onClick.invoke(it, item) },
                    )
                    is Parentheses.Back -> ClickableText(
                        style = textStyle,
                        modifier = modifier,
                        text = AnnotatedString(")"),
                        onClick = { onClick.invoke(it, item) },
                    )
                    is Parentheses.Forward -> ClickableText(
                        style = textStyle,
                        modifier = modifier,
                        text = AnnotatedString("("),
                        onClick = { onClick.invoke(it, item) },
                    )
                    is EmptyField -> ClickableText(
                        style = textStyle.copy(color = Color.Red),
                        modifier = modifier,
                        text = AnnotatedString("_"),
                        onClick = { onClick.invoke(it, item) },
                    )
                    else -> throw RuntimeException()
                }
            }
        },
    )
}