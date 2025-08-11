package com.example.cashyndoapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cashyndoapp.ui.auth.AuthScreen
import com.example.cashyndoapp.ui.screens.MainScreen
import com.example.cashyndoapp.ui.theme.CashyndoAppTheme
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        setContent {
            CashyndoAppTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "splash"
                ) {
                    composable("splash") {
                        AnimatedSplashScreen(navController)
                    }
                    composable("auth") {
                        AuthScreen(koinViewModel(), koinInject(), navController)
                    }
                    composable("main") {
                        MainScreen()
                    }
                }
            }
        }
    }
}


@Composable
fun AnimatedSplashScreen(
    navController: NavController
) {
    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(2000)
        Log.d("Splash", "Trying to navigate to auth")
        navController.navigate("auth") {
            popUpTo("splash") { inclusive = true }
        }
    }


    val offsetX by animateDpAsState(
        targetValue = if (startAnimation) (-50).dp else 0.dp,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
    )

    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 0.6f else 1f,
        animationSpec = tween(durationMillis = 600)
    )

    val textAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 600, delayMillis = 400)
    )

    LaunchedEffect(Unit) {
        delay(200)
        startAnimation = true
        delay(1200)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.offset(x = offsetX)
        ) {
            Image(
                painter = painterResource(id = R.drawable.cashyndoforeground),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(440.dp)
                    .clip(shape = RoundedCornerShape(16.dp))
                    .graphicsLayer(scaleX = scale, scaleY = scale),
                colorFilter = ColorFilter.tint(color = Color(0xFF043C47))
            )

        }

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "ASHYNDO",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF043C47),
                modifier = Modifier.alpha(textAlpha).padding(end = 80.dp),
                textAlign = TextAlign.Center
            )
        }

    }

}


@Preview
@Composable
private fun SplashScreenPreview() {
    AnimatedSplashScreen(navController = rememberNavController())
}