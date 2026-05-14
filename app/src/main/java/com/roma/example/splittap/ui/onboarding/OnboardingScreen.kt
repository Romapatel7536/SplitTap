package com.roma.example.splittap.ui.onboarding

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.roma.example.splittap.R

private data class OnboardingPage(
    @DrawableRes val iconRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val bodyRes: Int,
    @ColorRes val startColorRes: Int,
    @ColorRes val endColorRes: Int
)

private val OnboardingPages = listOf(
    OnboardingPage(
        iconRes = R.drawable.ic_receipt_24,
        titleRes = R.string.onboarding_track_title,
        bodyRes = R.string.onboarding_track_body,
        startColorRes = R.color.onboarding_blue_start,
        endColorRes = R.color.onboarding_blue_end
    ),
    OnboardingPage(
        iconRes = R.drawable.ic_groups_24,
        titleRes = R.string.onboarding_groups_title,
        bodyRes = R.string.onboarding_groups_body,
        startColorRes = R.color.onboarding_teal_start,
        endColorRes = R.color.onboarding_teal_end
    ),
    OnboardingPage(
        iconRes = R.drawable.ic_person_add_24,
        titleRes = R.string.onboarding_roommates_title,
        bodyRes = R.string.onboarding_roommates_body,
        startColorRes = R.color.onboarding_orange_start,
        endColorRes = R.color.onboarding_orange_end
    ),
    OnboardingPage(
        iconRes = R.drawable.ic_savings_24,
        titleRes = R.string.onboarding_balances_title,
        bodyRes = R.string.onboarding_balances_body,
        startColorRes = R.color.onboarding_green_start,
        endColorRes = R.color.onboarding_green_end
    )
)

@Composable
fun OnboardingScreen(
    uiState: OnboardingUiState,
    onSkipClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val page = OnboardingPages[uiState.currentPage.coerceIn(OnboardingPages.indices)]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.onboarding_background))
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 480.dp)
                .align(Alignment.TopCenter)
                .padding(horizontal = 18.dp, vertical = 18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onSkipClick) {
                    Text(
                        text = stringResource(R.string.onboarding_skip),
                        color = colorResource(R.color.onboarding_muted),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                GradientIconTile(
                    iconRes = page.iconRes,
                    startColorRes = page.startColorRes,
                    endColorRes = page.endColorRes,
                    contentDescriptionRes = R.string.onboarding_cd_slide_icon
                )

                Spacer(modifier = Modifier.height(34.dp))
                Text(
                    text = stringResource(page.titleRes),
                    color = colorResource(R.color.onboarding_ink),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = MaterialTheme.typography.headlineMedium.lineHeight
                )

                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = stringResource(page.bodyRes),
                    color = colorResource(R.color.onboarding_muted),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeight = MaterialTheme.typography.titleLarge.lineHeight
                )

                Spacer(modifier = Modifier.height(34.dp))
                OnboardingDots(
                    currentPage = uiState.currentPage,
                    pageCount = uiState.pageCount
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
            OnboardingGradientButton(
                textRes = if (uiState.isLastPage) {
                    R.string.onboarding_get_started
                } else {
                    R.string.onboarding_next
                },
                onClick = onNextClick
            )
        }
    }
}
