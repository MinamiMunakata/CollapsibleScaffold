package com.example.collapsiblescaffold

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.collapsiblescaffold.compose.CollapsibleScaffold
import com.example.collapsiblescaffold.compose.rememberCollapsibleState
import com.example.collapsiblescaffold.ui.theme.CollapsibleScaffoldTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state = rememberCollapsibleState()
            val scope = rememberCoroutineScope()
            CollapsibleScaffoldTheme {
                CollapsibleScaffold(
                    collapsibleAppBar = {
                        Column(
                            modifier = Modifier.collapsible(
                                offset = { IntOffset(0, state.collapsibleOffset.roundToInt()) },
                                onGloballyPositioned = {
                                    state.collapsibleHeight =
                                        it.size.height // getting its height.
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
                                .background(Color.White)
                                .border(border = BorderStroke(1.dp, Color.LightGray)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = it,
                                modifier = Modifier.clickable {
                                    scope.launch {
                                        state.scrollToItem(0)
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
