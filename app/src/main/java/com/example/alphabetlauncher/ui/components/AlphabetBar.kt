package com.example.alphabetlauncher.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.roundToInt

@Composable
fun AlphabetBar(
    onLetterSelected : (Char) -> Unit,
    onDragPosition : (Float,Float) -> Unit,
    onDragEnd: () -> Unit
) {
    val alphabet = ('A'..'Z').toList()

    var fingerY by remember { mutableFloatStateOf(-1f) }

    val curveProgress = remember {
        Animatable(0f)
    }

    LaunchedEffect(fingerY) {
        if (fingerY >= 0f) {
            curveProgress.snapTo(1f)
        } else {
            curveProgress.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 250
                )
            )
        }
    }


    var selectedLetter by remember { mutableStateOf<Char?>(null) }
    var containerHeight by remember { mutableIntStateOf(0) }

    val density = LocalDensity.current
    val verticalPaddingDp = 56.dp
    val verticalPaddingPx = with(density) { verticalPaddingDp.toPx() }

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .onSizeChanged {
                containerHeight = it.height
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        fingerY = offset.y
                        onDragPosition(offset.x, offset.y)

                        val letter = letterFromPosition(
                            y = offset.y,
                            height = size.height,
                            alphabet = alphabet,
                            paddingPx = verticalPaddingPx
                        )
                        selectedLetter = letter
                        onLetterSelected(letter)
                    },

                    onDrag = { change, _ ->
                        fingerY = change.position.y
                        onDragPosition(
                            change.position.x,
                            change.position.y
                        )

                        val letter = letterFromPosition(
                            y = change.position.y,
                            height = size.height,
                            alphabet = alphabet,
                            paddingPx = verticalPaddingPx
                        )
                        selectedLetter = letter
                        onLetterSelected(letter)
                        change.consume()
                    },

                    onDragEnd = {
                        fingerY = -1f
                        selectedLetter = null
                        onDragEnd()
                    },

                    onDragCancel = {
                        fingerY = -1f
                        selectedLetter = null
                        onDragEnd()
                    }
                )
            }
    ) {
        Text(
            text = "★",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 8.dp)
        )

        Text(
            text = "°",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 8.dp)
        )

        AlphabetLetters(
            alphabet = alphabet,
            fingerY = fingerY,
            containerHeight = containerHeight,
            paddingPx = verticalPaddingPx,
            curveProgress = curveProgress.value
        )

        if (fingerY >= 0f && selectedLetter != null) {
            LetterBubble(
                letter = selectedLetter!!,
                modifier = Modifier.offset {
                    IntOffset(
                        x = -80.dp.roundToPx(),
                        y = fingerY.roundToInt() - 32.dp.roundToPx()
                    )
                }
            )
        }
    }
}

@Composable
private fun AlphabetLetters(
    alphabet: List<Char>,
    fingerY: Float,
    containerHeight: Int,
    paddingPx: Float,
    curveProgress: Float
) {
    val density = LocalDensity.current
    val availableHeight = (containerHeight - 2 * paddingPx).coerceAtLeast(1f)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        alphabet.forEachIndexed { index, letter ->
            val letterY = if (containerHeight > 0) {
                paddingPx + (index + 0.5f) * (availableHeight / alphabet.size)
            } else {
                0f
            }

            val (offsetPx, influence) = calculateLetterOffset(
                letterY = letterY,
                fingerY = fingerY,
                curveProgress = curveProgress
            )

            val offsetDp = with(density) { offsetPx.toDp() }
            val yDp = with(density) { letterY.toDp() }
            val scale = 1f + (0.4f * influence * curveProgress)

            Text(
                text = letter.toString(),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 8.dp)
                    .offset(
                        x = -offsetDp,
                        y = yDp - 8.dp
                    )
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
            )
        }
    }
}

private fun calculateLetterOffset(
    letterY: Float,
    fingerY: Float,
    curveProgress: Float
): Pair<Float, Float> {
    if (fingerY < 0f || curveProgress <= 0f) {
        return Pair(0f, 0f)
    }
    val distance = abs(letterY - fingerY)

    val maxOffset = 180f
    val radius = 220f

    val influence = exp(
        -(distance * distance) / (2f * radius * radius)
    )
    val offset = maxOffset * influence * curveProgress
    return Pair(offset, influence)
}

private fun letterFromPosition(
    y: Float,
    height: Int,
    alphabet: List<Char>,
    paddingPx: Float
): Char {
    if (height <= 0) {
        return alphabet.first()
    }

    val availableHeight = (height - 2 * paddingPx).coerceAtLeast(1f)
    val relativeY = y - paddingPx
    val fraction = (relativeY / availableHeight)
        .coerceIn(0f, 0.999999f)

    val index = (fraction * alphabet.size)
        .toInt()
        .coerceIn(0, alphabet.lastIndex)

    return alphabet[index]
}
