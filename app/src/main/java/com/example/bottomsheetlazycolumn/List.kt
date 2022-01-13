package com.example.bottomsheetlazycolumn

import android.view.MotionEvent
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@ExperimentalComposeUiApi
@Composable
@Preview
fun MyList(
    scrollListener: (Int) -> Unit = {},
    toggle: () -> Unit = {},
    onTouchEvent: (MotionEvent) -> Unit = {}
) {
    val listState = rememberLazyListState()
    scrollListener(listState.firstVisibleItemScrollOffset)

    val myItems = remember {
        (1..100).toList().map { "Item $it" }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Button(onClick = toggle) {
            Text(text = "Toggle")
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            state = listState,
            content = {
                items(myItems.size) {
                    Text(text = myItems[it], modifier = Modifier.height(40.dp))
                }
            }
        )
    }
}