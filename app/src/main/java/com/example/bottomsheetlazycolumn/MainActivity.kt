package com.example.bottomsheetlazycolumn

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ComposeView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateLayoutParams
import com.google.android.material.bottomsheet.BottomSheetBehavior

@ExperimentalComposeUiApi
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBottomSheet()
    }

    private fun setupBottomSheet() {
//        toto add compose view here with lazy column

        val lockableBottomSheetBehavior = LockableBottomSheetBehavior<FrameLayout>(this@MainActivity, null)
        findViewById<FrameLayout>(R.id.bottomSheet).updateLayoutParams<CoordinatorLayout.LayoutParams> {
            this.behavior = lockableBottomSheetBehavior
        }

        BottomSheetBehavior.from(findViewById(R.id.bottomSheet)).apply {
            isFitToContents = true
            isGestureInsetBottomIgnored = false
            skipCollapsed = true
            isHideable = false
            peekHeight = resources.getDimensionPixelSize(R.dimen.peekHeight)
        }

        findViewById<ComposeView>(R.id.composeView).setContent {
            MaterialTheme {
                MyList(scrollListener = { childScrollPosition ->
                    lockableBottomSheetBehavior.childScrollPosition = childScrollPosition
                },
                onTouchEvent = { event ->
                    println("event = $event")
                    findViewById<FrameLayout>(R.id.bottomSheet).onTouchEvent(event)
                })
            }
        }
    }
}

@Suppress("unused")
class LockableBottomSheetBehavior<V : View> : BottomSheetBehavior<V> {
    constructor() : super()
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    var childScrollPosition = 0

    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: V,
        event: MotionEvent
    ): Boolean {
        return if (isSwipeEnabled()) {
            super.onInterceptTouchEvent(parent, child, event)
        } else {
            false
        }
    }

    private fun isSwipeEnabled(): Boolean {
        return if (childScrollPosition > 0) {
            state != BottomSheetBehavior.STATE_EXPANDED
        } else {
            true
        }
    }

    override fun onTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean {
        return if (isSwipeEnabled()) {
            super.onTouchEvent(parent, child, event)
        } else {
            false
        }
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return if (isSwipeEnabled()) {
            super.onStartNestedScroll(
                coordinatorLayout,
                child,
                directTargetChild,
                target,
                axes,
                type
            )
        } else {
            false
        }
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        if (isSwipeEnabled()) {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        }
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        type: Int
    ) {
        if (isSwipeEnabled()) {
            super.onStopNestedScroll(coordinatorLayout, child, target, type)
        }
    }

    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return if (isSwipeEnabled()) {
            super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
        } else {
            false
        }
    }
}