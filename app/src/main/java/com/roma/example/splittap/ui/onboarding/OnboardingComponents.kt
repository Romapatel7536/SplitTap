package com.roma.example.splittap.ui.onboarding

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.roma.example.splittap.R

@Composable
internal fun OnboardingGradientButton(
    @StringRes textRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(18.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(shape)
            .background(SplitTapGradient())
            .clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(textRes),
            color = colorResource(R.color.white),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
internal fun OnboardingOutlinedButton(
    @StringRes textRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(18.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(shape)
            .border(
                BorderStroke(1.dp, colorResource(R.color.white).copy(alpha = 0.35f)),
                shape
            )
            .clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(textRes),
            color = colorResource(R.color.white),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
internal fun GradientIconTile(
    @DrawableRes iconRes: Int,
    @ColorRes startColorRes: Int,
    @ColorRes endColorRes: Int,
    @StringRes contentDescriptionRes: Int,
    modifier: Modifier = Modifier,
    size: Dp = 164.dp,
    cornerRadius: Dp = 30.dp,
    iconSize: Dp = 82.dp
) {
    Surface(
        modifier = modifier.size(size),
        shape = RoundedCornerShape(cornerRadius),
        shadowElevation = 18.dp,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier.background(
                Brush.linearGradient(
                    listOf(
                        colorResource(startColorRes),
                        colorResource(endColorRes)
                    )
                )
            ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = stringResource(contentDescriptionRes),
                modifier = Modifier.size(iconSize),
                tint = colorResource(R.color.white)
            )
        }
    }
}

@Composable
internal fun OnboardingDots(
    currentPage: Int,
    pageCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val isSelected = index == currentPage
            Spacer(
                modifier = Modifier
                    .size(
                        width = if (isSelected) 34.dp else 9.dp,
                        height = 9.dp
                    )
                    .background(
                        color = if (isSelected) {
                            colorResource(R.color.onboarding_gradient_end)
                        } else {
                            colorResource(R.color.onboarding_dot_inactive)
                        },
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
internal fun SplitTapGradient(): Brush {
    return Brush.linearGradient(
        listOf(
            colorResource(R.color.onboarding_gradient_start),
            colorResource(R.color.onboarding_gradient_end)
        )
    )
}
