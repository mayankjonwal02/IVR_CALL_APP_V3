//package com.example.ivr_call_app_v3.android.UI.Components
//import androidx.compose.animation.core.LinearEasing
//import androidx.compose.animation.core.RepeatMode
//import androidx.compose.animation.core.animateFloat
//import androidx.compose.animation.core.infiniteRepeatable
//import androidx.compose.animation.core.rememberInfiniteTransition
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.Box
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.geometry.Size
//import androidx.compose.ui.graphics.StrokeCap
//import androidx.compose.ui.graphics.drawscope.Stroke
//import androidx.compose.ui.unit.dp
//
//@Composable
//fun CustomCircularLoadingIndicator(
//    modifier: Modifier = Modifier,
//    color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
//    strokeWidth: Float = 8f
//) {
//    val transition = rememberInfiniteTransition()
//    // Animation for rotating the circular progress
//    val animatedAngle = transition.animateFloat(
//        initialValue = 0f,
//        targetValue = 360f,
//        animationSpec = infiniteRepeatable(
//            tween(durationMillis = 1200, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        )
//    )
//
//    // Canvas to draw the custom circular loading indicator
//    Canvas(modifier = modifier.size(50.dp)) {
//        val diameter = size.minDimension
//        val arcSize = Size(diameter, diameter)
//
//        // Drawing the arc (progress) based on animated angle
//        drawArc(
//            color = color,
//            startAngle = animatedAngle.value,
//            sweepAngle = 270f, // This controls how much of the circle is filled (270 degrees for 3/4 circle)
//            useCenter = false,
//            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
//            size = arcSize,
//            topLeft = Offset((size.width - arcSize.width) / 2, (size.height - arcSize.height) / 2)
//        )
//    }
//}