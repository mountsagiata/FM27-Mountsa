package com.mountsa.fm2027.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mountsa.fm2027.ui.theme.Fm2027Theme
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(onStart: () -> Unit) {
    var progress by remember { mutableStateOf(0f) }
    var isLoadingComplete by remember { mutableStateOf(false) }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 3000),
        label = "LoadingProgress"
    )

    LaunchedEffect(Unit) {
        progress = 1f
        delay(3200) // Wait for animation to finish
        isLoadingComplete = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050B13)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.White)) {
                        append("FM")
                    }
                    withStyle(style = SpanStyle(color = Color(0xFF2ECC71))) {
                        append("2027")
                    }
                },
                fontSize = 48.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )
            
            Text(
                text = "SYSTEM INITIALIZING",
                fontSize = 10.sp,
                fontWeight = FontWeight.Light,
                color = Color(0xFF2ECC71),
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(60.dp))

            Box(contentAlignment = Alignment.Center) {
                // Show Loading Bar while progress < 1.0
                // Use explicit scope to avoid ambiguity with ColumnScope.AnimatedVisibility
                this@Column.AnimatedVisibility(
                    visible = !isLoadingComplete,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .width(200.dp)
                            .height(2.dp),
                        color = Color(0xFF2ECC71),
                        trackColor = Color.White.copy(alpha = 0.1f),
                    )
                }

                // Show Button when loading is complete
                this@Column.AnimatedVisibility(
                    visible = isLoadingComplete,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Button(
                        onClick = onStart,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2ECC71),
                            contentColor = Color.Black
                        ),
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier
                            .height(48.dp)
                            .width(200.dp)
                    ) {
                        Text(
                            "START CAREER",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    Fm2027Theme {
        LoadingScreen(onStart = {})
    }
}
