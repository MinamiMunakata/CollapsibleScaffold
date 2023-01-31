package com.example.collapsiblescaffold.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.collapsiblescaffold.ui.theme.CollapsibleScaffoldTheme
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Container with CollapsingToolbar which has an enterAlways-like behavior of CoordinatorLayout.
 * @param collapsibleAppBar AppBar that expands and collapses with scrolling.
 * @param content Scrollable content which has PaddingValues of AppBar height.
 */
@Composable
fun CollapsibleScaffold(
    collapsibleAppBar: @Composable CollapsibleScope.() -> Unit,
    modifier: Modifier = Modifier,
    state: CollapsibleState = rememberCollapsibleState(),
    content: LazyListScope.() -> Unit,
) {
    val scope = remember { CollapsibleScope() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(state.nestedScrollConnection),
    ) {
        LazyColumn(
            contentPadding = PaddingValues(top = with(LocalDensity.current) { state.collapsibleHeight.toDp() }),
            state = state.listState,
            content = content,
        )

        scope.collapsibleAppBar()
    }
}

/**
 * Create a CollapsibleState.
 * @param collapsibleHeight Collapsible area height, e.g. AppBar height.
 */
@Composable
fun rememberCollapsibleState(
    collapsibleHeight: Int = 0,
    collapsibleOffset: Float = 0f,
    listState: LazyListState = rememberLazyListState(),
): CollapsibleState {
    val state by rememberSaveable(stateSaver = CollapsibleState.provideSaver(listState)) {
        mutableStateOf(CollapsibleState(collapsibleHeight, collapsibleOffset, listState))
    }
    return state
}

/**
 * State for CollapsibleScaffold composable component.
 * @property collapsibleHeight Collapsible area height.
 * @property collapsibleOffset Scroll offset of the collapsible area.
 */
@Stable
class CollapsibleState(
    collapsibleHeight: Int,
    collapsibleOffset: Float,
    val listState: LazyListState,
) {
    var collapsibleHeight by mutableStateOf(collapsibleHeight)
    var collapsibleOffset by mutableStateOf(collapsibleOffset)

    val nestedScrollConnection: NestedScrollConnection
        get() = object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = collapsibleOffset + delta
                collapsibleOffset = newOffset.coerceIn(-collapsibleHeight.toFloat(), 0f)

                return Offset.Zero
            }
        }

    /**
     * Instantly brings the item at the index to the bottom of the collapsible area.
     */
    suspend fun scrollToItem(index: Int) {
        listState.scrollToItem(
            index,
            abs(collapsibleOffset.roundToInt()), // Set the offset to scroll the item further up when the AppBar is collapsed.
        )
    }

    companion object {
        fun provideSaver(listState: LazyListState) = mapSaver(
            save = {
                mapOf(
                    "collapsibleHeight" to it.collapsibleHeight,
                    "collapsibleOffset" to it.collapsibleOffset,
                )
            },
            restore = {
                CollapsibleState(
                    collapsibleHeight = it["collapsibleHeight"] as Int,
                    collapsibleOffset = it["collapsibleOffset"] as Float,
                    listState = listState,
                )
            }
        )
    }
}

/**
 * Receiver scope which is used by CollapsibleScaffold.
 */
class CollapsibleScope {
    /**
     * Decorates CollapsibleAppBar.
     * @param offset Change the position of the AppBar so that it shows expanding-collapsing behavior when scrolling.
     * @param elevation Shows a CoordinatorLayout-like shadow.
     * @param onGloballyPositioned Gives LayoutCoordinates to calculate the CollapsibleAppBar height.
     */
    @Stable
    fun Modifier.collapsible(
        offset: Density.() -> IntOffset,
        elevation: Dp = 4.dp,
        onGloballyPositioned: (LayoutCoordinates) -> Unit = {},
    ): Modifier {
        return onGloballyPositioned(onGloballyPositioned)
            .offset(offset)
            .shadow(elevation)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCollapsibleScaffoldWithBox() {
    val state = rememberCollapsibleState()
    CollapsibleScaffoldTheme {
        CollapsibleScaffold(
            collapsibleAppBar = {
                Column(
                    modifier = Modifier.collapsible(
                        offset = { IntOffset(0, state.collapsibleOffset.roundToInt()) },
                        onGloballyPositioned = {
                            state.collapsibleHeight = it.size.height // getting its height.
                        }
                    ),
                ) {
                    TopAppBar(title = { Text(text = "CollapsingToolbar") })

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color.Yellow),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "e.g. Your Image")
                    }
                }
            },
            state = state,
        ) {
            items(List(10) { "item $it" }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(Color.White),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = it)
                }
            }
        }
    }
}
