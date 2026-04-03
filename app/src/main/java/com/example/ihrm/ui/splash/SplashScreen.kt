package com.example.ihrm.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ihrm.R
import com.example.ihrm.ui.common.BaseHRMCompose
import com.example.ihrm.ui.theme.Primary200
import com.example.ihrm.ui.theme.SplashBlue100
import com.example.ihrm.ui.theme.SplashBlueMid
import com.example.ihrm.ui.theme.SplashSubtitleBlue
import com.example.ihrm.ui.theme.SplashTitleBlue
import com.example.ihrm.util.EmptyFunc
import kotlinx.coroutines.delay

/**
 * Splash screen UI theo [Figma 341-664](https://www.figma.com/design/Q9qmml4Qj4FD73VnYshBsO/HRM-Mobile-App-Ver?node-id=341-664):
 * Gradient trắng -> xanh, logo Shinhan DS trong ô trắng bo góc, title + subtitle, footer "Digital Solutions".
 */
@Composable
fun SplashScreen(
    onNavigateToLogin: EmptyFunc,
    onNavigateToHome: EmptyFunc,
    isLoggedIn: Boolean,
    viewmodel: SplashViewmodel = hiltViewModel()
){
    BaseHRMCompose(
        content = {
            SplashScreenContent(
                onNavigateToLogin = onNavigateToLogin,
                onNavigateToHome = onNavigateToHome,
                isLoggedIn = isLoggedIn
            )
        },
        viewmodel = viewmodel,
        onErrorAlertClose = onNavigateToLogin
    )
}
@Composable
fun SplashScreenContent(
    onNavigateToLogin: EmptyFunc,
    onNavigateToHome: EmptyFunc,
    isLoggedIn: Boolean,
    viewmodel: SplashViewmodel = hiltViewModel()
) {
    val state = viewmodel.uiState.collectAsState()
    LaunchedEffect(state.value.isLoadingCompleted) {
        if (state.value.isLoadingCompleted) {
            if (isLoggedIn) {
                onNavigateToHome()
            } else {
                onNavigateToLogin()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        SplashBlueMid,
                        SplashBlue100
                    )
                )
            )
    ) {
        Image(
            painter = painterResource(R.drawable.icon_flash),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillWidth
        )

        // Center content
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(28.dp)
                    )
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.shinhan_logo),
                    contentDescription = null,
                    modifier = Modifier.size(72.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.splash_title),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = SplashTitleBlue
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.splash_subtitle).uppercase(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = SplashSubtitleBlue,
                textAlign = TextAlign.Center
            )
        }

        // Footer
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
        ) {
            Image(painterResource(R.drawable.icon_shinhan_friend), contentDescription = "", contentScale = ContentScale.Fit, modifier = Modifier.size(128.dp))
            Text(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp),
                text = stringResource(R.string.splash_footer).uppercase(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Primary200
            )
        }
    }
}

@Preview(name = "Logged Out", showBackground = true)
@Composable
fun SplashScreenLoggedOutPreview() {
    SplashScreen(
        onNavigateToLogin = {},
        onNavigateToHome = {},
        isLoggedIn = false
    )
}

@Preview(name = "Logged In", showBackground = true)
@Composable
fun SplashScreenLoggedInPreview() {
    SplashScreen(
        onNavigateToLogin = {},
        onNavigateToHome = {},
        isLoggedIn = true
    )
}