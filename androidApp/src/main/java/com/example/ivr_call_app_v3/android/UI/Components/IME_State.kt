package com.example.ivr_call_app_v3.android.UI.Components

import android.view.ViewTreeObserver
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

@Composable
fun rememberImeState(): State<Boolean> {
    val imeState = remember { mutableStateOf(false) }
    val view = LocalView.current

    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            // Check if the keyboard is open by checking the IME insets
            val isKeyboardOpen = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
            imeState.value = isKeyboardOpen
        }

        // Add the listener to the view's tree observer
        view.viewTreeObserver.addOnGlobalLayoutListener(listener)

        // Cleanup to remove the listener when it's no longer needed
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    return imeState
}
