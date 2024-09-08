package com.example.ivr_call_app_v3.android.UI



import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ivr_call_app_v3.android.Constants.Constants

@Composable
fun Splash()
{
    var IconSize by remember {
        mutableStateOf(Animatable(0f))

    }
    Box(modifier = Modifier.fillMaxSize().background(Color.Transparent), contentAlignment = Alignment.Center , )
    {
            Icon(imageVector = Icons.Filled.Call, contentDescription = "",Modifier.size(IconSize.value.dp))
    }
    LaunchedEffect(Unit)
    {
        IconSize.animateTo(targetValue = 100f, animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutLinearInEasing
        ))
    }
}