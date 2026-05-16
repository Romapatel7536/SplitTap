package com.roma.example.splittap.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.roma.example.splittap.R

@Composable
fun GetStartedScreen(
    onGetStartedClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SplitTapGradient())
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp)
                .align(Alignment.TopCenter)
                .padding(horizontal = 18.dp, vertical = 18.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                SplitTapLogoCluster()

                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = stringResource(R.string.app_name),
                    color = colorResource(R.color.white),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.onboarding_app_tagline),
                    color = colorResource(R.color.white).copy(alpha = 0.9f),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeight = MaterialTheme.typography.titleLarge.lineHeight
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
            OnboardingGetStartedButton(onClick = onGetStartedClick)

            Spacer(modifier = Modifier.height(14.dp))
            OnboardingOutlinedButton(
                textRes = R.string.onboarding_login,
                onClick = onLoginClick
            )
        }
    }
}

@Composable
private fun SplitTapLogoCluster() {
    Box(
        modifier = Modifier.size(152.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.size(96.dp),
            shape = RoundedCornerShape(24.dp),
            color = colorResource(R.color.white),
            shadowElevation = 18.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(R.drawable.ic_dollar_24),
                    contentDescription = stringResource(R.string.onboarding_cd_logo),
                    modifier = Modifier.size(60.dp),
                    tint = colorResource(R.color.onboarding_gradient_start)
                )
            }
        }

        FloatingIconTile(
            iconRes = R.drawable.ic_groups_24,
            contentDescriptionRes = R.string.onboarding_cd_groups,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(64.dp)
        )

        FloatingIconTile(
            iconRes = R.drawable.ic_trending_up_24,
            contentDescriptionRes = R.string.onboarding_cd_trending,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(58.dp)
        )
    }
}

@Composable
private fun FloatingIconTile(
    iconRes: Int,
    contentDescriptionRes: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = colorResource(R.color.white).copy(alpha = 0.22f),
        shadowElevation = 10.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = stringResource(contentDescriptionRes),
                modifier = Modifier.size(30.dp),
                tint = colorResource(R.color.white)
            )
        }
    }
}

@Composable
private fun OnboardingGetStartedButton(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(18.dp),
        color = colorResource(R.color.white),
        shadowElevation = 10.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(R.string.onboarding_get_started),
                color = colorResource(R.color.onboarding_gradient_start),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
